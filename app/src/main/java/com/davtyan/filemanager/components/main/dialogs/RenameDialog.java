package com.davtyan.filemanager.components.main.dialogs;

import android.content.Context;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainPresenter;

public class RenameDialog extends EditTextDialog {
    private final MainPresenter presenter;

    public RenameDialog(Context context, MainPresenter presenter) {
        super(context);
        this.presenter = presenter;
    }

    @Override
    protected int getTitleRes() { return R.string.dialog_rename_title; }

    @Override
    protected int getPositiveRes() { return R.string.dialog_rename_action; }

    @Override
    protected int getErrorRes() { return R.string.dialog_rename_existsError; }

    @Override
    protected void onPositiveButtonClick() { presenter.onRenameDialogConfirm(getText()); }

    public void show(String text) {
        show();
        setText(text);
    }

    public void showExistsError() {
        showError(getText());
    }
}
