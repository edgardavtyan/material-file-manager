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
import com.davtyan.filemanager.lib.StorageAccessFramework;
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
    public MainModel provideMainModel(StorageAccessFramework saf) {
        return new MainModel(saf);
    }

    @Provides
    @ActivityScope
    public MainPresenter provideMainPresenter(MainModel model, StoragePermissionRequest spr) {
        return new MainPresenter(activity, model, spr);
    }

    @Provides
    @ActivityScope
    public EntryAdapter provideEntryAdapter(MainPresenter presenter) {
        return new EntryAdapter(activity, presenter);
    }

    @Provides
    @ActivityScope
    public DeleteConfirmDialog provideDeleteConfirmDialog(MainPresenter presenter) {
        return new DeleteConfirmDialog(activity, presenter);
    }

    @Provides
    @ActivityScope
    public NewFolderDialog provideNewFolderDialog(MainPresenter presenter) {
        return new NewFolderDialog(activity, presenter);
    }

    @Provides
    @ActivityScope
    public RenameDialog provideRenameDialog(MainPresenter presenter) {
        return new RenameDialog(activity, presenter);
    }

    @Provides
    @ActivityScope
    public StorageAccessFramework provideStorageAccessFramework() {
        return new StorageAccessFramework(activity);
    }

    @Provides
    @ActivityScope
    public DrawerPartial provideDrawerPartial(MainPresenter presenter) {
        return new DrawerPartial(activity, presenter);
    }

    @Provides
    @ActivityScope
    public EmptyDirectoryPartial provideEmptyDirectoryPartial() {
        return new EmptyDirectoryPartial(activity);
    }

    @Provides
    @ActivityScope
    public ListPartial provideListPartial(EntryAdapter entryAdapter) {
        return new ListPartial(activity, entryAdapter);
    }

    @Provides
    @ActivityScope
    public PermissionsPartial providePermissionsPartial(
            MainPresenter presenter,
            StoragePermissionRequest spr) {
        return new PermissionsPartial(activity, presenter, spr);
    }

    @Provides
    @ActivityScope
    public StoragePermissionRequest provideStoragePermissionRequest() {
        return new StoragePermissionRequest(activity);
    }

    @Provides
    @ActivityScope
    public ToolbarPartial provideToolbarPartial() {
        return new ToolbarPartial(activity, new StatusBarUtils(activity.getWindow()));
    }
}
