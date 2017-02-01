package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.test_lib.tests.BaseTest;

public class MainActivityTest extends BaseTest {
    private MainActivity activity;

    @Override
    public void beforeEach() {
        super.beforeEach();

        if (activity == null) {
            activity = startActivity(MainActivity.class);
        }
    }
}
