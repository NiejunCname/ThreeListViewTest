package com.example.threework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import util.SettingSave;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import dao.CityDao;
import dao.CountyDao;
import dao.ProvinceDao;

public class MainActivity extends AppCompatActivity
{
  public static final int LEVEL_CITY = 1;
  public static final int LEVEL_COUNTY = 2;
  public static final int LEVEL_PROVINCE=0;
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
    String str = new ProvinceDao(this).getProvinceNameById(paramInt);
    this.titleText.setText(str);
    this.backButton.setVisibility(0);
    this.cityList = new CityDao(this).findByProvince(paramInt);
    this.dataList.clear();
    Iterator localIterator = this.cityList.iterator();
    while (localIterator.hasNext())
    {
      City localCity = (City)localIterator.next();
      this.dataList.add(localCity.getCityName());
    }
    this.adapter.notifyDataSetChanged();
    this.listView.setSelection(0);
    this.currentLevel = 1;
  }

  private void queryCounties(int paramInt)
  {
    String str = new CityDao(this).getCityNameById(paramInt);
    this.titleText.setText(str);
    this.backButton.setVisibility(0);
    this.countyList = new CountyDao(this).findByCity(paramInt);
    this.dataList.clear();
    Iterator localIterator = this.countyList.iterator();
    while (localIterator.hasNext())
    {
      County localCounty = (County)localIterator.next();
      this.dataList.add(localCounty.getCountyName());
    }
    this.adapter.notifyDataSetChanged();
    this.listView.setSelection(0);
    this.currentLevel = 2;
  }

  private void queryProvinces()
  {
    this.titleText.setText("中国");
    /*this.backButton.setVisibility(8);*/
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
    InputStream localInputStream;
    FileOutputStream localFileOutputStream;
    if (!new File("/data/data/com.example.threework/databases/weather_area.db").exists())
    {
      File localFile = new File("/data/data/com.example.threework/databases/");
      if (!localFile.exists())
        localFile.mkdir();
      try
      {
        localInputStream = getAssets().open("weather_area.db");
        localFileOutputStream = new FileOutputStream("/data/data/com.example.threework/databases/weather_area.db");
        byte[] arrayOfByte = new byte[1024];
        while (true)
        {
          int i = localInputStream.read(arrayOfByte);
          if (i <= 0)
            break;
          localFileOutputStream.write(arrayOfByte, 0, i);
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
    else
    {
      return;
    }
    localFileOutputStream.flush();
    localFileOutputStream.close();
    localInputStream.close();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968603);
    Bundle localBundle = SettingSave.getSelect(this);
    this.titleText = ((TextView)findViewById(2131427414));
    this.backButton = ((Button)findViewById(2131427415));
    this.listView = ((ListView)findViewById(2131427416));
    this.adapter = new ArrayAdapter(this, 17367043, this.dataList);
    this.listView.setAdapter(this.adapter);
    this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        if (MainActivity.this.currentLevel == 0)
        {
          MainActivity.access$102(MainActivity.this, ((Province)MainActivity.this.provinceList.get(paramAnonymousInt)).getId());
          MainActivity.this.queryCities(MainActivity.this.selectedProvinceId);
        }
        do
        {
          return;
          if (MainActivity.this.currentLevel == 1)
          {
            MainActivity.ACCESSIBILITY_SERVICE(MainActivity.this, ((City)MainActivity.this.cityList.get(paramAnonymousInt)).getId());
            MainActivity.this.queryCounties(MainActivity.this.selectedCityId);
            return;
          }
        }
        while (MainActivity.this.currentLevel != 2);
        Toast.makeText(MainActivity.this, "天气展示该项功能暂未实现", 0).show();
      }
    });
    this.backButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (MainActivity.this.currentLevel == 2)
          MainActivity.this.queryCities(MainActivity.this.selectedProvinceId);
        while (MainActivity.this.currentLevel != 1)
          return;
        MainActivity.this.queryProvinces();
      }
    });
    if (localBundle.getInt("selectLevel") != -1)
    {
      this.currentLevel = localBundle.getInt("selectLevel");
      this.selectedProvinceId = localBundle.getInt("provinceId");
      this.selectedCityId = localBundle.getInt("cityId");
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
      case 2:
      }
      queryCounties(this.selectedCityId);
      return;
    }
    importDatabase();
    queryProvinces();
  }

  protected static void ACCESSIBILITY_SERVICE(MainActivity mainActivity, int id) {
	// TODO Auto-generated method stub
	
}

protected void onDestroy()
  {
    SettingSave.saveSelectLevel(this, this.currentLevel);
    SettingSave.saveSelectProvince(this, this.selectedProvinceId);
    SettingSave.saveSelectCity(this, this.selectedCityId);
    super.onDestroy();
  }
}