package com.davtyan.filemanager.components.main;

import android.Manifest;
import android.app.Activity;

import com.davtyan.filemanager.lib.PermissionRequest;

public class StoragePermissionRequest extends PermissionRequest {
    public StoragePermissionRequest(Activity activity) {
        super(activity);
    }

    @Override
    protected String[] getListOfPermissions() {
        return new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }
}
