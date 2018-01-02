package com.davtyan.filemanager.components.main.partials;

import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainActivity;
import com.davtyan.filemanager.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

public class ToolbarPartial {
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.toolbar) @Getter Toolbar toolbar;

    private final MainActivity activity;
    private final StatusBarUtils statusBarUtils;

    private final Drawable originalAppBarBackground;
    private final int originalStatusBarColor;
    private final int selectModeStatusBarColor;

    public ToolbarPartial(MainActivity activity, StatusBarUtils statusBarUtils) {
        this.activity = activity;
        this.statusBarUtils = statusBarUtils;

        ButterKnife.bind(this, activity);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        originalAppBarBackground = appbar.getBackground();
        originalStatusBarColor = statusBarUtils.getStatusBarColor();
        selectModeStatusBarColor = ContextCompat.getColor(activity, R.color.selectMode);
    }

    public void setSelectedEntriesCount(int count) {
        toolbar.setTitle(activity.getString(R.string.select_mode_title, count));
    }

    public void enterSelectMode() {
        appbar.setBackgroundResource(R.color.selectMode);
        statusBarUtils.setStatusBarColor(selectModeStatusBarColor);
    }

    public void exitSelectMode() {
        toolbar.setTitle(R.string.app_name);
        appbar.setBackground(originalAppBarBackground);
        statusBarUtils.setStatusBarColor(originalStatusBarColor);
    }
}
