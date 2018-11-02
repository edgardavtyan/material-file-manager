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

    public String getExtension() {
        return file.getName().substring(file.getName().lastIndexOf('.') + 1);
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
        deleteRecursive(file);
        return !file.exists();
    }

    public boolean renameTo(String newName) {
        File directory = file.getParentFile();
        return file.renameTo(new File(directory, newName));
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void clearSelection() {
        isSelected = false;
    }

    public boolean exists() {
        return file.exists();
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
