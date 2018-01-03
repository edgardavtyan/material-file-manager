package com.davtyan.filemanager.utils;

import android.os.Build;
import android.view.Window;

public class StatusBarUtils {
    private final Window window;

    public StatusBarUtils(Window window) {
        this.window = window;
    }

    public int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return window.getStatusBarColor();
        } else {
            return 0;
        }
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
        }
    }
}
