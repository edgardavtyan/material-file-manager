package com.davtyan.filemanager.lib;

import android.content.SharedPreferences;

public class PermissionRequestPrefs {
    private final SharedPreferences prefs;
    private final String key;

    public PermissionRequestPrefs(SharedPreferences prefs, String key) {
        this.prefs = prefs;
        this.key = key;
    }

    public boolean hasUserDeniedPermission() {
        return prefs.getBoolean(key, false);
    }

    public void setUserDeniedPermission(boolean isDenied) {
        prefs.edit().putBoolean(key, isDenied).apply();
    }
}
