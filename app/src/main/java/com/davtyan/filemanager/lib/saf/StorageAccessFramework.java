package com.davtyan.filemanager.lib.saf;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Cleanup;

@TargetApi(21)
public class StorageAccessFramework {
    private static final int REQUEST_MAIN = 1;

    private final Activity activity;
    private final String sdCardName;
    private final String sdCardEnding;
    private final Uri sdCardUri;

    public StorageAccessFramework(Activity activity) {
        this.activity = activity;
        sdCardName = getSdCardName();
        sdCardEnding = sdCardName + "%3A";
        sdCardUri = getSdCardUri();
    }

    public void makeAccessRequest() {
        if (sdCardUri == null) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            activity.startActivityForResult(intent, REQUEST_MAIN);
        }
    }

    public void persistPermissions(int requestCode, Intent intent) {
        if (requestCode == REQUEST_MAIN && intent.getData().toString().endsWith(sdCardEnding)) {
            int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            activity.getContentResolver().takePersistableUriPermission(intent.getData(), flags);
        }
    }

    public boolean deleteFile(String filePath) {
        try {
            return getDocumentFile(filePath).delete();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean renameFile(String filePath, String newName) {
        try {
            return getDocumentFile(filePath).renameTo(newName);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createDirectory(String path, String name) {
        try {
            return getDocumentFile(path).createDirectory(name) != null;
        } catch (Exception e) {
            return false;
        }
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

    @Nullable
    private Uri getSdCardUri() {
        for (UriPermission uriPermission : activity.getContentResolver().getPersistedUriPermissions()) {
            if (uriPermission.getUri().toString().endsWith(sdCardEnding)) {
                return uriPermission.getUri();
            }
        }

        return null;
    }

    private DocumentFile getDocumentFile(String filePath) {
        if (filePath.endsWith(sdCardName)) {
            return getRootFile();
        }

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
        return DocumentFile.fromTreeUri(activity, sdCardUri);
    }

    public void copyFile(String dstDir, String srcFullPath, String dstFileName) throws IOException {
        DocumentFile dstDirDoc = getDocumentFile(dstDir);
        DocumentFile dstFile = dstDirDoc.createFile("", dstFileName);

        @Cleanup OutputStream out = activity.getContentResolver().openOutputStream(dstFile.getUri());
        @Cleanup InputStream in = new FileInputStream(srcFullPath);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
