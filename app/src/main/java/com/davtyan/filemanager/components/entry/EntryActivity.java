package com.davtyan.filemanager.components.entry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryActivity extends AppCompatActivity implements EntryMvp.View {
    @BindView(R.id.list) RecyclerView list;

    private EntryMvp.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);

        EntryMvp.Model model = new EntryModel();
        EntryMvp.Presenter presenter = new EntryPresenter(this, model);
        adapter = new EntryAdapter(this, presenter);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        String path = getIntent().getStringExtra(EntryMvp.EXTRA_PATH);
        presenter.onCreate(path);
    }

    @Override
    public void updateEntries(String[] entries) {
        adapter.updateEntries(entries);
    }
}
