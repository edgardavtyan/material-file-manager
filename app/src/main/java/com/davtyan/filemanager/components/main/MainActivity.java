package com.davtyan.filemanager.components.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.base.BaseActivity;
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
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.navigation_view) NavigationView navView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.permission_storage_error_neverAskAgain) LinearLayout permissionStorageNeverAskAgainErrorView;
    @BindView(R.id.goto_settings_link) TextView gotoSettingsLinkView;
    @BindView(R.id.permission_storage_error_denied) LinearLayout permissionStorageDeniedErrorView;
    @BindView(R.id.permission_storage_request_link) TextView requestStoragePermissionLinkView;

    private EntryAdapter adapter;
    private MainPresenter presenter;
    private StoragePermissionRequest storagePermissionRequest;

    private Menu navMenu;
    private ActionBarDrawerToggle navDrawerToggle;

    private boolean deleteMenuEnabled;
    private DeleteConfirmDialog deleteConfirmDialog;

    private StatusBarUtils statusBarUtils;
    private Drawable originalAppBarBackground;
    private int originalStatusBarColor;

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

    private final StoragePermissionRequest.OnStoragePermissionGrantedListener onStoragePermissionGrantedListener
            = new StoragePermissionRequest.OnStoragePermissionGrantedListener() {
        @Override
        public void onStoragePermissionGranted() {
            presenter.onStoragePermissionGranted();
        }
    };

    private final StoragePermissionRequest.OnStoragePermissionDeniedListener onStoragePermissionDeniedListener
            = new StoragePermissionRequest.OnStoragePermissionDeniedListener() {
        @Override
        public void onStoragePermissionDenied() {
            presenter.onStoragePermissionDenied();
        }
    };

    private final View.OnClickListener onGotoSettingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
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
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        StoragePermissionPrefs storagePermissionPrefs = new StoragePermissionPrefs(prefs);
        storagePermissionRequest = new StoragePermissionRequest(this, storagePermissionPrefs);

        presenter = new MainPresenter(this, new MainModel(), storagePermissionRequest);
        adapter = new EntryAdapter(this, presenter);

        deleteMenuEnabled = false;
        deleteConfirmDialog = new DeleteConfirmDialog(this, presenter);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        ((SimpleItemAnimator) list.getItemAnimator()).setSupportsChangeAnimations(false);

        navMenu = navView.getMenu();
        navView.setNavigationItemSelectedListener(navItemSelectedListener);
        navDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(navDrawerToggle);
        drawerLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));

        statusBarUtils = new StatusBarUtils(getWindow());

        originalAppBarBackground = appbar.getBackground();
        originalStatusBarColor = statusBarUtils.getStatusBarColor();

        gotoSettingsLinkView.setOnClickListener(onGotoSettingsClickListener);
        requestStoragePermissionLinkView.setOnClickListener(onRequestStoragePermissionLinkClickListener);

        presenter.onCreate();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navDrawerToggle.syncState();
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
        toolbar.setTitle(getString(R.string.select_mode_title, count));
    }

    public void enterSelectMode() {
        appbar.setBackgroundResource(R.color.selectMode);
        statusBarUtils.setStatusBarColor(ContextCompat.getColor(this, R.color.selectModeDark));
        invalidateOptionsMenu();
    }

    public void exitSelectMode() {
        toolbar.setTitle(R.string.app_name);
        appbar.setBackground(originalAppBarBackground);
        statusBarUtils.setStatusBarColor(originalStatusBarColor);
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

    public void showStoragePermissionError() {
        list.setVisibility(View.GONE);
        permissionStorageNeverAskAgainErrorView.setVisibility(View.VISIBLE);
    }

    public void showStoragePermissionDeniedError() {
        list.setVisibility(View.GONE);
        permissionStorageDeniedErrorView.setVisibility(View.VISIBLE);
    }
}
