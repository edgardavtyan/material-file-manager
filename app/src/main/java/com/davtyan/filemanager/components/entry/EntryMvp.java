package com.davtyan.filemanager.components.entry;

import android.support.v7.widget.RecyclerView;

import com.davtyan.filemanager.data.Storage;

public interface EntryMvp {
    String EXTRA_PATH = "extra_path";

    interface View {
        void updateEntries(Storage[] entries);
        void updateViewSelectionAt(int position);
        void clearSelections();
        void setCurrentPath(String path);
        void close();
    }

    interface Model {
        void updateEntries(String dirPath);
        void toggleEntrySelectedAt(int position);
        void clearSelections();
        void navigateForward(int position);
        void navigateBack();
        Storage[] getEntries();
        String getCurrentPath();
        boolean isAtRoot();
    }

    interface Presenter {
        void onCreate(String path);
        void onEntryClick(int position);
        void onEntryToggleSelected(int position);
        void onNavigateBack();
        void onBindViewHolder(ViewHolder holder, int position);
        int getEntryCount();
        boolean isInSelectMode();
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
        public abstract void setIsSelected(boolean isSelected);
    }
}
