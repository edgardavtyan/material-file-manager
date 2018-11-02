package com.davtyan.filemanager.components.main.dialogs;

import android.content.Context;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainPresenter;

public class NewFolderDialog extends EditTextDialog {
    private final MainPresenter presenter;

    public NewFolderDialog(Context context, MainPresenter presenter) {
        super(context);
        this.presenter = presenter;
    }

    @Override
    protected int getTitleRes() { return R.string.dialog_newFolder_title; }

    @Override
    protected int getPositiveRes() { return R.string.dialog_newFolder_action; }

    @Override
    protected int getErrorRes() { return R.string.dialog_newFolder_existsError; }

    @Override
    public void onPositiveButtonClick() { presenter.onNewFolderDialogConfirm(getText()); }

    public void showExistsError() { showError(getText()); }
}
