package com.davtyan.filemanager.components.main.partials;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainActivity;
import com.davtyan.filemanager.components.main.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerPartial {
    private static final int GROUP_STORAGE = 0;
    private static final int ITEM_STORAGE_INTERNAL = 0;
    private static final int ITEM_STORAGE_EXTERNAL = 1;

    @BindView(R.id.navigation_view) NavigationView navView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;

    private final MainPresenter presenter;
    private final Menu navMenu;
    private final ActionBarDrawerToggle navDrawerToggle;

    @SuppressWarnings("FieldCanBeLocal")
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

    public DrawerPartial(MainActivity activity, MainPresenter presenter, Toolbar toolbar) {
        this.presenter = presenter;

        ButterKnife.bind(this, activity);

        navMenu = navView.getMenu();
        navView.setNavigationItemSelectedListener(navItemSelectedListener);
        navDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(navDrawerToggle);
    }

    public void syncState() {
        navDrawerToggle.syncState();
    }

    public void setInternalStorageItem() {
        navMenu.add(GROUP_STORAGE, ITEM_STORAGE_INTERNAL, 0, R.string.storage_internal_title)
               .setIcon(R.drawable.ic_smartphone);
    }

    public void setExternalStorageItem(String name) {
        navMenu.add(GROUP_STORAGE, ITEM_STORAGE_EXTERNAL, 1, name)
               .setIcon(R.drawable.ic_sdcard);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }
}
