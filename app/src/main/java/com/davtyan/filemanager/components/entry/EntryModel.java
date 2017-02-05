package com.davtyan.filemanager.components.entry;

import com.davtyan.filemanager.data.Storage;

import java.io.File;

public class EntryModel implements EntryMvp.Model {
    @Override
    public Storage[] getDirEntries(String dirPath) {
        String[] filenames = new File(dirPath).list();
        Storage[] entries = new Storage[filenames.length];
        for (int i = 0; i < filenames.length; i++) {
            entries[i] = new Storage(new File(new File(dirPath), filenames[i]));
        }

        return entries;
    }
}
