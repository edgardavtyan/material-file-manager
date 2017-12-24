package com.davtyan.filemanager.components.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.davtyan.filemanager.App;
import com.davtyan.filemanager.R;
import com.davtyan.filemanager.base.BaseActivity;
import com.davtyan.filemanager.components.entry.EntryActivity;
import com.davtyan.filemanager.data.Storage;
import com.davtyan.filemanager.views.storage.StorageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvp.View {

    @BindView(R.id.storage_internal) StorageView internalStorageView;
    @BindView(R.id.storage_container) LinearLayout storageContainer;

    private MainMvp.Presenter presenter;

    private View.OnClickListener onInternalStorageClick = view -> presenter.onInternalStorageClick();

    private View.OnClickListener onExternalStorageClick = view -> presenter.onExternalStorageClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainFactory factory = ((App) getApplicationContext()).getMainFactory(this, this);
        presenter = factory.getPresenter();
        presenter.onCreate();

        internalStorageView.setTitle(R.string.storage_internal_title);
        internalStorageView.setIcon(R.drawable.ic_smartphone);
        internalStorageView.setClickable(true);
        internalStorageView.setFocusable(true);
        internalStorageView.setOnClickListener(onInternalStorageClick);
    }

    @Override
    public void setInternalStorage(Storage storage) {
        internalStorageView.setSpace(storage.getFreeSpace(), storage.getTotalSpace());
    }

    @Override
    public void setExternalStorage(Storage storage) {
        StorageView storageView = new StorageView(this);
        storageView.setTitle(storage.getName());
        storageView.setSpace(storage.getFreeSpace(), storage.getTotalSpace());
        storageView.setIcon(R.drawable.ic_sdcard);
        storageView.setOnClickListener(onExternalStorageClick);
        storageView.setClickable(true);
        storageView.setFocusable(true);
        storageContainer.addView(storageView);
    }

    @Override
    public void gotoEntryActivity(String path) {
        Intent intent = new Intent(this, EntryActivity.class);
        intent.putExtra(EntryActivity.EXTRA_PATH, path);
        startActivity(intent);
    }
}
