package com.davtyan.filemanager.components.main.exceptions;

public class FolderExistsException extends Exception {
    public FolderExistsException(String folder) {
        super("Folder " + folder + " alread exists");
    }
}
