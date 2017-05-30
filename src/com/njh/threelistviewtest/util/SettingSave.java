package com.njh.threelistviewtest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SettingSave
{
  public static Bundle getSelect(Context paramContext)
  {
    SharedPreferences localSharedPreferences = paramContext.getSharedPreferences("data", 0);
    Bundle localBundle = new Bundle();
    localBundle.putInt("selectLevel", localSharedPreferences.getInt("selectLevel", -1));
    localBundle.putInt("provinceId", localSharedPreferences.getInt("provinceId", -1));
    localBundle.putInt("cityId", localSharedPreferences.getInt("cityId", -1));
    localBundle.putInt("id", localSharedPreferences.getInt("id", -1));
    return localBundle;
  }

  public static boolean saveSelectCity(Context paramContext, int cityId)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("data", 0).edit();
    localEditor.putInt("cityId", cityId);
    localEditor.commit();
    return true;
  }

  public static boolean saveSelectCounty(Context paramContext, int paramInt)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("data", 0).edit();
    localEditor.putInt("id", paramInt);
    localEditor.commit();
    return true;
  }

  public static boolean saveSelectLevel(Context paramContext, int paramInt)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("data", 0).edit();
    localEditor.putInt("selectLevel", paramInt);
    localEditor.commit();
    return true;
  }

  public static boolean saveSelectProvince(Context paramContext, int paramInt)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("data", 0).edit();
    localEditor.putInt("provinceId", paramInt);
    localEditor.commit();
    return true;
  }
}