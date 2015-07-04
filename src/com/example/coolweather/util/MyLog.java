package com.example.coolweather.util;

import android.util.Log;

/**
 * Created by Administrator on 2015/7/2.
 */
public class MyLog {
    public static boolean isLog = true;
    public static String getTag() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return "[" + traceElement.getFileName() + " | " + traceElement.getLineNumber() + " | " + traceElement.getMethodName() + "]";
    }
    public static void i(String tag,String log){
        if (isLog){
            Log.i(tag,log);
        }
    }
}
