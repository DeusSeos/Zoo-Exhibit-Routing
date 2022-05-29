package com.example.zooseeker;

import android.app.Application;

public class UserSettings extends Application {

    public static final String PREFERENCES = "Preferences";

    public static String CUSTOM_DIRECTION;
    public static final String BRIEF_DIRECTION = "briefDirection";
    public static final String DETAILED_DIRECTION = "detailedDirection";

    private String customDirection;

    public String getCustomDirection() {
        return customDirection;
    }

    public void setCustomDirection(String customDirection) {
        this.customDirection = customDirection;
    }


}
