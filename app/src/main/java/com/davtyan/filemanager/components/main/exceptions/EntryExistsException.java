package com.davtyan.filemanager.components.main.exceptions;

public class EntryExistsException extends Exception {
    public EntryExistsException(String folder) {
        super("Folder " + folder + " already exists");
    }
}
