package com.davtyan.filemanager.utils;

import java.io.File;

public class FileInfo {
    public long getFreeSpace(String path) {
        return new File(path).getFreeSpace();
    }

    public long getFreeSpace(File file) {
        return file.getFreeSpace();
    }

    public long getTotalSpace(File file) {
        return file.getTotalSpace();
    }
}
