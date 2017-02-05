package com.davtyan.filemanager.components.entry;

import java.io.File;

public class EntryModel implements EntryMvp.Model {
    @Override
    public String[] getDirEntries(String dirPath) {
        return new File(dirPath).list();
    }
}
