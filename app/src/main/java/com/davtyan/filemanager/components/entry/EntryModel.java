package com.davtyan.filemanager.components.entry;

import com.davtyan.filemanager.data.Storage;

import java.io.File;
import java.util.Stack;

import lombok.Getter;

public class EntryModel {
    private final Stack<String> entriesStack;
    private @Getter Storage[] entries;
    private @Getter int selectedEntriesCount;
    private @Getter String currentPath;

    public EntryModel() {
        entriesStack = new Stack<>();
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
}
