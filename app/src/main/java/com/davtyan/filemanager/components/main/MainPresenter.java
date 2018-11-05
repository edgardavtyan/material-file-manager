package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.components.main.exceptions.DirectoryCreateFailedException;
import com.davtyan.filemanager.components.main.exceptions.EntryExistsException;
import com.davtyan.filemanager.components.main.exceptions.FileCopyFailedException;
import com.davtyan.filemanager.components.main.exceptions.FileDeleteFailedException;
import com.davtyan.filemanager.components.main.exceptions.FileRenameFailedException;
import com.davtyan.filemanager.data.Entry;
import com.davtyan.filemanager.lib.saf.StoragePermissionRequest;

public class MainPresenter {
    private final MainActivity view;
    private final MainModel model;
    private final StoragePermissionRequest storagePermissionRequest;

    private boolean isInSelectMode;
    private boolean isInSingleEntryMode;
    private boolean storagePermissionDeniedBeforeExit;
    private int activeEntryPosition;

    public MainPresenter(
            MainActivity view,
            MainModel model,
            StoragePermissionRequest storagePermissionRequest) {
        this.view = view;
        this.model = model;
        this.storagePermissionRequest = storagePermissionRequest;
        this.storagePermissionRequest.setOnDeniedListener(this::onDeniedListener);
        this.storagePermissionRequest.setOnGrantedListener(this::onGrantedListener);
        isInSelectMode = false;
    }

    private void onDeniedListener(boolean isNeverAskAgainChecked) {
        if (isNeverAskAgainChecked) {
            view.showStoragePermissionNeverAskAgainError();
        } else {
            view.showStoragePermissionDeniedError();
        }
    }

    private void onGrantedListener() {
        initViewAndModel();
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
            view.setCurrentPath(model.getCurrentDir());
        }
    }


    public void onEntryToggleSelected(int position) {
        model.toggleEntrySelectedAt(position);
        view.updateViewSelectionAt(position);

        if (model.getSelectedEntries().size() == 0) {
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
                view.setCurrentPath(model.getCurrentDir());
            }
        }
    }

    public void onBindViewHolder(EntryViewHolder holder, int position) {
        Entry entry = model.getEntries()[position];
        holder.setTitle(entry.getName());
        holder.setIsSelected(model.isEntrySelected(entry));

        if (entry.isDirectory()) {
            holder.setAsDirectory();
        } else {
            holder.setAsFile(entry.getExtension());
        }
    }

    public int getEntryCount() {
        return model.getEntries().length;
    }

    public void onDeleteMenuItemClicked() {
        view.showDeleteConfirmDialog();
    }

    public void onDeleteEntryClicked(int position) {
        isInSingleEntryMode = true;
        model.toggleEntrySelectedAt(position);
        onDeleteMenuItemClicked();
    }

    public void onDeleteDialogConfirmed() {
        try {
            model.deleteSelectedItems();
        } catch (FileDeleteFailedException e) {
            view.showDeleteOperationFailed();
        }


        model.clearSelections();
        view.updateEntries(model.getEntries());
        view.exitSelectMode();
        isInSelectMode = false;
    }

    public void onDeleteDialogCanceled() {
        if (isInSingleEntryMode) {
            model.clearSelections();
        }
    }

    public void onRenameMenuItemClicked(int position) {
        activeEntryPosition = position;
        view.showRenameDialog(model.getEntries()[position].getName());
    }

    public void onRenameDialogConfirm(String newName) {
        try {
            model.renameEntry(activeEntryPosition, newName);
            view.updateEntries(model.getEntries());
            view.closeRenameDialog();
        } catch (EntryExistsException e) {
            view.showRenameExistsError();
        } catch (FileRenameFailedException e) {
            view.showRenameFailedError();
            view.closeRenameDialog();
        }
    }

    public void onNewFolderMenuItemClicked() {
        view.showNewFolderDialog();
    }

    public void onNewFolderDialogConfirm(String folderName) {
        try {
            model.createNewFolder(folderName);
            view.updateEntries(model.getEntries());
            view.closeNewFolderDialog();
        } catch (EntryExistsException e) {
            view.showNewFolderExistsError();
        } catch (DirectoryCreateFailedException e) {
            view.showNewFolderFailedError();
            view.closeNewFolderDialog();
        }
    }

    public void onInternalStorageClicked() {
        model.navigateToInternalStorage();
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentDir());
        view.closeDrawer();
    }

    public void onExternalStorageClicked() {
        model.navigateToExternalStorage();
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentDir());
        view.closeDrawer();
    }

    public void onRequestStoragePermissionLinkClicked() {
        storagePermissionRequest.request();
    }

    private void initViewAndModel() {
        model.init();
        model.updateEntries(model.getInternalRoot().getPath());
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentDir());

        view.setInternalStorage();
        if (model.hasExternalStorage()) {
            view.setExternalStorage(model.getExternalRoot().getName());
        }
    }

    public void onEntryCopyClicked(int position) {
        model.toggleEntrySelectedAt(position);
        model.beginCopy();
        view.setPasteMenuEnabled(true);
    }

    public void onEntryCutClicked(int position) {
        model.toggleEntrySelectedAt(position);
        model.beginCut();
        view.setPasteMenuEnabled(true);
    }

    public void onToolbarCopyClicked() {
        model.beginCopy();
        view.exitSelectMode();
        view.setPasteMenuEnabled(true);
        isInSelectMode = false;
    }

    public void onToolbarCutClicked() {
        model.beginCut();
        view.exitSelectMode();
        view.setPasteMenuEnabled(true);
        isInSelectMode = false;
    }

    public void onToolbarPasteClicked() {
        try {
            model.paste();
        } catch (FileCopyFailedException e) {
            view.showCopyFailedError();
        } catch (FileDeleteFailedException e) {
            view.showCutFailedError();
        }

        view.updateEntries(model.getEntries());
        view.setPasteMenuEnabled(false);
    }
}
