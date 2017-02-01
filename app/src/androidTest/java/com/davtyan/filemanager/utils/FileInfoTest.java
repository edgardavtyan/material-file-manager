package com.davtyan.filemanager.utils;

import com.davtyan.filemanager.test_lib.tests.BaseTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileInfoTest extends BaseTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    FileInfo fileInfo;

    @Override
    public void beforeEach() {
        super.beforeEach();
        fileInfo = new FileInfo();
    }

    @Test
    public void getFreeSpace_returnCorrectFreeSpace() throws IOException {
        assertThat(fileInfo.getFreeSpace(context.getFilesDir().getAbsolutePath())).isNotZero();
    }

    @Test
    public void getFreeSpace_givenFile_returnFileFreeSpace() {
        File file = mock(File.class);
        when(file.getFreeSpace()).thenReturn(200l);
        assertThat(fileInfo.getFreeSpace(file)).isEqualTo(200l);
    }

    @Test
    public void getTotalSpace_givenFile_returnFileTotalSpace() {
        File file = mock(File.class);
        when(file.getTotalSpace()).thenReturn(200l);
        assertThat(fileInfo.getTotalSpace(file)).isEqualTo(200l);
    }
}
