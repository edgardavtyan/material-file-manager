package com.davtyan.filemanager.components.main;

import com.davtyan.filemanager.data.Storage;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {
    private MainPresenter presenter;
    private MainActivity view;
    private MainModel model;

    private Storage[] entries;
    private String currentPath;
    private int selectedEntriesCount;

    @Before
    public void setUp() throws Exception {
        entries = new Storage[5];
        currentPath = "path";
        selectedEntriesCount = 3;

        model = mock(MainModel.class);
        when(model.getEntries()).thenReturn(entries);
        when(model.getCurrentPath()).thenReturn(currentPath);
        when(model.getSelectedEntriesCount()).thenReturn(selectedEntriesCount);

        view = mock(MainActivity.class);
        presenter = new MainPresenter(view, model);
    }

    @Test
    public void onCreate_updateModelAndView() {
        Storage[] entries = new Storage[5];
        when(model.getEntries()).thenReturn(entries);

        Storage internalStorage = mock(Storage.class);
        when(internalStorage.getPath()).thenReturn(currentPath);
        when(model.getInternalStorage()).thenReturn(internalStorage);

        Storage externalStorage = mock(Storage.class);
        when(externalStorage.getName()).thenReturn("external");
        when(model.getExternalStorage()).thenReturn(externalStorage);

        presenter.onCreate();

        verify(model).updateEntries(currentPath);
        verify(view).updateEntries(entries);
        verify(view).setCurrentPath(currentPath);
        verify(view).setExternalStorage("external");
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
        verify(view).enterSelectMode();
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
    public void onDeleteMenuItemClicked_showDeleteConfirmDialog() {
        presenter.onDeleteMenuItemClicked();
        verify(view).showDeleteConfirmDialog();
    }

    @Test
    public void onDeleteConfirmDialogPositiveButtonClicked_deleteSelectedEntries() {
        presenter.onDeleteConfirmDialogPositiveButtonClicked();
        verify(model).deleteSelectedItems();
        verify(model).clearSelections();
        verify(view).updateEntries(entries);
        verify(view).exitSelectMode();
        assertThat(presenter.isInSelectMode()).isFalse();
    }
}
