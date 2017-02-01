package com.davtyan.filemanager.components.main;

public interface MainMvp {
    interface Model {
        long getInternalStorageFreeSpace();
        long getInternalStorageTotalSpace();
    }

    interface View {
        void setInternalStorageInfo(long freeSpace, long totalSpace);
    }

    interface Presenter {
        void onCreate();
    }
}
