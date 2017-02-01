package com.davtyan.filemanager.components.main;

import android.content.Context;

import com.davtyan.filemanager.utils.FileInfo;

public class MainModel {

    private final Context context;
    private final FileInfo fileInfo;

    public MainModel(Context context, FileInfo fileInfo) {
        this.context = context;
        this.fileInfo = fileInfo;
    }

    public long getInternalStorageFreeSpace() {
        return fileInfo.getFreeSpace(context.getFilesDir());
    }

    public long getInternalStorageTotalSpace() {
        return fileInfo.getTotalSpace(context.getFilesDir());
    }
}
