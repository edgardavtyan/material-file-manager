package com.davtyan.filemanager.lib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;

@TargetApi(21)
public class StorageAccessFramework {
    private static final int REQUEST_MAIN = 1;
    private static final String SDCARD_NAME = "7875-6541/";
    private static final String URI_BASE_SDCARD = "content://com.android.externalstorage.documents/tree/7875-6541%3A/document/7875-6541%3A";

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
            int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            activity.getContentResolver().takePersistableUriPermission(intent.getData(), flags);
        }
    }

    public void deleteFile(String filePath) {
        String fileSubPath = filePath.substring(filePath.indexOf(SDCARD_NAME) + SDCARD_NAME.length());
        getDocumentFile(fileSubPath).delete();
    }

    private boolean isPermissionGranted() {
        for (UriPermission uriPermission : activity.getContentResolver().getPersistedUriPermissions()) {
            if (uriPermission.getUri().toString().endsWith("7875-6541%3A")) {
                return true;
            }
        }

        return false;
    }

    private DocumentFile getDocumentFile(String filePath) {
        String[] fileParts = filePath.split("/");
        DocumentFile resultDocument = DocumentFile.fromTreeUri(activity, Uri.parse(URI_BASE_SDCARD));

        for (String filePart : fileParts) {
            resultDocument = resultDocument.findFile(filePart);
        }

        return resultDocument;
    }
}
