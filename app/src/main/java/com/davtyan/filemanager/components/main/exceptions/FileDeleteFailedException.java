package com.davtyan.filemanager.components.main.exceptions;

import java.io.IOException;

public class FileDeleteFailedException extends IOException {
    public FileDeleteFailedException(String filePath) {
        super(String.format("Could not delete file [%s]", filePath));
    }
}
