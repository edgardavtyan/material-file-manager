package com.davtyan.filemanager.components.main.exceptions;

import java.io.IOException;

public class FileOperationFailedException extends IOException {
    public FileOperationFailedException(String operation, Object... args) {
        super(String.format("The following file operation has failed: " + operation, args));
    }
}
