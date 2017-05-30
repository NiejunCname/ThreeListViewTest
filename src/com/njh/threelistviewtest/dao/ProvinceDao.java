package com.njh.threelistviewtest.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.njh.threelistviewtest.modle.Province;
import com.njh.threelistviewtest.util.WeatherDBHelper;

public class ProvinceDao
{
  private WeatherDBHelper helper;

  public ProvinceDao(Context paramContext)
  {
    this.helper = new WeatherDBHelper(paramContext);
  }

  public int delete(Province paramProvince)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getWritableDatabase();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = paramProvince.getProvinceName();
    int i = localSQLiteDatabase.delete("province", "provincename=?", arrayOfString);
    localSQLiteDatabase.close();
    return i;
  }

  public List<Province> findAll()
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getReadableDatabase();
    ArrayList localArrayList = new ArrayList();
    Cursor localCursor = localSQLiteDatabase.query("province", null, null, null, null, null, null);
    while (localCursor.moveToNext())
    {
      int i = localCursor.getInt(localCursor.getColumnIndex("id"));
      String str = localCursor.getString(localCursor.getColumnIndex("provincename"));
      int j = localCursor.getInt(localCursor.getColumnIndex("provincecode"));
      Province localProvince = new Province();
      localProvince.setId(i);
      localProvince.setProvinceName(str);
      localProvince.setProvinceCode(j);
      localArrayList.add(localProvince);
    }
    localCursor.close();
    localSQLiteDatabase.close();
    return localArrayList;
  }

  public String getProvinceNameById(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getReadableDatabase();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramInt);
    Cursor localCursor = localSQLiteDatabase.query("province", null, "id=?", arrayOfString, null, null, null);
    String str = "";
    if (localCursor.moveToFirst())
      str = localCursor.getString(localCursor.getColumnIndex("provincename"));
    localCursor.close();
    localSQLiteDatabase.close();
    return str;
  }

  public long insert(Province paramProvince)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("provincename", paramProvince.getProvinceName());
    localContentValues.put("provincecode", Integer.valueOf(paramProvince.getProvinceCode()));
    long l = localSQLiteDatabase.insert("province", null, localContentValues);
    localSQLiteDatabase.close();
    return l;
  }
}