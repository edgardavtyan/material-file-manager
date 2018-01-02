package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.data.Storage;
import com.davtyan.filemanager.lib.PermissionState;

import lombok.Getter;

public class MainPresenter
        implements StoragePermissionRequest.OnStoragePermissionDeniedListener,
                   StoragePermissionRequest.OnStoragePermissionGrantedListener {

    private final MainActivity view;
    private final MainModel model;
    private final StoragePermissionRequest storagePermissionRequest;

    private @Getter boolean isInSelectMode;

    public MainPresenter(
            MainActivity view,
            MainModel model,
            StoragePermissionRequest storagePermissionRequest) {
        this.view = view;
        this.model = model;
        this.storagePermissionRequest = storagePermissionRequest;
        this.storagePermissionRequest.setOnStoragePermissionDeniedListener(this);
        this.storagePermissionRequest.setOnStoragePermissionGrantedListener(this);
        isInSelectMode = false;
    }

    public void onCreate() {
        switch (storagePermissionRequest.getState()) {
            case NOT_YET_ASKED:
                storagePermissionRequest.request();
                break;
            case GRANTED:
                initViewAndModel();
                break;
            case DENIED:
                view.showStoragePermissionDeniedError();
                break;
            case NEVER_ASK_AGAIN:
                view.showStoragePermissionError();
                break;
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

    public void onRequestStoragePermissionLinkClicked() {
        storagePermissionRequest.request();
    }

    @Override
    public void onStoragePermissionGranted() {
        onCreate();
    }

    @Override
    public void onStoragePermissionDenied() {
        PermissionState state = storagePermissionRequest.getState();
        if (state == PermissionState.NEVER_ASK_AGAIN) {
            view.showStoragePermissionError();
        } else {
            view.showStoragePermissionDeniedError();
        }
    }

    private void initViewAndModel() {
        model.init();
        model.updateEntries(model.getInternalStorage().getPath());
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentPath());

        view.setInternalStorage();
        if (model.hasExternalStorage()) {
            view.setExternalStorage(model.getExternalStorage().getName());
        }
    }
}
