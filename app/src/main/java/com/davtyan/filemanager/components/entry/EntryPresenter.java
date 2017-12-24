package com.davtyan.filemanager.components.entry;

import com.davtyan.filemanager.data.Storage;

import lombok.Getter;

public class EntryPresenter {
    private final EntryActivity view;
    private final EntryModel model;

    private @Getter boolean isInSelectMode;

    public EntryPresenter(EntryActivity view, EntryModel model) {
        this.view = view;
        this.model = model;
        isInSelectMode = false;
    }

    public void onCreate(String path) {
        model.updateEntries(path);
        view.updateEntries(model.getEntries());
            view.setCurrentPath(model.getCurrentPath());
    }

    public void onEntryClick(int position) {
        model.navigateForward(position);
        view.updateEntries(model.getEntries());
        view.setCurrentPath(model.getCurrentPath());
    }

    public void onEntryToggleSelected(int position) {
        model.toggleEntrySelectedAt(position);
        view.updateViewSelectionAt(position);

        if (model.getSelectedEntriesCount() == 0) {
            view.exitSelectMode();
            isInSelectMode = false;
        } else {
            view.enterSelectMode(model.getSelectedEntriesCount());
            isInSelectMode = true;
        }
    }

    public void onNavigateBack() {
        if (isInSelectMode) {
            isInSelectMode = false;
            model.clearSelections();
            view.clearSelections();
            view.exitSelectMode();
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

    public void onBindViewHolder(EntryViewHolder holder, int position) {
        Storage entry = model.getEntries()[position];
        holder.setTitle(entry.getName());
        holder.setIsDirectory(entry.isDirectory());
        holder.setIsSelected(entry.isSelected());
    }

    public int getEntryCount() {
        return model.getEntries().length;
    }

    public void onDeleteMenuItemClicked() {
        model.deleteSelectedItems();
        view.updateEntries(model.getEntries());
        view.exitSelectMode();
    }
}
