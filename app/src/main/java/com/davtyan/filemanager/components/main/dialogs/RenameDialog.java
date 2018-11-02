package com.davtyan.filemanager.components.main.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RenameDialog {
    @BindView(R.id.edittext) EditText editTextView;

    private final AlertDialog dialog;

    public RenameDialog(Context context, MainPresenter presenter) {
        View view = View.inflate(context, R.layout.dialog_rename, null);
        ButterKnife.bind(this, view);

        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.dialog_rename_title)
                .setPositiveButton(
                        R.string.dialog_rename_action,
                        (d, w) -> presenter.onRenameDialogConfirm(editTextView.getText().toString()))
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    public void show(String text) {
        editTextView.setText(text);
        editTextView.setSelection(editTextView.getText().length());
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
