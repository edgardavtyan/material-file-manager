package com.davtyan.filemanager.components.main;

import android.content.Context;
import android.os.Environment;

import com.davtyan.filemanager.data.Storage;

import java.io.File;

import lombok.Getter;

public class MainModel implements MainMvp.Model {

    private final @Getter Storage internalStorage;
    private final @Getter Storage externalStorage;

    public MainModel(Context context) {
        internalStorage = new Storage(Environment.getExternalStorageDirectory());
        externalStorage = new Storage(getSDCardFile());
    }

    private File getSDCardFile() {
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
    public boolean hasExternalStorage() {
        return getSDCardFile() != null;
    }
}
