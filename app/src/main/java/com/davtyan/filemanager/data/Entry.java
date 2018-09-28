package com.davtyan.filemanager.data;

import java.io.File;

import lombok.Getter;

public class Entry {
    private File file;
    private @Getter boolean isSelected;

    public Entry(File file) {
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

    public boolean delete() {
        return file.delete();
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void clearSelection() {
        isSelected = false;
    }
}
