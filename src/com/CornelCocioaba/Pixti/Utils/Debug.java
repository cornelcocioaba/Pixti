package com.CornelCocioaba.Pixti.Utils;

import android.util.Log;

public class Debug {
	public static final String TAG = "Engine";
    public static final boolean ON = true;
    
    public static void log(String msg){
    	if(ON){
    		Log.v(TAG, msg);
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
}