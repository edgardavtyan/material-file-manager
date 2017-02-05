package com.davtyan.filemanager.components.main;

import java.io.File;

public interface MainMvp {
    interface Model {
        long getInternalStorageFreeSpace();
        long getInternalStorageTotalSpace();
        boolean hasSDCardStorage();
        File getSDCardFile();

        long getSDCardFreeSpace();

        long getSDCardTotalSpace();
    }

    interface View {
        void setInternalStorageInfo(long freeSpace, long totalSpace);
        void addExternalStorage(String name, long freeSpace, long totalSpace);
    }

    interface Presenter {
        void onCreate();
    }
}
