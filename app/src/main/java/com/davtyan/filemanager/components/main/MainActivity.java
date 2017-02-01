package com.davtyan.filemanager.components.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.davtyan.filemanager.App;
import com.davtyan.filemanager.R;
import com.davtyan.filemanager.views.storage.StorageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainMvp.View {

    @BindView(R.id.storage_internal) StorageView internalStorageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainFactory factory = ((App) getApplicationContext()).getMainFactory(this, this);
        MainMvp.Presenter presenter = factory.getPresenter();
        presenter.onCreate();

        internalStorageView.setTitle(R.string.storage_internal_title);
        internalStorageView.setIcon(R.drawable.ic_smartphone);
    }

    @Override
    public void setInternalStorageInfo(long freeSpace, long totalSpace) {
        internalStorageView.setSpace(freeSpace ,totalSpace);
    }
}
