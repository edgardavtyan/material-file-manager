package com.davtyan.filemanager.components.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import lombok.Setter;

public class StoragePermissionRequest {
    private static final int REQUEST_READ = 1;
    private static final int REQUEST_WRITE = 2;

    private static final String[] PERMISSION_READ = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String[] PERMISSION_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final Activity activity;

    private @Setter OnStoragePermissionDeniedListener onStoragePermissionDeniedListener;
    private @Setter OnStoragePermissionGrantedListener onStoragePermissionGrantedListener;

    public interface OnStoragePermissionDeniedListener {
        void onStoragePermissionDenied();
    }

    public interface OnStoragePermissionGrantedListener {
        void onStoragePermissionGranted();
    }

    public StoragePermissionRequest(Activity activity) {
        this.activity = activity;
    }

    public void requestPermissions() {
        if (isApi23()) {
            if (isReadPermissionGranted() && isWritePermissionGranted()) {
                onStoragePermissionGrantedListener.onStoragePermissionGranted();
            } else {
                if (!isReadPermissionGranted())
                    activity.requestPermissions(PERMISSION_READ, REQUEST_READ);
                if (!isWritePermissionGranted())
                    activity.requestPermissions(PERMISSION_WRITE, REQUEST_WRITE);
            }
        }
    }

    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode != REQUEST_READ && requestCode != REQUEST_WRITE) {
            return;
        }

        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            onStoragePermissionDeniedListener.onStoragePermissionDenied();
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onStoragePermissionGrantedListener.onStoragePermissionGranted();
            return;
        }
    }

    private boolean isReadPermissionGranted() {
        //noinspection SimplifiableIfStatement
        if (isApi23()) {
            return activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                   == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private boolean isWritePermissionGranted() {
        //noinspection SimplifiableIfStatement
        if (isApi23()) {
            return activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                   == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private boolean isApi23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
