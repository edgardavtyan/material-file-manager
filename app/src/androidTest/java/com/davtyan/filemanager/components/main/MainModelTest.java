package com.davtyan.filemanager.components.main;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MainModelTest {
    @Rule public TemporaryFolder tempFolderRoot = new TemporaryFolder();

    private MainModel model;
    private File tempFolderChild;

    @Before
    public void beforeEach() throws IOException {
        model = new MainModel();

        tempFolderRoot.newFile("000");
        tempFolderRoot.newFile("001");
        tempFolderRoot.newFile("002");

        tempFolderChild = tempFolderRoot.newFolder("009");
        new File(tempFolderChild, "010").createNewFile();
        new File(tempFolderChild, "011").createNewFile();

        model.updateEntries(tempFolderRoot.getRoot().getAbsolutePath());
    }

    @Test
    public void updateEntries_addsCorrectAmountOfEntries() {
        assertThat(model.getEntries().length).isEqualTo(4);
        assertThat(model.getEntries()[0].getName()).isEqualTo("000");
        assertThat(model.getEntries()[1].getName()).isEqualTo("001");
        assertThat(model.getEntries()[2].getName()).isEqualTo("002");
    }

    @Test
    public void toggleEntrySelected_entryNotSelected_makeEntrySelected() {
        model.toggleEntrySelectedAt(0);
        assertThat(model.getEntries()[0].isSelected()).isTrue();
    }

    @Test
    public void toggleEntrySelected_entrySelected_makeEntryNotSelected() {
        model.getEntries()[0].setSelected(true);
        model.toggleEntrySelectedAt(0);
        assertThat(model.getEntries()[0].isSelected()).isFalse();
    }

    @Test
    public void toggleEntrySelected_increaseSelectedEntriesCount() {
        model.toggleEntrySelectedAt(0);
        assertThat(model.getSelectedEntriesCount()).isEqualTo(1);
    }

    @Test
    public void toggleEntrySelected_decreaseSelectedEntriesCount() {
        model.toggleEntrySelectedAt(0);
        model.toggleEntrySelectedAt(0);
        assertThat(model.getSelectedEntriesCount()).isEqualTo(0);
    }

    @Test
    public void clearSelection_setIsSelectedToFalseOnSelectedEntries() {
        model.toggleEntrySelectedAt(0);
        model.toggleEntrySelectedAt(1);
        model.toggleEntrySelectedAt(2);

        assertThat(model.getEntries()[0].isSelected()).isTrue();
        assertThat(model.getEntries()[1].isSelected()).isTrue();
        assertThat(model.getEntries()[2].isSelected()).isTrue();

        model.clearSelections();

        assertThat(model.getEntries()[0].isSelected()).isFalse();
        assertThat(model.getEntries()[1].isSelected()).isFalse();
        assertThat(model.getEntries()[2].isSelected()).isFalse();
    }

    @Test
    public void clearSelection_setSelectedEntriesCountToZero() {
        model.toggleEntrySelectedAt(0);
        model.toggleEntrySelectedAt(1);
        model.toggleEntrySelectedAt(2);

        assertThat(model.getSelectedEntriesCount()).isEqualTo(3);

        model.clearSelections();

        assertThat(model.getSelectedEntriesCount()).isEqualTo(0);
    }

    @Test
    public void navigateForward_updateEntries() {
        model.navigateForward(3);
        assertThat(model.getEntries().length).isEqualTo(2);
        assertThat(model.getEntries()[0].getName()).isEqualTo("010");
        assertThat(model.getEntries()[1].getName()).isEqualTo("011");
    }

    @Test
    public void navigateBack_updateEntries() {
        model.navigateForward(3);

        assertThat(model.getEntries().length).isEqualTo(2);
        assertThat(model.getEntries()[0].getName()).isEqualTo("010");
        assertThat(model.getEntries()[1].getName()).isEqualTo("011");

        model.navigateBack();

        assertThat(model.getEntries().length).isEqualTo(4);
        assertThat(model.getEntries()[0].getName()).isEqualTo("000");
        assertThat(model.getEntries()[1].getName()).isEqualTo("001");
        assertThat(model.getEntries()[2].getName()).isEqualTo("002");
    }

    @Test
    public void isAtRoot_atRoot_returnTrue() {
        assertThat(model.isAtRoot()).isTrue();
    }

    @Test
    public void isAtRoot_notAtRoot_returnFalse() {
        model.navigateForward(3);
        assertThat(model.isAtRoot()).isFalse();
    }

    @Test
    public void deleteSelectedItems_noEntriesSelected_notDeleteAnyEntries() {
        model.deleteSelectedItems();
        assertThat(tempFolderRoot.getRoot().listFiles().length).isEqualTo(4);
    }

    @Test
    public void deleteSelectedItems_fileEntrySelected_deleteSelectedEntry() {
        model.toggleEntrySelectedAt(0);
        model.deleteSelectedItems();
        assertThat(new File(tempFolderRoot.getRoot(), "000").exists()).isFalse();
    }

    @Test
    public void getCurrentPath_returnCurrentPath() {
        assertThat(model.getCurrentPath()).isEqualTo(tempFolderRoot.getRoot().getAbsolutePath());
    }

    @Test
    public void getCurrentPath_navigateForward_change() {
        model.navigateForward(3);
        assertThat(model.getCurrentPath()).isEqualTo(tempFolderChild.getAbsolutePath());
    }
}
