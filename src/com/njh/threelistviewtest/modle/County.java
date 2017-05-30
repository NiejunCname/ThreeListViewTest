package com.njh.threelistviewtest.modle;


public class County
{
  private int cityId;
  private String countyName;
  private int id;
  private String weatherId;
  
  public int getCityId()
  {
    return this.cityId;
  }
  
  public String getCountyName()
  {
    return this.countyName;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public String getWeatherId()
  {
    return this.weatherId;
  }
  
  public void setCityId(int paramInt)
  {
    this.cityId = paramInt;
  }
  
  public void setCountyName(String paramString)
  {
    this.countyName = paramString;
  }
  
  public void setId(int paramInt)
  {
    this.id = paramInt;
  }
  
  public void setWeatherId(String paramString)
  {
    this.weatherId = paramString;
  }
}

