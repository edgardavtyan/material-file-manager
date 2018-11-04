package com.davtyan.filemanager.data;

import java.io.File;

public class Entry {
    private final File file;

    public Entry(File file) {
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public String getExtension() {
        return file.getName().substring(file.getName().lastIndexOf('.') + 1);
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }
}
