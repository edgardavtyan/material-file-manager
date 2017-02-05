package com.davtyan.filemanager.components.entry;

import android.support.v7.widget.RecyclerView;

import com.davtyan.filemanager.data.Storage;

public interface EntryMvp {
    String EXTRA_PATH = "extra_path";

    interface View {
        void updateEntries(Storage[] entries);
    }

    interface Model {
        Storage[] getDirEntries(String dirPath);
    }

    interface Presenter {
        void onCreate(String path);
    }

    abstract class Adapter extends RecyclerView.Adapter<ViewHolder> {
        public abstract void updateEntries(Storage[] entries);
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(android.view.View itemView) {
            super(itemView);
        }

        public abstract void setTitle(String title);
        public abstract void setIsDirectory(boolean isDirectory);
    }
}
