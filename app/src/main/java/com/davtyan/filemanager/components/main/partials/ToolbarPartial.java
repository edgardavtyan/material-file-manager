package com.davtyan.filemanager.components.main.partials;

import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainActivity;
import com.davtyan.filemanager.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolbarPartial {
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.current_path) TextView currentPathView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private final MainActivity activity;
    private final StatusBarUtils statusBarUtils;

    private final Drawable originalAppBarBackground;
    private final int originalStatusBarColor;
    private final int selectModeStatusBarColor;

    private boolean deleteMenuEnabled = false;
    private boolean copyMenuEnabled = false;
    private boolean cutMenuEnabled = false;
    private boolean pasteMenuEnabled = false;

    public ToolbarPartial(MainActivity activity, StatusBarUtils statusBarUtils) {
        this.activity = activity;
        this.statusBarUtils = statusBarUtils;

        ButterKnife.bind(this, activity);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        originalAppBarBackground = appbar.getBackground();
        originalStatusBarColor = statusBarUtils.getStatusBarColor();
        selectModeStatusBarColor = ContextCompat.getColor(activity, R.color.selectMode);
    }

    public void setCurrentPath(String currentPath) {
        currentPathView.setText(currentPath);
    }

    public void enterSelectMode() {
        deleteMenuEnabled = true;
        copyMenuEnabled = true;
        cutMenuEnabled = true;

        appbar.setBackgroundResource(R.color.selectMode);
        statusBarUtils.setStatusBarColor(selectModeStatusBarColor);
        activity.invalidateOptionsMenu();
    }

    public void exitSelectMode() {
        deleteMenuEnabled = false;
        copyMenuEnabled = false;
        cutMenuEnabled = false;

        appbar.setBackground(originalAppBarBackground);
        statusBarUtils.setStatusBarColor(originalStatusBarColor);
        activity.invalidateOptionsMenu();
    }

    public void setPasteMenuEnabled(boolean enabled) {
        pasteMenuEnabled = enabled;
        activity.invalidateOptionsMenu();
    }

    public void onCreateOptionsMenu(Menu menu) {
        setMenuItemEnabled(menu, R.id.menuitem_delete, deleteMenuEnabled);
        setMenuItemEnabled(menu, R.id.menuitem_copy, copyMenuEnabled);
        setMenuItemEnabled(menu, R.id.menuitem_cut, cutMenuEnabled);
        setMenuItemEnabled(menu, R.id.menuitem_paste, pasteMenuEnabled);
    }

    private void setMenuItemEnabled(Menu menu, @IdRes int menuItemId, boolean enabled) {
        MenuItem menuItem = menu.findItem(menuItemId);
        menuItem.setVisible(enabled);
        menuItem.setEnabled(enabled);
    }
}
