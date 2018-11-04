package com.davtyan.filemanager.lib;

import com.davtyan.filemanager.components.main.exceptions.DirectoryCreateFailedException;
import com.davtyan.filemanager.components.main.exceptions.EntryExistsException;
import com.davtyan.filemanager.components.main.exceptions.FileCopyFailedException;
import com.davtyan.filemanager.components.main.exceptions.FileDeleteFailedException;
import com.davtyan.filemanager.components.main.exceptions.FileRenameFailedException;
import com.davtyan.filemanager.lib.saf.StorageAccessFramework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Cleanup;

public class FileManager {
    private final StorageAccessFramework saf;

    public FileManager(StorageAccessFramework saf) {
        this.saf = saf;
    }

    public void deleteFile(String path) throws FileDeleteFailedException {
        File file = new File(path);

        deleteRecursive(file);

        if (file.exists()) {
            saf.deleteFile(path);
        }

        if (file.exists()) {
            throw new FileDeleteFailedException(path);
        }
    }

    public void renameFile(String path, String newName)
    throws FileRenameFailedException, EntryExistsException {
        File file = new File(path);
        File dir = file.getParentFile();
        File newFile = new File(dir, newName);

        if (newFile.exists()) {
            throw new EntryExistsException(newFile.getPath());
        }

        if (file.renameTo(new File(dir, newName))) {
            return;
        } else if (saf.renameFile(path, newName)) {
            return;
        } else {
            throw new FileRenameFailedException(path, newName);
        }
    }

    public void createDirectory(String parentDir, String name)
    throws EntryExistsException, DirectoryCreateFailedException {
        File dir = new File(parentDir, name);
        if (dir.exists()) {
            throw new EntryExistsException(dir.getPath());
        } else if (dir.mkdir()) {
            return;
        } else if (saf.createDirectory(parentDir, name)) {
            return;
        } else {
            throw new DirectoryCreateFailedException(dir.getPath());
        }
    }

    public void copyFile(String dstDir, File srcFile, File dstFile) throws FileCopyFailedException {
        try {
            copy(srcFile, dstFile);

            if (!dstFile.exists()) {
                saf.copyFile(dstDir, srcFile.getPath(), dstFile.getName());
            }

            if (!dstFile.exists()) {
                throw new FileCopyFailedException(srcFile, dstFile);
            }
        } catch (IOException e) {
            throw new FileCopyFailedException(srcFile, dstFile);
        }


    }

    private void copy(File src, File dst) throws IOException {
        @Cleanup InputStream in = new FileInputStream(src);
        @Cleanup OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
