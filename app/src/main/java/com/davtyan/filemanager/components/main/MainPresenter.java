package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.data.Storage;

import lombok.Getter;

public class MainPresenter {
    private final MainActivity view;
    private final MainModel model;

    private @Getter boolean isInSelectMode;

    public MainPresenter(MainActivity view, MainModel model) {
        this.view = view;
        this.model = model;
        isInSelectMode = false;
    }

    public void onCreate() {
        model.updateEntries(model.getInternalStorage().getPath());
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentPath());

        if (model.hasExternalStorage()) {
            view.setExternalStorage(model.getExternalStorage().getName());
        }
    }

    public void onEntryClick(int position) {
        model.navigateForward(position);
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentPath());
    }

    public void onEntryToggleSelected(int position) {
        model.toggleEntrySelectedAt(position);
        view.updateViewSelectionAt(position);
        view.setSelectedEntriesCount(model.getSelectedEntriesCount());

        if (model.getSelectedEntriesCount() == 0) {
            view.exitSelectMode();
            isInSelectMode = false;
        } else {
            view.enterSelectMode();
            isInSelectMode = true;
        }
    }

    public void onNavigateBack() {
        if (isInSelectMode) {
            isInSelectMode = false;
            model.clearSelections();
            view.exitSelectMode();
        } else {
            if (model.isAtRoot()) {
                view.close();
            } else {
                model.navigateBack();
                view.updateEntries(model.getEntries());
                view.setCurrentPath(model.getCurrentPath());
            }
        }
    }

    public void onBindViewHolder(EntryViewHolder holder, int position) {
        Storage entry = model.getEntries()[position];
        holder.setTitle(entry.getName());
        holder.setIsDirectory(entry.isDirectory());
        holder.setIsSelected(entry.isSelected());
    }

    public int getEntryCount() {
        return model.getEntries().length;
    }

    public void onDeleteMenuItemClicked() {
        view.showDeleteConfirmDialog();
    }

    public void onDeleteConfirmDialogPositiveButtonClicked() {
        model.deleteSelectedItems();
        model.clearSelections();
        view.updateEntries(model.getEntries());
        view.exitSelectMode();
        isInSelectMode = false;
    }

    public void onInternalStorageClicked() {
        model.navigateToInternalStorage();
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentPath());
        view.closeDrawer();
    }

    public void onExternalStorageClicked() {
        model.navigateToExternalStorage();
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentPath());
        view.closeDrawer();
    }
}
