package com.davtyan.filemanager.data;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

public class Storage {
    private File file;
    private @Setter @Getter boolean isSelected;

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
