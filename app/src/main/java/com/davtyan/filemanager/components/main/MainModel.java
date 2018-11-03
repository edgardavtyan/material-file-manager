package com.davtyan.filemanager.components.main;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.davtyan.filemanager.components.main.exceptions.EntryExistsException;
import com.davtyan.filemanager.data.Entry;
import com.davtyan.filemanager.lib.StorageAccessFramework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import lombok.Cleanup;
import lombok.Getter;

public class MainModel {
    private final Stack<String> entriesStack;
    private final StorageAccessFramework saf;

    private @Getter Entry internalRoot;
    private @Getter Entry externalRoot;
    private @Getter Entry[] entries;
    private @Getter List<Entry> selectedEntries;
    private @Getter List<Entry> copyEntries;
    private @Getter String currentPath;
    private boolean hasExternalStorage;
    private boolean isCutting;

    public MainModel(StorageAccessFramework saf) {
        this.saf = saf;
        entriesStack = new Stack<>();
        entries = new Entry[0];
        selectedEntries = new ArrayList<>();
        copyEntries = new ArrayList<>();
    }

    public void init() {
        internalRoot = new Entry(Environment.getExternalStorageDirectory());

        File sdCardDirectory = getSDCardDirectory();
        if (sdCardDirectory == null) {
            externalRoot = null;
            hasExternalStorage = false;
        } else {
            externalRoot = new Entry(sdCardDirectory);
            hasExternalStorage = true;
        }
    }

    public boolean hasExternalStorage() {
        return hasExternalStorage;
    }

    public void updateEntries(String dirPath) {
        String[] filenames = new File(dirPath).list();
        Entry[] entries = new Entry[filenames.length];
        File dirFile = new File(dirPath);
        for (int i = 0; i < filenames.length; i++) {
            entries[i] = new Entry(new File(dirFile, filenames[i]));
        }

        this.entries = entries;
        this.currentPath = dirPath;
    }

    public void toggleEntrySelectedAt(int position) {
        Entry entry = entries[position];
        if (selectedEntries.contains(entry)) {
            selectedEntries.remove(entry);
        } else {
            selectedEntries.add(entry);
        }
    }

    public void clearSelections() {
        selectedEntries.clear();
    }

    public boolean isEntrySelected(Entry entry) {
        return selectedEntries.contains(entry);
    }

    public void navigateForward(int position) {
        entriesStack.push(currentPath);
        updateEntries(entries[position].getPath());
    }

    public void navigateBack() {
        updateEntries(entriesStack.pop());
    }

    public boolean isAtRoot() {
        return entriesStack.size() == 0;
    }

    public void deleteSelectedItems() {
        for (Entry entry : selectedEntries) {
            if (!entry.delete())
                saf.deleteFile(entry.getPath());
        }

        updateEntries(currentPath);
    }

    public void renameEntry(int position, String newName) throws EntryExistsException {
        if (entries[position].exists()) {
            throw new EntryExistsException(newName);
        }

        if (!entries[position].renameTo(newName)) {
            saf.renameFile(entries[position].getPath(), newName);
        }

        updateEntries(currentPath);
    }

    public void createNewFolder(String folderName) throws EntryExistsException {
        File folder = new File(currentPath, folderName);

        if (folder.exists()) {
            throw new EntryExistsException(folder.getAbsolutePath());
        }

        if (!folder.mkdir()) {
            saf.mkdir(currentPath, folderName);
        }

        updateEntries(currentPath);
    }

    public void navigateToInternalStorage() {
        entriesStack.clear();
        currentPath = internalRoot.getPath();
        updateEntries(internalRoot.getPath());
    }

    public void navigateToExternalStorage() {
        entriesStack.clear();
        currentPath = externalRoot.getPath();
        updateEntries(externalRoot.getPath());
    }

    public void beginCopy() {
        copyEntries.clear();
        copyEntries.addAll(selectedEntries);
        selectedEntries.clear();
        isCutting = false;
    }

    public void beginCut() {
        beginCopy();
        isCutting = true;
    }

    public void paste() {
        File currentDir = new File(currentPath);

        for (Entry copyEntry : copyEntries) {
            File srcFile = new File(copyEntry.getPath());
            File dstFile = new File(currentDir, copyEntry.getName());

            copy(srcFile, dstFile);
            if (!dstFile.exists()) {
                saf.copyFile(currentPath, srcFile.getPath(), dstFile.getName());
            }


            if (isCutting) {
                if (!srcFile.delete()) {
                    saf.deleteFile(srcFile.getPath());
                }
            }
        }

        updateEntries(currentPath);
    }

    @Nullable
    private File getSDCardDirectory() {
        File storage = new File("/storage");

        if (!storage.exists()) {
            return null;
        }

        for (String dir : storage.list()) {
            File file = new File(storage, dir);
            if (file.list() != null) {
                return file;
            }
        }

        return null;
    }

    private void copy(File src, File dst) {
        try {
            @Cleanup InputStream in = new FileInputStream(src);
            @Cleanup OutputStream out = new FileOutputStream(dst);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException ignored) {
        }
    }
}
