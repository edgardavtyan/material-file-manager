package com.davtyan.filemanager.components.main;

import android.os.Environment;

import com.davtyan.filemanager.data.Storage;

import java.io.File;
import java.util.Stack;

import lombok.Getter;

public class MainModel {
    private final Stack<String> entriesStack;

    private @Getter Storage internalStorage;
    private @Getter Storage externalStorage;
    private @Getter Storage[] entries;
    private @Getter String currentPath;
    private @Getter int selectedEntriesCount;
    private boolean hasExternalStorage;

    public MainModel() {
        entriesStack = new Stack<>();
        entries = new Storage[0];
    }

    public void init() {
        internalStorage = new Storage(Environment.getExternalStorageDirectory());

        File sdCardDirectory = getSDCardDirectory();
        if (sdCardDirectory == null) {
            externalStorage = null;
            hasExternalStorage = false;
        } else {
            externalStorage = new Storage(getSDCardDirectory());
            hasExternalStorage = true;
        }
    }

    public boolean hasExternalStorage() {
        return hasExternalStorage;
    }

    public void updateEntries(String dirPath) {
        String[] filenames = new File(dirPath).list();
        Storage[] entries = new Storage[filenames.length];
        File dirFile = new File(dirPath);
        for (int i = 0; i < filenames.length; i++) {
            entries[i] = new Storage(new File(dirFile, filenames[i]));
        }

        this.entries = entries;
        this.currentPath = dirPath;
    }

    public void toggleEntrySelectedAt(int position) {
        Storage entry = entries[position];
        if (entry.isSelected()) {
            entry.setSelected(false);
            selectedEntriesCount--;
        } else {
            entry.setSelected(true);
            selectedEntriesCount++;
        }
    }

    public void clearSelections() {
        for (Storage entry : entries) entry.clearSelection();
        selectedEntriesCount = 0;
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
        for (Storage entry : entries) {
            if (!entry.isSelected()) continue;
            entry.delete();
            updateEntries(currentPath);
        }
    }

    public void navigateToInternalStorage() {
        entriesStack.clear();
        currentPath = internalStorage.getPath();
        updateEntries(internalStorage.getPath());
    }

    public void navigateToExternalStorage() {
        entriesStack.clear();
        currentPath = externalStorage.getPath();
        updateEntries(externalStorage.getPath());
    }

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
}
