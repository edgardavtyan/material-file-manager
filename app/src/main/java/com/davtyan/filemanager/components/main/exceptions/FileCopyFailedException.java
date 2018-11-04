package com.davtyan.filemanager.components.main.exceptions;

import java.io.File;
import java.io.IOException;

public class FileCopyFailedException extends IOException {
    public FileCopyFailedException(File srcFile, File dstFile) {
        super(String.format("Could not copy [%s] to [%s]", srcFile.getPath(), dstFile.getPath()));
    }
}
