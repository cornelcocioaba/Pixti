package com.CornelCocioaba.Pixti.Utils;

import android.util.Log;

public class Debug {
	public static final String TAG = "Pixti";
    public static final boolean ON = true;
    
    public static <T> void log(T msg){
    	if(ON){
    		Log.d(TAG, String.valueOf(msg));
    	}
    }
    
    public static <T> void logInfo(T msg){
    	if(ON){
    		Log.i(TAG, String.valueOf(msg));
    	}
    }
    
    public static <T> void logError(T msg){
    	if(ON){
    		Log.e(TAG, String.valueOf(msg));
    	}
    }
}