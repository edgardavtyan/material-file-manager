package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.components.main.partials.DrawerPartial;
import com.davtyan.filemanager.components.main.partials.EmptyDirectoryPartial;
import com.davtyan.filemanager.components.main.partials.ListPartial;
import com.davtyan.filemanager.components.main.partials.PermissionsPartial;
import com.davtyan.filemanager.components.main.partials.ToolbarPartial;
import com.davtyan.filemanager.lib.StorageAccessFramework;
import com.davtyan.filemanager.utils.StatusBarUtils;

public class MainFactory {
    private final MainActivity activity;

    private MainModel model;
    private MainPresenter presenter;
    private StoragePermissionRequest storagePermissionRequest;
    private EntryAdapter adapter;
    private DeleteConfirmDialog deleteConfirmDialog;
    private RenameDialog renameDialog;
    private NewFolderDialog newFolderDialog;
    private StatusBarUtils statusBarUtils;
    private DrawerPartial drawerPartial;
    private EmptyDirectoryPartial emptyDirectoryPartial;
    private ListPartial listPartial;
    private PermissionsPartial permissionsPartial;
    private ToolbarPartial toolbarPartial;
    private StorageAccessFramework storageAccessFramework;

    public MainFactory(MainActivity activity) {
        this.activity = activity;
    }

    public void inject() {
        activity.saf = getStorageAccessFramework();
        activity.presenter = getPresenter();
        activity.deleteConfirmDialog = getDeleteConfirmDialog();
        activity.renameDialog = getRenameDialog();
        activity.newFolderDialog = getNewFolderDialog();
        activity.toolbarPartial = getToolbarPartial();
        activity.permissionsPartial = getPermissionsPartial();
        activity.drawerPartial = getDrawerPartial();
        activity.listPartial = getListPartial();
        activity.emptyDirectoryPartial = getEmptyDirectoryPartial();
    }

    private StorageAccessFramework getStorageAccessFramework() {
        if (storageAccessFramework == null) {
            storageAccessFramework = new StorageAccessFramework(activity);
        }
        return storageAccessFramework;
    }

    public MainPresenter getPresenter() {
        if (presenter == null)
            presenter = new MainPresenter(activity, getModel(), getStoragePermissionRequest());
        return presenter;
    }

    public MainModel getModel() {
        if (model == null)
            model = new MainModel(storageAccessFramework);
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

    private RenameDialog getRenameDialog() {
        if (renameDialog == null)
            renameDialog = new RenameDialog(activity, presenter);
        return renameDialog;
    }

    private NewFolderDialog getNewFolderDialog() {
        if (newFolderDialog == null)
            newFolderDialog = new NewFolderDialog(activity, presenter);
        return newFolderDialog;
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

    // Partials

    public DrawerPartial getDrawerPartial() {
        if (drawerPartial == null)
            drawerPartial = new DrawerPartial(activity, getPresenter());
        return drawerPartial;
    }

    public EmptyDirectoryPartial getEmptyDirectoryPartial() {
        if (emptyDirectoryPartial == null)
            emptyDirectoryPartial = new EmptyDirectoryPartial(activity);
        return emptyDirectoryPartial;
    }

    public ListPartial getListPartial() {
        if (listPartial == null)
            listPartial = new ListPartial(activity, getAdapter());
        return listPartial;
    }

    public PermissionsPartial getPermissionsPartial() {
        if (permissionsPartial == null)
            permissionsPartial = new PermissionsPartial(
                    activity, getPresenter(), getStoragePermissionRequest());
        return permissionsPartial;
    }

    public ToolbarPartial getToolbarPartial() {
        if (toolbarPartial == null)
            toolbarPartial = new ToolbarPartial(activity, getStatusBarUtils());
        return toolbarPartial;
    }
}
