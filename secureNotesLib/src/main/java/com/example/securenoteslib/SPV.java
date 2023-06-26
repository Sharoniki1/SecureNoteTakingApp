package com.example.securenoteslib;

import android.content.Context;
import android.content.SharedPreferences;

public class SPV {

    private static final String DB_FILE= "DB_FILE";
    private static final String DB_FILE2 = "DB_FILE2";
    private static SPV mySPV = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;

    private SPV(Context context){
        sharedPreferences = context.getSharedPreferences(DB_FILE, context.MODE_PRIVATE);
        sharedPreferences2 = context.getSharedPreferences(DB_FILE2, context.MODE_PRIVATE);
    }


    public static SPV getIntance() {
        return mySPV;
    }

    public static void init(Context context){
        if(mySPV == null)
            mySPV = new SPV(context);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getString(String key, String def){
        return sharedPreferences.getString(key,def);
    }

    public void putString2(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getString2(String key, String def){
        return sharedPreferences2.getString(key,def);
    }

}
