package com.davtyan.filemanager.data;

import java.io.File;

public class Storage {
    private File file;

    public Storage(File file) {
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public long getFreeSpace() {
        return file.getFreeSpace();
    }

    public long getTotalSpace() {
        return file.getTotalSpace();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }
}
