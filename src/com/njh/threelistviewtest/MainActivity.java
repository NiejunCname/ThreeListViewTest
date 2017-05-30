package com.njh.threelistviewtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.threelistviewtest.R;
import com.njh.threelistviewtest.dao.CityDao;
import com.njh.threelistviewtest.dao.CountyDao;
import com.njh.threelistviewtest.dao.ProvinceDao;
import com.njh.threelistviewtest.modle.City;
import com.njh.threelistviewtest.modle.County;
import com.njh.threelistviewtest.modle.Province;
import com.njh.threelistviewtest.util.SettingSave;

public class MainActivity
  extends AppCompatActivity
{
  public static final int LEVEL_CITY = 1;
  public static final int LEVEL_COUNTY = 2;
  public static final int LEVEL_PROVINCE = 0;
  private ArrayAdapter<String> adapter;
  private Button backButton;
  private List<City> cityList;
  private List<County> countyList;
  private int currentLevel;
  private List<String> dataList = new ArrayList();
  private ListView listView;
  private List<Province> provinceList;
  private int selectedCityId;
  private int selectedProvinceId;
  private TextView titleText;
  
  private void queryCities(int paramInt)
  {
    Object localObject = new ProvinceDao(this).getProvinceNameById(paramInt);
    this.titleText.setText((CharSequence)localObject);
    this.backButton.setVisibility(0);
    this.cityList = new CityDao(this).findByProvince(paramInt);
    this.dataList.clear();
    localObject = this.cityList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      City localCity = (City)((Iterator)localObject).next();
      this.dataList.add(localCity.getCityName());
    }
    this.adapter.notifyDataSetChanged();
    this.listView.setSelection(0);
    this.currentLevel = 1;
  }
  
  private void queryCounties(int paramInt)
  {
    Object localObject = new CityDao(this).getCityNameById(paramInt);
    this.titleText.setText((CharSequence)localObject);
    this.backButton.setVisibility(0);
    this.countyList = new CountyDao(this).findByCity(paramInt);
    this.dataList.clear();
    localObject = this.countyList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      County localCounty = (County)((Iterator)localObject).next();
      this.dataList.add(localCounty.getCountyName());
    }
    this.adapter.notifyDataSetChanged();
    this.listView.setSelection(0);
    this.currentLevel = 2;
  }
  
  private void queryProvinces()
  {
    this.titleText.setText("中国");
    this.backButton.setVisibility(8);
    this.provinceList = new ProvinceDao(this).findAll();
    this.dataList.clear();
    Iterator localIterator = this.provinceList.iterator();
    while (localIterator.hasNext())
    {
      Province localProvince = (Province)localIterator.next();
      this.dataList.add(localProvince.getProvinceName());
    }
    this.adapter.notifyDataSetChanged();
    this.listView.setSelection(0);
    this.currentLevel = 0;
  }
  
  public void importDatabase()
  {
    FileOutputStream localFileOutputStream;
    if (!new File("/data/data/com.njh.threelistviewtest/databases/weather_area.db").exists())
    {
      Object localObject = new File("/data/data/com.njh.threelistviewtest/databases/");
      if (!((File)localObject).exists()) {
        ((File)localObject).mkdir();
      }
      try
      {
        localObject = getAssets().open("weather_area.db");
        localFileOutputStream = new FileOutputStream("/data/data/com.njh.threelistviewtest/databases/weather_area.db");
        byte[] arrayOfByte = new byte[1024];
        for (;;)
        {
          int i = ((InputStream)localObject).read(arrayOfByte);
          if (i <= 0) {
            break;
          }
          localFileOutputStream.write(arrayOfByte, 0, i);
        }
        return;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
    localFileOutputStream.flush();
    localFileOutputStream.close();
    localException.close();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_main);
    paramBundle = SettingSave.getSelect(this);
    this.titleText = (TextView)findViewById(R.id.title_text);
    this.backButton = (Button)findViewById(R.id.back_button);
    this.listView = (ListView)findViewById(R.id.list_view);
    this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.dataList);
    this.listView.setAdapter(this.adapter);
    this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        if (currentLevel == LEVEL_PROVINCE)
        {        	
          provinceList.get(paramAnonymousInt).getId();
          queryCities(selectedProvinceId);      
          return;}
        else if (currentLevel == LEVEL_CITY)
          {
        	  
        	 cityList.get(paramAnonymousInt).getId();
            queryCounties(selectedCityId);
            return;
          
        } 
      else if (currentLevel != LEVEL_COUNTY);
        Toast.makeText(MainActivity.this, "天气功能暂未显示", 0).show();
      }
    });
    this.backButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (currentLevel == 2) {
          queryCities(selectedProvinceId);
        }
        while (currentLevel != 1) {
          return;
        }
        queryProvinces();
      }
    });
    if (paramBundle.getInt("selectLevel") != -1)
    {
      this.currentLevel = paramBundle.getInt("selectLevel");
      this.selectedProvinceId = paramBundle.getInt("provinceId");
      this.selectedCityId = paramBundle.getInt("cityId");
      switch (this.currentLevel)
      {
      default: 
        return;
      case 0: 
        queryProvinces();
        return;
      case 1: 
        queryCities(this.selectedProvinceId);
        return;
      case 3:
    	  queryCounties(this.selectedCityId);
          return;      
    }}
    importDatabase();
    queryProvinces();
  }
  
  protected void onDestroy()
  {
    SettingSave.saveSelectLevel(this, this.currentLevel);
    SettingSave.saveSelectProvince(this, this.selectedProvinceId);
    SettingSave.saveSelectCity(this, this.selectedCityId);
    super.onDestroy();
  }
}
