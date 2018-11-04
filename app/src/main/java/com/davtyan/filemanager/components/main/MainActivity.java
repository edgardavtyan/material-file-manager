package com.davtyan.filemanager.components.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @Inject MainPresenter presenter;
    @Inject StorageAccessFramework saf;

    @Inject DeleteConfirmDialog deleteConfirmDialog;
    @Inject RenameDialog renameDialog;
    @Inject NewFolderDialog newFolderDialog;

    @Inject ToolbarPartial toolbarPartial;
    @Inject PermissionsPartial permissionsPartial;
    @Inject DrawerPartial drawerPartial;
    @Inject ListPartial listPartial;
    @Inject EmptyDirectoryPartial emptyDirectoryPartial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DaggerMainDIComponent
                .builder()
                .mainDIModule(new MainDIModule(this))
                .build()
                .inject(this);

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
            case R.id.menuitem_copy:
                presenter.onToolbarCopyClicked();
                return true;
            case R.id.menuitem_cut:
                presenter.onToolbarCutClicked();
                return true;
            case R.id.menuitem_paste:
                presenter.onToolbarPasteClicked();
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
    }

    public void enterSelectMode() {
        toolbarPartial.enterSelectMode();
    }

    public void exitSelectMode() {
        toolbarPartial.exitSelectMode();
        listPartial.notifyDataSetChanged();
    }

    public void setPasteMenuEnabled(boolean enabled) {
        toolbarPartial.setPasteMenuEnabled(enabled);
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

    public void showDeleteOperationFailed() {
        Toast.makeText(this, R.string.toast_delete_failed, Toast.LENGTH_SHORT).show();
    }

    public void showRenameDialog(String text) {
        renameDialog.show(text);
    }

    public void showRenameExistsError() {
        renameDialog.showExistsError();
    }

    public void showRenameFailedError() {
        Toast.makeText(this, R.string.toast_rename_failed, Toast.LENGTH_SHORT).show();
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

    public void showNewFolderFailedError() {
        Toast.makeText(this, R.string.toast_newFolder_failed, Toast.LENGTH_SHORT).show();
    }

    public void showCopyFailedError() {
        Toast.makeText(this, R.string.toast_copy_failed, Toast.LENGTH_SHORT).show();
    }

    public void showCutFailedError() {
        Toast.makeText(this, R.string.toast_cut_failed, Toast.LENGTH_SHORT).show();
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
}
