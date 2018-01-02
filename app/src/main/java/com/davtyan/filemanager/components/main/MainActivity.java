package com.davtyan.filemanager.components.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.base.BaseActivity;
import com.davtyan.filemanager.components.main.partials.ToolbarPartial;
import com.davtyan.filemanager.data.Storage;
import com.davtyan.filemanager.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static final int GROUP_STORAGE = 0;
    private static final int ITEM_STORAGE_INTERNAL = 0;
    private static final int ITEM_STORAGE_EXTERNAL = 1;

    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.current_path) TextView currentPathView;
    @BindView(R.id.empty_directory_msg) LinearLayout emptyDirectoryView;
    @BindView(R.id.navigation_view) NavigationView navView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.permission_storage_error_neverAskAgain) LinearLayout storagePermissionNeverAskAgainErrorView;
    @BindView(R.id.permission_storage_error_denied) LinearLayout storagePermissionDeniedErrorView;
    @BindView(R.id.permission_storage_link_goto_settings) TextView gotoSettingsLinkView;
    @BindView(R.id.permission_storage_link_request) TextView requestStoragePermissionLinkView;

    private EntryAdapter adapter;
    private MainPresenter presenter;
    private StoragePermissionRequest storagePermissionRequest;
    private DeleteConfirmDialog deleteConfirmDialog;
    private ActionBarDrawerToggle navDrawerToggle;
    private Menu navMenu;

    private ToolbarPartial toolbarPartial;

    private boolean deleteMenuEnabled;

    private final NavigationView.OnNavigationItemSelectedListener navItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case ITEM_STORAGE_INTERNAL:
                    presenter.onInternalStorageClicked();
                    return true;
                case ITEM_STORAGE_EXTERNAL:
                    presenter.onExternalStorageClicked();
                    return true;
                default:
                    return false;
            }
        }
    };

    private final View.OnClickListener onGotoSettingsClickListener = v -> {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    };

    private final View.OnClickListener onRequestStoragePermissionLinkClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onRequestStoragePermissionLinkClicked();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbarPartial = new ToolbarPartial(this, new StatusBarUtils(getWindow()));

        MainFactory factory = getFactory();
        storagePermissionRequest = factory.getStoragePermissionRequest();
        presenter = factory.getPresenter();
        adapter = factory.getAdapter();
        deleteConfirmDialog = factory.getDeleteConfirmDialog();

        deleteMenuEnabled = false;

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        ((SimpleItemAnimator) list.getItemAnimator()).setSupportsChangeAnimations(false);

        gotoSettingsLinkView.setOnClickListener(onGotoSettingsClickListener);
        requestStoragePermissionLinkView.setOnClickListener(onRequestStoragePermissionLinkClickListener);

        navMenu = navView.getMenu();
        navView.setNavigationItemSelectedListener(navItemSelectedListener);
        navDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarPartial.getToolbar(), 0, 0);
        drawerLayout.addDrawerListener(navDrawerToggle);
        drawerLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navDrawerToggle.syncState();
        presenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onCheckPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onCheckPermission();
    }

    @Override
    public void onBackPressed() {
        presenter.onNavigateBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);

        MenuItem deleteMenuItem = menu.findItem(R.id.menuitem_delete);
        deleteMenuItem.setVisible(deleteMenuEnabled);
        deleteMenuItem.setEnabled(deleteMenuEnabled);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_delete:
                presenter.onDeleteMenuItemClicked();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        storagePermissionRequest.onRequestPermissionResult(requestCode, grantResults);
    }

    public void close() {
        finish();
    }

    public void updateEntries(Storage[] entries) {
        adapter.updateEntries(entries);

        if (entries.length == 0) {
            list.setVisibility(View.GONE);
            emptyDirectoryView.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            emptyDirectoryView.setVisibility(View.GONE);
        }
    }

    public void setCurrentPath(String currentPath) {
        currentPathView.setText(currentPath);
    }

    public void updateViewSelectionAt(int position) {
        adapter.notifyItemChanged(position);
        deleteMenuEnabled = true;
    }

    public void setSelectedEntriesCount(int count) {
        toolbarPartial.setSelectedEntriesCount(count);
    }

    public void enterSelectMode() {
        toolbarPartial.enterSelectMode();
        invalidateOptionsMenu();
    }

    public void exitSelectMode() {
        toolbarPartial.exitSelectMode();
        adapter.notifyDataSetChanged();
        deleteMenuEnabled = false;
        invalidateOptionsMenu();
    }

    public void setInternalStorage() {
        navMenu.add(GROUP_STORAGE, ITEM_STORAGE_INTERNAL, 0, "Internal storage")
                .setIcon(R.drawable.ic_smartphone);
    }

    public void setExternalStorage(String name) {
        navMenu.add(GROUP_STORAGE, ITEM_STORAGE_EXTERNAL, 1, name)
                .setIcon(R.drawable.ic_sdcard);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }

    public void showDeleteConfirmDialog() {
        deleteConfirmDialog.show();
    }

    public void showStoragePermissionNeverAskAgainError() {
        list.setVisibility(View.GONE);
        storagePermissionNeverAskAgainErrorView.setVisibility(View.VISIBLE);
    }

    public void showStoragePermissionDeniedError() {
        list.setVisibility(View.GONE);
        storagePermissionDeniedErrorView.setVisibility(View.VISIBLE);
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
