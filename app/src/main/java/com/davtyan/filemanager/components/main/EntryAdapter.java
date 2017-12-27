package com.davtyan.filemanager.components.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.data.Storage;
import com.davtyan.filemanager.utils.BoolUtils;

import java.util.Arrays;

public class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {

    private final Context context;
    private final MainPresenter presenter;

    public EntryAdapter(Context context, MainPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem_entry, parent, false);
        return new EntryViewHolder(view, presenter);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        presenter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getEntryCount();
    }

    public void updateEntries(Storage[] entries) {
        Arrays.sort(entries, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        Arrays.sort(entries, (a, b) -> BoolUtils.compare(a.isFile(), b.isFile()));
        notifyDataSetChanged();
    }
}
