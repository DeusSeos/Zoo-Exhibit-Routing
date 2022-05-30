package com.example.zooseeker;

import android.app.Application;

public class UserSettings extends Application {

    public static final String PREFERENCES = "Preferences";

    public static final String CUSTOM_DIRECTION = "false";
    public static final String BRIEF_DIRECTION = "briefDirection";
    public static final String DETAILED_DIRECTION = "detailedDirection";

    private boolean customDirection;

    public boolean getCustomDirection() {
        return customDirection;
    }

    public void setCustomDirection(boolean customDirection) {
        this.customDirection = customDirection;
    }


}
