package com.davtyan.filemanager.lib;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.davtyan.filemanager.components.main.StoragePermissionRequest;

import lombok.Setter;

public abstract class PermissionRequest {
    private final Activity activity;
    private final PermissionRequestPrefs prefs;
    private final String[] listOfPermissions;

    private @Setter StoragePermissionRequest.OnStoragePermissionDeniedListener onStoragePermissionDeniedListener;
    private @Setter StoragePermissionRequest.OnStoragePermissionGrantedListener onStoragePermissionGrantedListener;

    public interface OnStoragePermissionDeniedListener {
        void onStoragePermissionDenied(boolean isNeverAskAgainSelected);
    }

    public interface OnStoragePermissionGrantedListener {
        void onStoragePermissionGranted();
    }

    public PermissionRequest(Activity activity, PermissionRequestPrefs prefs) {
        this.activity = activity;
        this.prefs = prefs;

        listOfPermissions = getListOfPermissions();
    }

    protected abstract String[] getListOfPermissions();

    public void request() {
        if (isApi23()) requestFirstPermission();
    }

    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode != 0) {
            return;
        }

        if (requestCode == 1) {
            return;
        }

        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            onStoragePermissionDeniedListener.onStoragePermissionDenied(isNeverAskAgainChecked());
            prefs.setUserDeniedPermission(true);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onStoragePermissionGrantedListener.onStoragePermissionGranted();
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
        if (!isApi23()) return false;
        return !activity.shouldShowRequestPermissionRationale(listOfPermissions[0]);
    }

    private void requestFirstPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{listOfPermissions[0]}, 0);
    }

    private void requestSecondPermission() {
        if (isApi23()) activity.requestPermissions(new String[]{listOfPermissions[1]}, 1);
    }

    private boolean isApi23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
