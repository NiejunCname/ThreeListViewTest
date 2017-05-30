package com.njh.threelistviewtest.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.njh.threelistviewtest.modle.City;
import com.njh.threelistviewtest.util.WeatherDBHelper;

public class CityDao
{
  private WeatherDBHelper helper;

  public CityDao(Context paramContext)
  {
    this.helper = new WeatherDBHelper(paramContext);
  }

  public List<City> findByProvince(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getReadableDatabase();
    ArrayList localArrayList = new ArrayList();
    Cursor localCursor = localSQLiteDatabase.query("city", null, "provinceid=?", new String[] { String.valueOf(paramInt) }, null, null, null);
    while (localCursor.moveToNext())
    {
      int i = localCursor.getInt(localCursor.getColumnIndex("id"));
      String str = localCursor.getString(localCursor.getColumnIndex("cityname"));
      int j = localCursor.getInt(localCursor.getColumnIndex("citycode"));
      City localCity = new City();
      localCity.setId(i);
      localCity.setCityName(str);
      localCity.setCityCode(j);
      localCity.setProvinceId(paramInt);
      localArrayList.add(localCity);
    }
    localCursor.close();
    localSQLiteDatabase.close();
    return localArrayList;
  }

  public String getCityNameById(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getReadableDatabase();
    Cursor localCursor = localSQLiteDatabase.query("city", null, "id=?", new String[] { String.valueOf(paramInt) }, null, null, null);
    String str = "";
    if (localCursor.moveToFirst())
      str = localCursor.getString(localCursor.getColumnIndex("cityname"));
    localCursor.close();
    localSQLiteDatabase.close();
    return str;
  }

  public long insert(City paramCity)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("cityname", paramCity.getCityName());
    localContentValues.put("citycode", Integer.valueOf(paramCity.getCityCode()));
    localContentValues.put("provinceid", Integer.valueOf(paramCity.getProvinceId()));
    long l = localSQLiteDatabase.insert("city", null, localContentValues);
    localSQLiteDatabase.close();
    return l;
  }
}