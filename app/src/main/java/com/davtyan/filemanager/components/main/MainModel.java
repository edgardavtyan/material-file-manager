package com.davtyan.filemanager.components.main;

import android.content.Context;

import com.davtyan.filemanager.utils.FileInfo;

import java.io.File;

public class MainModel implements MainMvp.Model {

    private final Context context;
    private final FileInfo fileInfo;

    public MainModel(Context context, FileInfo fileInfo) {
        this.context = context;
        this.fileInfo = fileInfo;
    }

    @Override
    public long getInternalStorageFreeSpace() {
        return fileInfo.getFreeSpace(context.getFilesDir());
    }

    @Override
    public long getInternalStorageTotalSpace() {
        return fileInfo.getTotalSpace(context.getFilesDir());
    }

    @Override
    public boolean hasSDCardStorage() {
        return fileInfo.hasExternalStorage(context);
    }

    @Override
    public File getSDCardFile() {
        File storage = new File("/storage");
        for (String dir : storage.list()) {
            File file = new File(storage, dir);
            if (file.list() != null) {
                return file;
            }
        }

        return null;
    }

    @Override
    public long getSDCardFreeSpace() {
        return fileInfo.getFreeSpace(getSDCardFile());
    }

    @Override
    public long getSDCardTotalSpace() {
        return fileInfo.getTotalSpace(getSDCardFile());
    }
}
