package com.example.threework;

import java.io.FileOutputStream;
import java.util.Iterator;

import model.City;
import model.County;
import model.Province;

import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.widget.Toast;
import dao.CityDao;
import dao.CountyDao;
import dao.ProvinceDao;

public class Main2Activity extends AppCompatActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968604);
    ProvinceDao localProvinceDao = new ProvinceDao(this);
    CityDao localCityDao = new CityDao(this);
    CountyDao localCountyDao = new CountyDao(this);
    XmlSerializer localXmlSerializer;
    FileOutputStream localFileOutputStream;
    while (true)
    {
      try
      {
        localXmlSerializer = Xml.newSerializer();
        localFileOutputStream = openFileOutput("weather_area.xml", 0);
        localXmlSerializer.setOutput(localFileOutputStream, "UTF-8");
        localXmlSerializer.startDocument("UTF-8", Boolean.valueOf(true));
        localXmlSerializer.startTag(null, "provinces");
        Iterator localIterator1 = localProvinceDao.findAll().iterator();
        if (!localIterator1.hasNext())
          break;
        Province localProvince = (Province)localIterator1.next();
        localXmlSerializer.startTag(null, "province");
        localXmlSerializer.attribute(null, "provincename", localProvince.getProvinceName());
        localXmlSerializer.attribute(null, "provincecode", String.valueOf(localProvince.getProvinceCode()));
        Iterator localIterator2 = localCityDao.findByProvince(localProvince.getId()).iterator();
        if (!localIterator2.hasNext())
          break ;
        City localCity = (City)localIterator2.next();
        localXmlSerializer.startTag(null, "city");
        localXmlSerializer.attribute(null, "cityname", localCity.getCityName());
        localXmlSerializer.attribute(null, "citycode", String.valueOf(localCity.getCityCode()));
        Iterator localIterator3 = localCountyDao.findByCity(localCity.getId()).iterator();
        if (localIterator3.hasNext())
        {
          County localCounty = (County)localIterator3.next();
          localXmlSerializer.startTag(null, "county");
          localXmlSerializer.attribute(null, "countyname", localCounty.getCountyName());
          localXmlSerializer.attribute(null, "weatherid", localCounty.getWeatherId());
          localXmlSerializer.endTag(null, "county");
          continue;
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        Toast.makeText(this, "操作失败", 1).show();
        return;
      }
      localXmlSerializer.endTag(null, "city");
      continue;
      label379: localXmlSerializer.endTag(null, "province");
    }
    localXmlSerializer.endTag(null, "provinces");
    localXmlSerializer.endDocument();
    localXmlSerializer.flush();
    localFileOutputStream.close();
    Toast.makeText(this, "操作成功", 0).show();
  }
}