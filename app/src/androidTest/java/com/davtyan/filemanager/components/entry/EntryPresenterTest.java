package com.davtyan.filemanager.components.entry;

import com.davtyan.filemanager.data.Storage;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EntryPresenterTest {
    private EntryPresenter presenter;
    private EntryActivity view;
    private EntryModel model;

    private Storage[] entries;
    private String currentPath;
    private int selectedEntriesCount;

    @Before
    public void setUp() throws Exception {
        entries = new Storage[5];
        currentPath = "path";
        selectedEntriesCount = 3;

        model = mock(EntryModel.class);
        when(model.getEntries()).thenReturn(entries);
        when(model.getCurrentPath()).thenReturn(currentPath);
        when(model.getSelectedEntriesCount()).thenReturn(selectedEntriesCount);

        view = mock(EntryActivity.class);
        presenter = new EntryPresenter(view, model);
    }

    @Test
    public void onCreate_updateModelAndView() {
        Storage[] entries = new Storage[5];
        when(model.getEntries()).thenReturn(entries);

        presenter.onCreate(currentPath);

        verify(model).updateEntries(currentPath);
        verify(view).updateEntries(entries);
        verify(view).setCurrentPath(currentPath);
    }

    @Test
    public void onEntryClick_navigateModelForwardAndUpdateView() {
        presenter.onEntryClick(0);
        verify(model).navigateForward(0);
        verify(view).updateEntries(entries);
        verify(view).setCurrentPath(currentPath);
    }

    @Test
    public void onEntryToggleSelected_toggleEntrySelected() {
        presenter.onEntryToggleSelected(0);
        verify(model).toggleEntrySelectedAt(0);
        verify(view).updateViewSelectionAt(0);
        verify(view).enterSelectMode(selectedEntriesCount);
        assertThat(presenter.isInSelectMode()).isTrue();
    }

    @Test
    public void onEntryToggleSelected_lastEntryUnselected_exitSelectMode() {
        when(model.getSelectedEntriesCount()).thenReturn(0);
        presenter.onEntryToggleSelected(0);
        verify(view).exitSelectMode();
        assertThat(presenter.isInSelectMode()).isFalse();
    }

    @Test
    public void onNavigateBack_inSelectedMode_clearSelection() {
        presenter.onEntryToggleSelected(0);
        presenter.onNavigateBack();
        verify(model).clearSelections();
        verify(view).clearSelections();
        verify(view).exitSelectMode();
        assertThat(presenter.isInSelectMode()).isFalse();
    }

    @Test
    public void onNavigateBack_notInSelectMode_atRoot_closeView() {
        when(model.isAtRoot()).thenReturn(true);
        presenter.onNavigateBack();
        verify(view).close();
    }

    @Test
    public void onNavigateBack_notInSelectMode_notAtRoot_navigateModelAndViewBack() {
        when(model.isAtRoot()).thenReturn(false);
        presenter.onNavigateBack();
        verify(model).navigateBack();
        verify(view).updateEntries(entries);
        verify(view).setCurrentPath(currentPath);
    }

    @Test
    public void onBindViewHolder_setViewHolderValues() {
        Storage entry = mock(Storage.class);
        when(entry.getName()).thenReturn("name");
        when(entry.isDirectory()).thenReturn(true);
        when(entry.isSelected()).thenReturn(true);
        entries[0] = entry;

        EntryViewHolder holder = mock(EntryViewHolder.class);

        presenter.onBindViewHolder(holder, 0);

        verify(holder).setTitle("name");
        verify(holder).setIsDirectory(true);
        verify(holder).setIsSelected(true);
    }

    @Test
    public void getEntryCount_returnEntryCount() {
        assertThat(presenter.getEntryCount()).isEqualTo(5);
    }

    @Test
    public void onDeleteMenuItemClicked_deleteSelectedEntries() {
        presenter.onDeleteMenuItemClicked();
        verify(model).deleteSelectedItems();
        verify(view).updateEntries(entries);
    }
}
