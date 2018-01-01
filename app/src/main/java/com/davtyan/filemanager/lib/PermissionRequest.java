package com.davtyan.filemanager.lib;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.davtyan.filemanager.components.main.StoragePermissionRequest;

import lombok.Getter;
import lombok.Setter;

public abstract class PermissionRequest {
    private final Activity activity;
    private final PermissionRequestPrefs prefs;
    private final String[] listOfPermissions;

    private @Getter PermissionState state;

    private @Setter StoragePermissionRequest.OnStoragePermissionDeniedListener onStoragePermissionDeniedListener;
    private @Setter StoragePermissionRequest.OnStoragePermissionGrantedListener onStoragePermissionGrantedListener;

    public interface OnStoragePermissionDeniedListener {
        void onStoragePermissionDenied();
    }

    public interface OnStoragePermissionGrantedListener {
        void onStoragePermissionGranted();
    }

    public PermissionRequest(Activity activity, PermissionRequestPrefs prefs) {
        this.activity = activity;
        this.prefs = prefs;

        listOfPermissions = getListOfPermissions();

        if (isGranted()) {
            state = PermissionState.GRANTED;
        } else if (isExplicitlyDeniedByUser()) {
            state = PermissionState.DENIED;
        } else if (isNeverAskAgainSelected()) {
            state = PermissionState.NEVER_ASK_AGAIN;
        } else {
            state = PermissionState.NOT_YET_ASKED;
        }
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
            onStoragePermissionDeniedListener.onStoragePermissionDenied();
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

    private boolean isExplicitlyDeniedByUser() {
        return prefs.hasUserDeniedPermission();
    }

    private boolean isGranted() {
        if (!isApi23()) return true;
        return activity.checkSelfPermission(listOfPermissions[0]) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isNeverAskAgainSelected() {
        if (!isApi23()) return false;
        return activity.shouldShowRequestPermissionRationale(listOfPermissions[0]);
    }

    private void requestFirstPermission() {
        if (isApi23()) activity.requestPermissions(new String[]{listOfPermissions[0]}, 0);
    }

    private void requestSecondPermission() {
        if (isApi23()) activity.requestPermissions(new String[]{listOfPermissions[1]}, 1);
    }

    private boolean isApi23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
