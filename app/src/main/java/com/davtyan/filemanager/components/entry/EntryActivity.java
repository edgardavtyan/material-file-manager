package com.davtyan.filemanager.components.entry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.data.Storage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryActivity extends AppCompatActivity implements EntryMvp.View {
    @BindView(R.id.list) RecyclerView list;

    private EntryMvp.Adapter adapter;
    private EntryPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);

        EntryMvp.Model model = new EntryModel();
        presenter = new EntryPresenter(this, model);
        adapter = new EntryAdapter(this, presenter);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        String path = getIntent().getStringExtra(EntryMvp.EXTRA_PATH);
        presenter.onCreate(path);
    }

    @Override
    public void onBackPressed() {
        presenter.onNavigateBack();
    }

    @Override
    public void updateEntries(Storage[] entries) {
        adapter.updateEntries(entries);
    }

    @Override
    public void close() {
        finish();
    }
}
