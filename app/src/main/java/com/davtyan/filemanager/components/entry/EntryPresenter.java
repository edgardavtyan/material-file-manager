package com.davtyan.filemanager.components.entry;

import com.davtyan.filemanager.data.Storage;

import lombok.Getter;

public class EntryPresenter implements EntryMvp.Presenter {
    private final EntryMvp.View view;
    private final EntryMvp.Model model;

    private @Getter boolean isInSelectMode;

    public EntryPresenter(EntryMvp.View view, EntryMvp.Model model) {
        this.view = view;
        this.model = model;
        isInSelectMode = false;
    }

    @Override
    public void onCreate(String path) {
        model.updateEntries(path);
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentPath());
    }

    @Override
    public void onEntryClick(int position) {
        model.navigateForward(position);
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentPath());
    }

    @Override
    public void onEntryToggleSelected(int position) {
        model.toggleEntrySelectedAt(position);
        view.updateViewSelectionAt(position);
        isInSelectMode = true;
    }

    @Override
    public void onNavigateBack() {
        if (isInSelectMode) {
            isInSelectMode = false;
            model.clearSelections();
            view.clearSelections();
        } else {
            if (model.isAtRoot()) {
                view.close();
            } else {
                model.navigateBack();
                view.updateEntries(model.getEntries());
                view.setCurrentPath(model.getCurrentPath());
            }
        }
    }

    @Override
    public void onBindViewHolder(EntryMvp.ViewHolder holder, int position) {
        Storage entry = model.getEntries()[position];
        holder.setTitle(entry.getName());
        holder.setIsDirectory(entry.isDirectory());
        holder.setIsSelected(entry.isSelected());
    }

    @Override
    public int getEntryCount() {
        return model.getEntries().length;
    }
}
