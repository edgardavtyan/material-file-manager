package com.davtyan.filemanager.components.main.partials;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.EntryAdapter;
import com.davtyan.filemanager.components.main.MainActivity;
import com.davtyan.filemanager.data.Storage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListPartial {
    @BindView(R.id.list) RecyclerView list;
    private final EntryAdapter adapter;

    public ListPartial(MainActivity activity, EntryAdapter adapter) {
        this.adapter = adapter;

        ButterKnife.bind(this, activity);

        list.setLayoutManager(new LinearLayoutManager(activity));
        list.setAdapter(adapter);
        ((SimpleItemAnimator) list.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    public void showList() {
        list.setVisibility(View.VISIBLE);
    }

    public void hideList() {
        list.setVisibility(View.GONE);
    }

    public void updateEntries(Storage[] entries) {
        adapter.updateEntries(entries);
    }

    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}
