package com.davtyan.filemanager.components.main.exceptions;

import java.io.IOException;

public class DirectoryCreateFailedException extends IOException {
    public DirectoryCreateFailedException(String dir) {
        super(String.format("Failed to create directory [%s]", dir));
    }
}
