package com.example.zooseeker;

import android.app.Application;

public class UserSettings extends Application {

    public static final String PREFERENCES = "Preferences";

    public static final String CUSTOM_DIRECTION = "false";



    private int currentIndex;

    private boolean customDirection;

    public boolean getCustomDirection() {
        return customDirection;
    }

    public void setDetailed(boolean customDirection) {
        this.customDirection = customDirection;
    }


}
