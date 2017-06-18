package com.threelistviewtest.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threelistviewtest.R;
import com.threelistviewtest.service.UpdateService;
import com.threelistviewtest.util.HttpCallbackListener;
import com.threelistviewtest.util.HttpUtil;
import com.threelistviewtest.util.Utility;

public class TwoActivity extends Activity implements OnClickListener{


	/**
	 * 用于显示气温1
	 */
	private TextView temp1txt;
	/**
	 * 用于显示气温2
	 */
	private TextView temp2txt;
	/**
	 * 用于显示当前日期
	 */
	private TextView currentDatetxt;
	/**
	 * 切换城市按钮
	 */
	private Button qhcitybtn;
	/**
	 * 更新天气按钮
	 */
	private Button gxtq;
	private LinearLayout tqxxlayout;
	/**
	 * 用于显示城市名
	 */
	private TextView citynametxt;
	/**
	 * 用于显示发布时间
	 */
	private TextView publishtxt;
	/**
	 * 用于显示天气描述信息
	 */
	private TextView weatherDesptxt;
	
	
	
	/**
	 *根据传入的地址和类型去向服务器查询天气代号或者天气信息。
	 */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// 从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					// 处理服务器返回的天气信息
					Utility.handleWeatherResponse(TwoActivity.this, response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						publishtxt.setText("同步失败");
					}
				});
			}
		});
	}
	/**
	 * 查询天气代号所对应的天气。
	 */
	private void queryWeatherInfo(String weatherCode) {
		
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}
	
	
	/**
	 * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		citynametxt.setText( prefs.getString("city_name", ""));
		temp1txt.setText(prefs.getString("temp1", ""));
		temp2txt.setText(prefs.getString("temp2", ""));
		weatherDesptxt.setText(prefs.getString("weather_desp", ""));
		publishtxt.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDatetxt.setText(prefs.getString("current_date", ""));
		tqxxlayout.setVisibility(View.VISIBLE);
		citynametxt.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, UpdateService.class);
		startService(intent);
	}
	/**
	 * 查询县级代号所对应的天气代号。
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		queryFromServer(address, "countyCode");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity2);
		// 初始化各控件
		tqxxlayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		citynametxt = (TextView) findViewById(R.id.city_name);
		publishtxt = (TextView) findViewById(R.id.publish_text);
		weatherDesptxt = (TextView) findViewById(R.id.weather_desp);
		temp1txt = (TextView) findViewById(R.id.temp1);
		temp2txt = (TextView) findViewById(R.id.temp2);
		currentDatetxt = (TextView) findViewById(R.id.current_date);
		qhcitybtn = (Button) findViewById(R.id.switch_city);
		gxtq = (Button) findViewById(R.id.refresh_weather);
		String countyCode = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countyCode)) {
			// 有县级代号时就去查询天气
			publishtxt.setText("同步中...");
			tqxxlayout.setVisibility(View.INVISIBLE);
			citynametxt.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			// 没有县级代号时就直接显示本地天气
			showWeather();
		}
		qhcitybtn.setOnClickListener(this);
		gxtq.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishtxt.setText("同步中...");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}

}