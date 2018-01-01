package com.davtyan.filemanager.components.main;

import android.content.SharedPreferences;

import com.davtyan.filemanager.lib.PermissionRequestPrefs;

public class StoragePermissionPrefs extends PermissionRequestPrefs {
    public StoragePermissionPrefs(SharedPreferences prefs) {
        super(prefs, "permission_storage_isDenied");
    }
}
