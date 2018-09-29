package com.davtyan.filemanager.lib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriPermission;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;

@TargetApi(21)
public class StorageAccessFramework {
    private static final int REQUEST_MAIN = 1;
    private static final String PREF_SAF_URI = "pref_saf_uri";

    private final Activity activity;
    private final String sdCardName;
    private final String sdCardEnding;
    private final SharedPreferences prefs;

    public StorageAccessFramework(Activity activity) {
        this.activity = activity;
        this.sdCardName = getSdCardName();
        this.sdCardEnding = sdCardName + "%3A";
        this.prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void makeAccessRequest() {
        if (!isPermissionGranted()) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            activity.startActivityForResult(intent, REQUEST_MAIN);
        }
    }

    public void persistPermissions(int requestCode, Intent intent) {
        if (requestCode == REQUEST_MAIN && intent.getData().toString().endsWith(sdCardEnding)) {
            int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            activity.getContentResolver().takePersistableUriPermission(intent.getData(), flags);
            prefs.edit().putString(PREF_SAF_URI, intent.getData().toString()).apply();
        }
    }

    public void deleteFile(String filePath) {
        getDocumentFile(filePath).delete();
    }

    public void renameFile(String filePath, String newName) {
        getDocumentFile(filePath).renameTo(newName);
    }

    @Nullable
    public String getSdCardName() {
        if (activity.getExternalCacheDirs().length < 2) {
            return null;
        }

        String sdCardPath = activity.getExternalCacheDirs()[1].getPath();
        int sdCardNameStart = sdCardPath.indexOf('/', 1) + 1;
        int sdCardNameEnd = sdCardPath.indexOf('/', sdCardNameStart);
        return sdCardPath.substring(sdCardNameStart, sdCardNameEnd);
    }

    private boolean isPermissionGranted() {
        for (UriPermission uriPermission : activity.getContentResolver().getPersistedUriPermissions()) {
            if (uriPermission.getUri().toString().endsWith(sdCardEnding)) {
                return true;
            }
        }

        return false;
    }

    private DocumentFile getDocumentFile(String filePath) {
        String[] fileParts = filePath
                .substring(filePath.indexOf(sdCardName) + sdCardName.length() + 1)
                .split("/");
        DocumentFile resultDocument = getRootFile();

        for (String filePart : fileParts) {
            resultDocument = resultDocument.findFile(filePart);
        }

        return resultDocument;
    }

    private DocumentFile getRootFile() {
        return DocumentFile.fromTreeUri(activity, Uri.parse(getSafUri()));
    }

    @Nullable
    private String getSafUri() {
        return prefs.getString(PREF_SAF_URI, null);
    }
}
