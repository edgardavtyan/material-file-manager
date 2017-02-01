package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.test_lib.tests.BaseTest;
import com.davtyan.filemanager.utils.FileInfo;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainModelTest extends BaseTest {
    private MainModel mainModel;
    private FileInfo fileInfo;

    @Override
    public void beforeEach() {
        super.beforeEach();
        fileInfo = mock(FileInfo.class);
        mainModel = new MainModel(context, fileInfo);
    }

    @Test
    public void getInternalStorageFreeSpace_returnCorrectSpaceSize() {
        when(fileInfo.getFreeSpace(context.getFilesDir())).thenReturn(100l);
        assertThat(mainModel.getInternalStorageFreeSpace()).isEqualTo(100l);
    }

    @Test
    public void getInternalStorageTotalSpace_returnCorrectSpace() {
        when(fileInfo.getTotalSpace(context.getFilesDir())).thenReturn(200l);
        assertThat(mainModel.getInternalStorageTotalSpace()).isEqualTo(200l);
    }
}
