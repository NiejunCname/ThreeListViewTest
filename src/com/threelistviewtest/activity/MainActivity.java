package com.threelistviewtest.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.threelistviewtest.R;
import com.threelistviewtest.db.tqiDB;
import com.threelistviewtest.model.City;
import com.threelistviewtest.model.County;
import com.threelistviewtest.model.Province;
import com.threelistviewtest.util.HttpCallbackListener;
import com.threelistviewtest.util.HttpUtil;
import com.threelistviewtest.util.Utility;

public class MainActivity extends Activity {

	public static final int LVL_PROVINCE = 0;
	public static final int LVL_CITY = 1;
	public static final int LVL_COUNTY = 2;
	 
	private Button btnfanhui;
	private ProgressDialog prodlog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private tqiDB tqiDb;
	private List<String> dataList = new ArrayList<String>();
	/**
	 * 省列表
	 */
	private List<Province> provinceLists;
	/**
	 * 市列表
	 */
	private List<City> citylists;
	/**
	 * 县列表
	 */
	private List<County> countyLists;
	/**
	 * 选中的省份
	 */
	private Province xuanzeProvince;
	/**
	 * 选中的城市
	 */
	private City xuanzeCity;
	/**
	 * 当前选中的级别
	 */
	private int dqlvl;
	/**
	 * 是否从WeatherActivity中跳转过来。
	 */
	private boolean isFromWeatherActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity) {
			Intent intent = new Intent(this, TwoActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity1);
		btnfanhui=(Button)findViewById(R.id.back_button);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		tqiDb = tqiDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				if (dqlvl == LVL_PROVINCE) {
					xuanzeProvince = provinceLists.get(index);
					queryCities();
				} else if (dqlvl == LVL_CITY) {
					xuanzeCity = citylists.get(index);
					queryCounties();
				} else if (dqlvl == LVL_COUNTY) {
					String countyCode = countyLists.get(index).getCountyCode();
					Intent intent = new Intent(MainActivity.this, TwoActivity.class);
					intent.putExtra("county_code", countyCode);
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();  // 加载省级数据
		btnfanhui.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(dqlvl==LVL_CITY)
				{
					queryProvinces();
				}
				else if(dqlvl==LVL_COUNTY)
				{
					queryCities();
				}
				
			}
		});
	}

	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
	 */
	private void queryProvinces() {
		provinceLists = tqiDb.loadProvinces();
		if (provinceLists.size() > 0) {
			dataList.clear();
			for (Province province : provinceLists) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			btnfanhui.setVisibility(8);
			listView.setSelection(0);
			titleText.setText("中国");
			dqlvl = LVL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
	 */
	private void queryCities() {
		citylists = tqiDb.loadCities(xuanzeProvince.getId());
		if (citylists.size() > 0) {
			dataList.clear();
			for (City city : citylists) {
				dataList.add(city.getCityName());
			}
			btnfanhui.setVisibility(0);
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(xuanzeProvince.getProvinceName());
			dqlvl = LVL_CITY;
		} else {
			queryFromServer(xuanzeProvince.getProvinceCode(), "city");
		}
	}
	
	/**
	 * 查询选中省内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
	 */
	private void queryCounties() {
		countyLists = tqiDb.loadCounties(xuanzeCity.getId());
		if (countyLists.size() > 0) {
			dataList.clear();
			for (County county : countyLists) {
				dataList.add(county.getCountyName());
			}
			btnfanhui.setVisibility(0);
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(xuanzeCity.getCityName());
			dqlvl = LVL_COUNTY;
		} else {
			queryFromServer(xuanzeCity.getCityCode(), "county");
		}
	}
	
	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据。
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(tqiDb,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(tqiDb,
							response, xuanzeProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(tqiDb,
							response, xuanzeCity.getId());
				}
				if (result) {
					// // 通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// // 通过runOnUiThread()方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(MainActivity.this,
										"加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	/**
	 *  显示进度对话框
	 */
	private void showProgressDialog() {
		if (prodlog == null) {
			prodlog = new ProgressDialog(this);
			prodlog.setMessage("正在加载...");
			prodlog.setCanceledOnTouchOutside(false);
		}
		prodlog.show();
	}
	
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (prodlog != null) {
			prodlog.dismiss();
		}
	}
	
	/*@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 SettingSave.saveSelectLevel(this, dqlvl);
		    SettingSave.saveSelectProvince(this, xuanzeProvince.getId());
		    SettingSave.saveSelectCity(this, xuanzeCity.getId());
		    super.onDestroy();
	}
*/
	/**
	 * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
	 */
	@Override
	public void onBackPressed() {
		if (dqlvl == LVL_COUNTY) {
			queryCities();
		} else if (dqlvl == LVL_CITY) {
			queryProvinces();
		} else {
			if (isFromWeatherActivity) {
				Intent intent = new Intent(this, TwoActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}

}