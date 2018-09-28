package com.davtyan.filemanager.lib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.UriPermission;

@TargetApi(21)
public class StorageAccessFramework {
    private static final int REQUEST_MAIN = 1;

    private final Activity activity;

    public StorageAccessFramework(Activity activity) {
        this.activity = activity;
    }

    public void makeAccessRequest() {
        if (!isPermissionGranted()) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            activity.startActivityForResult(intent, REQUEST_MAIN);
        }
    }

    public void persistPermissions(int requestCode, Intent intent) {
        if (requestCode == REQUEST_MAIN) {
            activity.grantUriPermission(activity.getPackageName(), intent.getData(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            final int takeFlags = intent.getFlags()
                                  & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                     | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.getContentResolver().takePersistableUriPermission(intent.getData(), takeFlags);
        }
    }

    private boolean isPermissionGranted() {
        for (UriPermission uriPermission : activity.getContentResolver().getPersistedUriPermissions()) {
            if (uriPermission.getUri().toString().endsWith("7875-6541%3A")) {
                return true;
            }
        }

        return false;
    }
}
