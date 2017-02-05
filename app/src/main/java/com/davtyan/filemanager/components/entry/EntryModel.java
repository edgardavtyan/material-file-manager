package com.davtyan.filemanager.components.entry;

import com.davtyan.filemanager.data.Storage;

import java.io.File;

import lombok.Getter;

public class EntryModel implements EntryMvp.Model {
    private @Getter Storage[] entries;

    @Override
    public void updateEntries(String dirPath) {
        String[] filenames = new File(dirPath).list();
        Storage[] entries = new Storage[filenames.length];
        for (int i = 0; i < filenames.length; i++) {
            entries[i] = new Storage(new File(new File(dirPath), filenames[i]));
        }

        this.entries = entries;
    }

    @Override
    public Storage getEntryAt(int position) {
        return entries[position];
    }
}
