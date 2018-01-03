package com.davtyan.filemanager.lib;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import lombok.Setter;

public abstract class PermissionRequest {
    private final Activity activity;
    private final PermissionRequestPrefs prefs;
    private final String[] listOfPermissions;

    private @Setter OnDeniedListener onDeniedListener;
    private @Setter OnGrantedListener onGrantedListener;

    public interface OnDeniedListener {
        void onStoragePermissionDenied(boolean isNeverAskAgainSelected);
    }

    public interface OnGrantedListener {
        void onStoragePermissionGranted();
    }

    public PermissionRequest(Activity activity, PermissionRequestPrefs prefs) {
        this.activity = activity;
        this.prefs = prefs;

        listOfPermissions = getListOfPermissions();
    }

    protected abstract String[] getListOfPermissions();

    public void request() {
        requestFirstPermission();
    }

    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode != 0) {
            return;
        }

        if (requestCode == 1) {
            return;
        }

        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            onDeniedListener.onStoragePermissionDenied(isNeverAskAgainChecked());
            prefs.setUserDeniedPermission(true);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onGrantedListener.onStoragePermissionGranted();
            prefs.setUserDeniedPermission(false);
            requestSecondPermission();
            return;
        }
    }

    public boolean isGranted() {
        return ActivityCompat.checkSelfPermission(activity, listOfPermissions[0])
               == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isNeverAskAgainChecked() {
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, listOfPermissions[0]);
    }

    private void requestFirstPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{listOfPermissions[0]}, 0);
    }

    private void requestSecondPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{listOfPermissions[1]}, 1);
    }
}
