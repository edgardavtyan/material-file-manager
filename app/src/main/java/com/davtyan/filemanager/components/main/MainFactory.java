package com.davtyan.filemanager.components.main;

import android.content.Context;

public class MainFactory {
    private final Context context;
    private final MainMvp.View view;
    private MainMvp.Model model;
    private MainMvp.Presenter presenter;

    public MainFactory(Context context, MainMvp.View view) {
        this.context = context;
        this.view = view;
    }

    public Context getContext() {
        return context;
    }

    public MainMvp.View getView() {
        return view;
    }

    public MainMvp.Model getModel() {
        if (model == null)
            model = new MainModel(getContext());
        return model;
    }

    public MainMvp.Presenter getPresenter() {
        if (presenter == null)
            presenter = new MainPresenter(getView(), getModel());
        return presenter;
    }
}
