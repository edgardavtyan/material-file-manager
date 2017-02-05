package com.davtyan.filemanager.components.entry;

import com.davtyan.filemanager.data.Storage;

public class EntryPresenter implements EntryMvp.Presenter {
    private final EntryMvp.View view;
    private final EntryMvp.Model model;

    public EntryPresenter(EntryMvp.View view, EntryMvp.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate(String path) {
        model.updateEntries(path);
        view.updateEntries(model.getEntries());
    }

    @Override
    public void onEntryClick(int position) {
        model.updateEntries(model.getEntryAt(position).getPath());
        view.updateEntries(model.getEntries());
    }

    @Override
    public void onBindViewHolder(EntryMvp.ViewHolder holder, int position) {
        Storage entry = model.getEntryAt(position);
        holder.setTitle(entry.getName());
        holder.setIsDirectory(entry.isDirectory());
    }

    @Override
    public int getEntryCount() {
        return model.getEntries().length;
    }
}
