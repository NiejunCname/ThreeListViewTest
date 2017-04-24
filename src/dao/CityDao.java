package dao;

import java.util.ArrayList;
import java.util.List;

import model.City;
import util.WeatherDBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramInt);
    Cursor localCursor = localSQLiteDatabase.query("city", null, "provinceid=?", arrayOfString, null, null, null);
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
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramInt);
    Cursor localCursor = localSQLiteDatabase.query("city", null, "id=?", arrayOfString, null, null, null);
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