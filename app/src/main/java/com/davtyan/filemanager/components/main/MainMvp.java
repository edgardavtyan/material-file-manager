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
    }

    interface Presenter {
        void onCreate();
    }
}
