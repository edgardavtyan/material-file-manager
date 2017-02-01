package com.davtyan.filemanager.test_lib.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestFileSystem {
    public static void saveFileWithSize(File file, int size) throws IOException {
        byte[] bytes = new byte[size];
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(bytes, 0, bytes.length);
        stream.close();
    }
}
