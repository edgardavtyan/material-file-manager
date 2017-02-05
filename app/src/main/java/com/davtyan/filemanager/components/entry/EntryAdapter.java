package com.davtyan.filemanager.components.entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davtyan.filemanager.R;

public class EntryAdapter extends EntryMvp.Adapter {
    private final Context context;

    private String[] entries;

    public EntryAdapter(Context context, EntryMvp.Presenter presenter) {
        this.context = context;
    }

    @Override
    public EntryMvp.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem_entry, parent, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntryMvp.ViewHolder holder, int position) {
        holder.setTitle(entries[position]);
    }

    @Override
    public int getItemCount() {
        return entries.length;
    }

    @Override
    public void updateEntries(String[] entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }
}
