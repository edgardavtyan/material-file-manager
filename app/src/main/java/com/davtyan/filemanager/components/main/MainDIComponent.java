package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = MainDIModule.class)
public interface MainDIComponent {
    void inject(MainActivity activity);
}
