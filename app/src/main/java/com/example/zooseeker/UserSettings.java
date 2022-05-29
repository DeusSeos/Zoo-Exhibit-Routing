package com.example.zooseeker;

import android.app.Application;

public class UserSettings extends Application {

    public static final String PREFERENCES = "Preferences";

    public static final String Custom_Direction = "customDirection";
    public static final String BRIEF_DIRECTION = "briefDirection";
    public static final String DETAILED_DIRECTION = "detailedDirection";

    private String customTheme;

    public String getCustomTheme() {
        return customTheme;
    }

    public void setCustomTheme(String customTheme) {
        this.customTheme = customTheme;
    }


}
