package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public  class WeatherDBHelper extends SQLiteOpenHelper {

	public WeatherDBHelper(Context context) {
		super(context, "weather.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("CREATE TABLE if not exists province ("+"id INTEGER PRIMARY KEY AUTO TNCREMENT,"+"provincename TEXT,"+"provincecode INTEGER)");
		arg0.execSQL("CREATE TABLE if not exists city ("+"id INTEGER PRIMARY KEY AUTO TNCREMENT,"+"cityname TEXT,"+"citycode INTEGER,"+"provinceid INTEGER)");
		arg0.execSQL("CREATE TABLE if not exists county ("+"id INTEGER PRIMARY KEY AUTO TNCREMENT,"+"weatherid TEXT,"+"countyname text,"+"cityid INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
