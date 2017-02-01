package com.davtyan.filemanager;

import android.app.Application;
import android.content.Context;

import com.davtyan.filemanager.components.main.MainFactory;
import com.davtyan.filemanager.components.main.MainMvp;

public class App extends Application {
    private MainFactory mainFactory;

    public MainFactory getMainFactory(Context context, MainMvp.View view) {
        if (mainFactory == null)
            return new MainFactory(context, view);
        return mainFactory;
    }
}
