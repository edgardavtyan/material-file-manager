package com.davtyan.filemanager.components.main;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MainFactory {
    private final MainActivity activity;

    private MainModel model;
    private MainPresenter presenter;
    private StoragePermissionRequest storagePermissionRequest;
    private StoragePermissionPrefs storagePermissionPrefs;
    private SharedPreferences prefs;
    private EntryAdapter adapter;
    private DeleteConfirmDialog deleteConfirmDialog;

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
            storagePermissionRequest = new StoragePermissionRequest(activity, getStoragePermissionPrefs());
        return storagePermissionRequest;
    }

    public StoragePermissionPrefs getStoragePermissionPrefs() {
        if (storagePermissionPrefs == null)
            storagePermissionPrefs = new StoragePermissionPrefs(getPrefs());
        return storagePermissionPrefs;
    }

    public SharedPreferences getPrefs() {
        if (prefs == null)
            prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs;
    }
}
