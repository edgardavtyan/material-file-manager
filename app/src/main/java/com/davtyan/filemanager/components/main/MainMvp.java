package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.data.Storage;

public interface MainMvp {
    interface Model {
        Storage getInternalStorage();
        Storage getExternalStorage();
        boolean hasExternalStorage();
    }

    interface View {
        void setInternalStorage(Storage storage);
        void setExternalStorage(Storage storage);
        void gotoEntryActivity(String path);
    }

    interface Presenter {
        void onCreate();
        void onInternalStorageClick();
        void onExternalStorageClick();
    }
}
