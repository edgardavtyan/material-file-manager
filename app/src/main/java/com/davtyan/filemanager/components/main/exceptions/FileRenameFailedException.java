package com.davtyan.filemanager.components.main.exceptions;

import java.io.IOException;

public class FileRenameFailedException extends IOException {
    public FileRenameFailedException(String filePath, String newName) {
        super(String.format("Could not rename file [%s] to [%s]", filePath, newName));
    }
}
