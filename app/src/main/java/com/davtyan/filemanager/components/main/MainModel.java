package com.davtyan.filemanager.components.main;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.davtyan.filemanager.data.Entry;
import com.davtyan.filemanager.lib.StorageAccessFramework;

import java.io.File;
import java.util.Stack;

import lombok.Getter;

public class MainModel {
    private final Stack<String> entriesStack;
    private final StorageAccessFramework saf;

    private @Getter Entry internalRoot;
    private @Getter Entry externalRoot;
    private @Getter Entry[] entries;
    private @Getter String currentPath;
    private @Getter int selectedEntriesCount;
    private boolean hasExternalStorage;

    public MainModel(StorageAccessFramework saf) {
        this.saf = saf;
        entriesStack = new Stack<>();
        entries = new Entry[0];
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
        if (entry.isSelected()) {
            entry.setSelected(false);
            selectedEntriesCount--;
        } else {
            entry.setSelected(true);
            selectedEntriesCount++;
        }
    }

    public void clearSelections() {
        for (Entry entry : entries) entry.clearSelection();
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
        for (Entry entry : entries) {
            if (!entry.isSelected()) {
                continue;
            }

            if (!entry.delete()) {
                saf.deleteFile(entry.getPath());
            }

            updateEntries(currentPath);
        }
    }

    public void renameEntry(int position, String newName) {
        if (!entries[position].renameTo(newName)) {
            saf.renameFile(entries[position].getPath(), newName);
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
}
