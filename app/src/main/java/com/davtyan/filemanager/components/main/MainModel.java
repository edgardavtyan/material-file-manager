package com.davtyan.filemanager.components.main;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.davtyan.filemanager.components.main.exceptions.EntryExistsException;
import com.davtyan.filemanager.components.main.exceptions.FileCopyFailedException;
import com.davtyan.filemanager.components.main.exceptions.FileDeleteFailedException;
import com.davtyan.filemanager.components.main.exceptions.FileOperationFailedException;
import com.davtyan.filemanager.components.main.exceptions.FileRenameFailedException;
import com.davtyan.filemanager.data.Entry;
import com.davtyan.filemanager.lib.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import lombok.Getter;

public class MainModel {
    private final Stack<String> dirsStack;
    private final FileManager fm;

    private @Getter Entry internalRoot;
    private @Getter Entry externalRoot;
    private @Getter Entry[] entries;
    private @Getter List<Entry> selectedEntries;
    private @Getter String currentDir;
    private List<Entry> copyEntries;
    private boolean hasExternalStorage;
    private boolean isCutting;

    public MainModel(FileManager fm) {
        this.fm = fm;
        dirsStack = new Stack<>();
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
        this.currentDir = dirPath;
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
        dirsStack.push(currentDir);
        updateEntries(entries[position].getPath());
    }

    public void navigateBack() {
        updateEntries(dirsStack.pop());
    }

    public boolean isAtRoot() {
        return dirsStack.size() == 0;
    }

    public void deleteSelectedItems() throws FileDeleteFailedException {
        try {
            for (Entry entry : selectedEntries) {
                fm.deleteFile(entry.getPath());
            }
        } finally {
            updateEntries(currentDir);
        }
    }

    public void renameEntry(int position, String newName)
    throws EntryExistsException, FileRenameFailedException {
        try {
            fm.renameFile(entries[position].getPath(), newName);
        } finally {
            updateEntries(currentDir);
        }
    }

    public void createNewFolder(String folderName)
    throws EntryExistsException, FileOperationFailedException {
        try {
            fm.createDirectory(currentDir, folderName);
        } finally {
            updateEntries(currentDir);
        }
    }

    public void navigateToInternalStorage() {
        dirsStack.clear();
        currentDir = internalRoot.getPath();
        updateEntries(internalRoot.getPath());
    }

    public void navigateToExternalStorage() {
        dirsStack.clear();
        currentDir = externalRoot.getPath();
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

    public void paste() throws FileCopyFailedException, FileDeleteFailedException {
        try {
            for (Entry copyEntry : copyEntries) {
                File srcFile = new File(copyEntry.getPath());
                File dstFile = new File(currentDir, copyEntry.getName());
                fm.copyFile(currentDir, srcFile, dstFile);

                if (isCutting) {
                    fm.deleteFile(srcFile.getPath());
                }
            }
        } finally {
            updateEntries(currentDir);
        }

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
