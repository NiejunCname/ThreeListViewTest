package com.njh.threelistviewtest.modle;

public class City
{
  private int cityCode;
  private String cityName;
  private int id;
  private int provinceId;
  
  public int getCityCode()
  {
    return this.cityCode;
  }
  
  public String getCityName()
  {
    return this.cityName;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public int getProvinceId()
  {
    return this.provinceId;
  }
  
  public void setCityCode(int paramInt)
  {
    this.cityCode = paramInt;
  }
  
  public void setCityName(String paramString)
  {
    this.cityName = paramString;
  }
  
  public void setId(int paramInt)
  {
    this.id = paramInt;
  }
  
  public void setProvinceId(int paramInt)
  {
    this.provinceId = paramInt;
  }
}