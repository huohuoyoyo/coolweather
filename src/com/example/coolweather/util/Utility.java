package com.example.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.example.coolweather.database.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/7/2.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     * @param coolWeatherDB 数据库管理类
     * @param response 从服务器返回的请求的数据
     * @return
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
        //判断字符串是否为null或者长度是否为0 是则返回true
        if (!TextUtils.isEmpty(response)){
            //public String[] split(String regex, int limit)此函数返回的是一个字符串数组，
            // 用于分割字符串成一个字符串数组 在其中每个出现regex的地方都要分割（这里为“，”）
            // response的值为（代号|城市，代号|城市）
            //（1）regex是可选项。字符串或正则表达式对象，它标识了分隔字符串时使用的是一个还是多个字符。
            // 如果忽略该选项，返回包含整个字符串的单一元素数组。
            //（2）limit也是可选项。该值用来限制返回数组中的元素个数。
            String[] allProvinces = response.split(",");
            if (allProvinces!=null&&allProvinces.length > 0){
                for (String p : allProvinces){
                    //要注意转义字符：“.”和“|”都是转义字符，必须得加"\\"。同理：*和+也是如此的
                    //第二次分割（代号|城市）返回的是一个没有分割符号的字符串数组
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                MyLog.i(MyLog.getTag(),"处理省级请求数据");
                return true;
            }
        }
        return false;
    }
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities!=null&&allCities.length > 0){
                for (String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                    System.out.println("城市代码" + array[0] + "");
                }
                MyLog.i(MyLog.getTag(),"处理市级请求数据");
                return true;
            }
        }
        return false;
    }
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            System.out.println(allCounties[0]);
            if (allCounties != null&&allCounties.length>0){
                for (String c : allCounties){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                MyLog.i(MyLog.getTag(),"处理县级请求数据");
                return true;
            }
        }
        return false;
    }
    public static void handleWeatherResponse(Context context,String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            MyLog.i(MyLog.getTag(), "解析完成");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
                                        String temp2, String weatherDesp, String publishTime) {
        //SimpleDateFormat 是 Java 中一个非常常用的类，该类用来对日期字符串进行解析和格式化输出
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
        MyLog.i(MyLog.getTag(), "保存天气信息到preference");
    }
}
