package com.example.coolweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/2.
 */
public class CoolWeatherDB {
    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper helper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = helper.getWritableDatabase();
    }
    public synchronized static CoolWeatherDB getInstance(Context context){
        if (coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
            MyLog.i(MyLog.getTag(),"实例化coolWeatherDB");
        }
        return coolWeatherDB;
    }
    public void saveProvince(Province province){
        if (province!=null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province", null, values);
            MyLog.i(MyLog.getTag(), "saveProvinces");
        }
    }

    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<Province>();
        MyLog.i(MyLog.getTag(),"loadProvinces");
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Province province = new Province();
            province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            list.add(province);
        }
        cursor.close();

        return list;
    }
    public void saveCity(City city){
        if (city!=null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City", null, values);
            MyLog.i(MyLog.getTag(), "saveCity");
        }

    }
    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvinceId(provinceId);
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            list.add(city);
        }
        cursor.close();
        MyLog.i(MyLog.getTag(),"LoadCities");
        return list;
    }
    public void saveCounty(County county){
        if (county!=null){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County", null, values);
            MyLog.i(MyLog.getTag(), "saveCounty");
        }
    }
    public List<County> loadCounties(int cityId){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
        while (cursor.moveToNext()){
            County county = new County();
            county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
            county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
            county.setCityId(cityId);
            county.setId(cursor.getInt(cursor.getColumnIndex("id")));
            list.add(county);
        }
        cursor.close();
        MyLog.i(MyLog.getTag(),"loadCounties");
        return list;
    }
}
