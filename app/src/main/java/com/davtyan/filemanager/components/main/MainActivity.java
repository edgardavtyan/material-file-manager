package com.davtyan.filemanager.components.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
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

    private EntryAdapter adapter;
    private MainPresenter presenter;

    private Menu navMenu;

    private boolean deleteMenuEnabled;

    private StatusBarUtils statusBarUtils;
    private Drawable originalAppBarBackground;
    private int originalStatusBarColor;

    private NavigationView.OnNavigationItemSelectedListener navItemSelectedListener
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        deleteMenuEnabled = false;

        MainModel model = new MainModel();
        presenter = new MainPresenter(this, model);
        adapter = new EntryAdapter(this, presenter);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        ((SimpleItemAnimator) list.getItemAnimator()).setSupportsChangeAnimations(false);

        navMenu = navView.getMenu();
        navView.setNavigationItemSelectedListener(navItemSelectedListener);

        presenter.onCreate();

        statusBarUtils = new StatusBarUtils(getWindow());

        originalAppBarBackground = appbar.getBackground();
        originalStatusBarColor = statusBarUtils.getStatusBarColor();

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

    public void updateViewSelectionAt(int position) {
        adapter.notifyItemChanged(position);
        deleteMenuEnabled = true;
        invalidateOptionsMenu();
    }

    public void clearSelections() {
        adapter.notifyDataSetChanged();
        deleteMenuEnabled = false;
        invalidateOptionsMenu();
    }

    public void setCurrentPath(String path) {
        currentPathView.setText(path);
    }

    public void close() {
        finish();
    }

    public void enterSelectMode(int entriesCount) {
        toolbar.setTitle(getString(R.string.select_mode_title, entriesCount));
        appbar.setBackgroundResource(R.color.selectMode);
        statusBarUtils.setStatusBarColor(ContextCompat.getColor(this, R.color.selectModeDark));
    }

    public void exitSelectMode() {
        toolbar.setTitle(R.string.app_name);
        appbar.setBackground(originalAppBarBackground);
        statusBarUtils.setStatusBarColor(originalStatusBarColor);
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
}
