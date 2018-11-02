package com.davtyan.filemanager.components.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.base.BaseActivity;
import com.davtyan.filemanager.components.main.dialogs.DeleteConfirmDialog;
import com.davtyan.filemanager.components.main.dialogs.NewFolderDialog;
import com.davtyan.filemanager.components.main.dialogs.RenameDialog;
import com.davtyan.filemanager.components.main.partials.DrawerPartial;
import com.davtyan.filemanager.components.main.partials.EmptyDirectoryPartial;
import com.davtyan.filemanager.components.main.partials.ListPartial;
import com.davtyan.filemanager.components.main.partials.PermissionsPartial;
import com.davtyan.filemanager.components.main.partials.ToolbarPartial;
import com.davtyan.filemanager.data.Entry;
import com.davtyan.filemanager.lib.StorageAccessFramework;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    MainPresenter presenter;
    DeleteConfirmDialog deleteConfirmDialog;
    RenameDialog renameDialog;
    NewFolderDialog newFolderDialog;

    ToolbarPartial toolbarPartial;
    PermissionsPartial permissionsPartial;
    DrawerPartial drawerPartial;
    ListPartial listPartial;
    EmptyDirectoryPartial emptyDirectoryPartial;
    StorageAccessFramework saf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getFactory().inject();
        saf.makeAccessRequest();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerPartial.syncState();
        presenter.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        saf.persistPermissions(requestCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onCheckPermission();
    }

    @Override
    public void onBackPressed() {
        presenter.onNavigateBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        toolbarPartial.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_delete:
                presenter.onDeleteMenuItemClicked();
                return true;
            case R.id.menuitem_newFolder:
                presenter.onNewFolderMenuItemClicked();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsPartial.onRequestPermissionResult(requestCode, grantResults);
    }

    public void close() {
        finish();
    }

    public void updateEntries(Entry[] entries) {
        listPartial.updateEntries(entries);

        if (entries.length == 0) {
            listPartial.hideList();
            emptyDirectoryPartial.show();

        } else {
            listPartial.showList();
            emptyDirectoryPartial.hide();
        }
    }

    public void setCurrentPath(String currentPath) {
        toolbarPartial.setCurrentPath(currentPath);
    }

    public void updateViewSelectionAt(int position) {
        listPartial.notifyItemChanged(position);
        toolbarPartial.setDeleteMenuEnabled(true);
    }

    public void setSelectedEntriesCount(int count) {
        toolbarPartial.setSelectedEntriesCount(count);
    }

    public void enterSelectMode() {
        toolbarPartial.enterSelectMode();
    }

    public void exitSelectMode() {
        toolbarPartial.exitSelectMode();
        listPartial.notifyDataSetChanged();
    }

    public void setInternalStorage() {
        drawerPartial.setInternalStorageItem();
    }

    public void setExternalStorage(String name) {
        drawerPartial.setExternalStorageItem(name);
    }

    public void closeDrawer() {
        drawerPartial.closeDrawer();
    }

    public void showDeleteConfirmDialog() {
        deleteConfirmDialog.show();
    }

    public void showRenameDialog(String text) {
        renameDialog.show(text);
    }

    public void showRenameExistsError() {
        renameDialog.showExistsError();
    }

    public void showNewFolderDialog() {
        newFolderDialog.show();
    }

    public void closeNewFolderDialog() {
        newFolderDialog.close();
    }

    public void showNewFolderExistsError() {
        newFolderDialog.showExistsError();
    }

    public void showStoragePermissionNeverAskAgainError() {
        listPartial.hideList();
        permissionsPartial.showNeverAskAgainError();
    }

    public void showStoragePermissionDeniedError() {
        listPartial.hideList();
        permissionsPartial.showDeniedError();
    }

    public void waitForAnimation() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected MainFactory getFactory() {
        return new MainFactory(this);
    }
}
