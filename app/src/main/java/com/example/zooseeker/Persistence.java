package com.example.zooseeker;

import android.content.Context;
import android.content.SharedPreferences;

public class Persistence {

    public static final String CURRENT_INDEX = "CURRENT_INDEX";

    public void saveIndex(Context context, int i) {
        SharedPreferences.Editor editor = context.getSharedPreferences(com.example.zooseeker.UserSettings.PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putInt(CURRENT_INDEX, i);
        editor.apply();
    }


    public int loadIndex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(com.example.zooseeker.UserSettings.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CURRENT_INDEX, -1);

    }

}
