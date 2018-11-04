package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.ActivityScope;
import com.davtyan.filemanager.components.main.dialogs.DeleteConfirmDialog;
import com.davtyan.filemanager.components.main.dialogs.NewFolderDialog;
import com.davtyan.filemanager.components.main.dialogs.RenameDialog;
import com.davtyan.filemanager.components.main.partials.DrawerPartial;
import com.davtyan.filemanager.components.main.partials.EmptyDirectoryPartial;
import com.davtyan.filemanager.components.main.partials.ListPartial;
import com.davtyan.filemanager.components.main.partials.PermissionsPartial;
import com.davtyan.filemanager.components.main.partials.ToolbarPartial;
import com.davtyan.filemanager.lib.FileManager;
import com.davtyan.filemanager.lib.saf.StorageAccessFramework;
import com.davtyan.filemanager.lib.saf.StoragePermissionRequest;
import com.davtyan.filemanager.utils.StatusBarUtils;

import dagger.Module;
import dagger.Provides;

@Module
@ActivityScope
public class MainDIModule {
    private final MainActivity activity;

    public MainDIModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public MainModel getMainModel(FileManager fm) {
        return new MainModel(fm);
    }

    @Provides
    @ActivityScope
    public MainPresenter getMainPresenter(MainModel model, StoragePermissionRequest spr) {
        return new MainPresenter(activity, model, spr);
    }

    @Provides
    @ActivityScope
    public EntryAdapter getEntryAdapter(MainPresenter presenter) {
        return new EntryAdapter(activity, presenter);
    }

    @Provides
    @ActivityScope
    public DeleteConfirmDialog getDeleteConfirmDialog(MainPresenter presenter) {
        return new DeleteConfirmDialog(activity, presenter);
    }

    @Provides
    @ActivityScope
    public NewFolderDialog getNewFolderDialog(MainPresenter presenter) {
        return new NewFolderDialog(activity, presenter);
    }

    @Provides
    @ActivityScope
    public RenameDialog getRenameDialog(MainPresenter presenter) {
        return new RenameDialog(activity, presenter);
    }

    @Provides
    @ActivityScope
    public StorageAccessFramework getStorageAccessFramework() {
        return new StorageAccessFramework(activity);
    }

    @Provides
    @ActivityScope
    public FileManager getFileManager(StorageAccessFramework saf) {
        return new FileManager(saf);
    }

    @Provides
    @ActivityScope
    public DrawerPartial getDrawerPartial(MainPresenter presenter) {
        return new DrawerPartial(activity, presenter);
    }

    @Provides
    @ActivityScope
    public EmptyDirectoryPartial getEmptyDirectoryPartial() {
        return new EmptyDirectoryPartial(activity);
    }

    @Provides
    @ActivityScope
    public ListPartial getListPartial(EntryAdapter entryAdapter) {
        return new ListPartial(activity, entryAdapter);
    }

    @Provides
    @ActivityScope
    public PermissionsPartial getPermissionsPartial(
            MainPresenter presenter,
            StoragePermissionRequest spr) {
        return new PermissionsPartial(activity, presenter, spr);
    }

    @Provides
    @ActivityScope
    public StoragePermissionRequest getStoragePermissionRequest() {
        return new StoragePermissionRequest(activity);
    }

    @Provides
    @ActivityScope
    public ToolbarPartial getToolbarPartial() {
        return new ToolbarPartial(activity, new StatusBarUtils(activity.getWindow()));
    }
}
