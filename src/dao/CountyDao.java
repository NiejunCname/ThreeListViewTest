package dao;

import java.util.ArrayList;
import java.util.List;

import model.County;
import util.WeatherDBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CountyDao
{
  private WeatherDBHelper helper;

  public CountyDao(Context paramContext)
  {
    this.helper = new WeatherDBHelper(paramContext);
  }

  public List<County> findByCity(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getReadableDatabase();
    ArrayList localArrayList = new ArrayList();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramInt);
    Cursor localCursor = localSQLiteDatabase.query("county", null, "cityid=?", arrayOfString, null, null, null);
    while (localCursor.moveToNext())
    {
      int i = localCursor.getInt(localCursor.getColumnIndex("id"));
      String str1 = localCursor.getString(localCursor.getColumnIndex("countyname"));
      String str2 = localCursor.getString(localCursor.getColumnIndex("weatherid"));
      County localCounty = new County();
      localCounty.setId(i);
      localCounty.setCountyName(str1);
      localCounty.setWeatherId(str2);
      localCounty.setCityId(paramInt);
      localArrayList.add(localCounty);
    }
    localCursor.close();
    localSQLiteDatabase.close();
    return localArrayList;
  }

  public String getCountyNameById(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getReadableDatabase();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramInt);
    Cursor localCursor = localSQLiteDatabase.query("county", null, "id=?", arrayOfString, null, null, null);
    String str = "";
    if (localCursor.moveToFirst())
      str = localCursor.getString(localCursor.getColumnIndex("countyname"));
    localCursor.close();
    localSQLiteDatabase.close();
    return str;
  }

  public long insert(County paramCounty)
  {
    SQLiteDatabase localSQLiteDatabase = this.helper.getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("countyname", paramCounty.getCountyName());
    localContentValues.put("weatherid", paramCounty.getWeatherId());
    localContentValues.put("cityid", Integer.valueOf(paramCounty.getCityId()));
    long l = localSQLiteDatabase.insert("county", null, localContentValues);
    localSQLiteDatabase.close();
    return l;
  }
}