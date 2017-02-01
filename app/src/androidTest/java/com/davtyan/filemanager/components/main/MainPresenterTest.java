package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.test_lib.tests.BaseTest;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest extends BaseTest {
    private MainPresenter presenter;
    private MainMvp.View view;
    private MainMvp.Model model;

    @Override
    public void beforeEach() {
        super.beforeEach();
        view = mock(MainMvp.View.class);
        model = mock(MainMvp.Model.class);
        presenter = new MainPresenter(view, model);
    }

    @Test
    public void onCreate_setUpInternalStorage() {
        when(model.getInternalStorageFreeSpace()).thenReturn(100l);
        when(model.getInternalStorageTotalSpace()).thenReturn(200l);
        presenter.onCreate();
        verify(view).setInternalStorageInfo(100l, 200l);
    }
}
