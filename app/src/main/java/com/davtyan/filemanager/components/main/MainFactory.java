package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.utils.StatusBarUtils;

public class MainFactory {
    private final MainActivity activity;

    private MainModel model;
    private MainPresenter presenter;
    private StoragePermissionRequest storagePermissionRequest;
    private EntryAdapter adapter;
    private DeleteConfirmDialog deleteConfirmDialog;
    private StatusBarUtils statusBarUtils;

    public MainFactory(MainActivity activity) {
        this.activity = activity;
    }

    public MainPresenter getPresenter() {
        if (presenter == null)
            presenter = new MainPresenter(activity, getModel(), getStoragePermissionRequest());
        return presenter;
    }

    public MainModel getModel() {
        if (model == null)
            model = new MainModel();
        return model;
    }

    public EntryAdapter getAdapter() {
        if (adapter == null)
            adapter = new EntryAdapter(activity, getPresenter());
        return adapter;
    }

    public DeleteConfirmDialog getDeleteConfirmDialog() {
        if (deleteConfirmDialog == null)
            deleteConfirmDialog = new DeleteConfirmDialog(activity, presenter);
        return deleteConfirmDialog;
    }

    public StoragePermissionRequest getStoragePermissionRequest() {
        if (storagePermissionRequest == null)
            storagePermissionRequest = new StoragePermissionRequest(activity);
        return storagePermissionRequest;
    }

    public StatusBarUtils getStatusBarUtils() {
        if (statusBarUtils == null)
            statusBarUtils = new StatusBarUtils(activity.getWindow());
        return statusBarUtils;
    }
}
