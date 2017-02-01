package com.davtyan.filemanager.components.main;

public class MainPresenter implements MainMvp.Presenter {
    private final MainMvp.View view;
    private final MainMvp.Model model;

    public MainPresenter(MainMvp.View view, MainMvp.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        view.setInternalStorageInfo(
                model.getInternalStorageFreeSpace(),
                model.getInternalStorageTotalSpace());
    }
}
