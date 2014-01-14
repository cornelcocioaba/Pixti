package com.CornelCocioaba.Pixti.Utils;

import android.util.Log;

public class Debug {
	public static final String TAG = "Engine";
    public static final boolean ON = true;
    
    public static void log(String msg){
    	if(ON){
    		Log.d(TAG, msg);
    	}
    }
    
    public static void log(short msg){
    	log(String.valueOf(msg));
    }
    
    public static void log(int msg){
    	log(String.valueOf(msg));
    }
    
    public static void log(float msg){
    	log(String.valueOf(msg));
    }
    
    public static void log(double msg){
    	log(String.valueOf(msg));
    }
    
    public static void logInfo(String msg){
    	if(ON){
    		Log.i(TAG, msg);
    	}
    }
    
    public static void logInfo(short msg){
    	logInfo(String.valueOf(msg));
    }
    
    public static void logInfo(int msg){
    	logInfo(String.valueOf(msg));
    }
    
    public static void logInfo(float msg){
    	logInfo(String.valueOf(msg));
    }
    
    public static void logInfo(double msg){
    	logInfo(String.valueOf(msg));
    }
}