package com.davtyan.filemanager.components.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.utils.FileInfo;
import com.davtyan.filemanager.views.storage.StorageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.storage_internal) StorageView internalStorageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainModel model = new MainModel(this, new FileInfo());

        internalStorageView.setTitle(R.string.storage_internal_title);
        internalStorageView.setIcon(R.drawable.ic_smartphone);
        internalStorageView.setSpace(
                model.getInternalStorageFreeSpace(),
                model.getInternalStorageTotalSpace());
    }

    public void setInternalStorageInfo(long freeSpace, long totalSpace) {
        internalStorageView.setSpace(freeSpace ,totalSpace);
    }
}
