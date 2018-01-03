package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.data.Storage;
import com.davtyan.filemanager.lib.PermissionRequest;

public class MainPresenter {
    private final MainActivity view;
    private final MainModel model;
    private final StoragePermissionRequest storagePermissionRequest;

    private boolean isInSelectMode;
    private boolean storagePermissionDeniedBeforeExit;

    @SuppressWarnings("FieldCanBeLocal")
    private final PermissionRequest.OnDeniedListener onDeniedListener
            = new PermissionRequest.OnDeniedListener() {
        @Override
        public void onDenied(boolean isNeverAskAgainChecked) {
            if (isNeverAskAgainChecked) {
                view.showStoragePermissionNeverAskAgainError();
            } else {
                view.showStoragePermissionDeniedError();
            }
        }
    };

    @SuppressWarnings({"FieldCanBeLocal", "Convert2Lambda"})
    private final PermissionRequest.OnGrantedListener onGrantedListener
            = new PermissionRequest.OnGrantedListener() {
        @Override
        public void onGranted() {
            initViewAndModel();
        }
    };

    public MainPresenter(
            MainActivity view,
            MainModel model,
            StoragePermissionRequest storagePermissionRequest) {
        this.view = view;
        this.model = model;
        this.storagePermissionRequest = storagePermissionRequest;
        this.storagePermissionRequest.setOnDeniedListener(onDeniedListener);
        this.storagePermissionRequest.setOnGrantedListener(onGrantedListener);
        isInSelectMode = false;
    }

    public void onCreate() {
        if (storagePermissionRequest.isGranted()) {
            storagePermissionDeniedBeforeExit = false;
            initViewAndModel();
        } else {
            //TODO: HACK, fixes animation glitch on startup, find proper solution
            view.waitForAnimation();
            storagePermissionRequest.request();
            storagePermissionDeniedBeforeExit = true;
        }
    }

    public void onCheckPermission() {
        if (storagePermissionRequest.isGranted() && storagePermissionDeniedBeforeExit) {
            initViewAndModel();
        }
    }

    public void onEntryClick(int position) {
        if (isInSelectMode) {
            onEntryToggleSelected(position);
        } else {
            model.navigateForward(position);
            view.updateEntries(model.getEntries());
            view.setCurrentPath(model.getCurrentPath());
        }
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
