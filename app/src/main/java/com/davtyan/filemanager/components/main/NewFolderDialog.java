package com.davtyan.filemanager.components.main;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewFolderDialog {
    @BindView(R.id.edittext) EditText editTextView;
    @BindView(R.id.existsError) TextView existsErrorView;

    private final Context context;
    private final MainPresenter presenter;
    private final AlertDialog dialog;

    public NewFolderDialog(Context context, MainPresenter presenter) {
        this.context = context;
        this.presenter = presenter;

        View view = View.inflate(context, R.layout.dialog_new_folder, null);
        ButterKnife.bind(this, view);

        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.dialog_newFolder_title)
                .setPositiveButton(R.string.dialog_newFolder_action, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    public void show() {
        existsErrorView.setVisibility(View.INVISIBLE);
        editTextView.setText(null);

        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
              .setOnClickListener(v -> presenter.onNewFolderDialogConfirm(editTextView.getText().toString()));
    }

    public void close() {
        dialog.dismiss();
    }

    public void showExistsError() {
        existsErrorView.setText(context.getString(R.string.dialog_newFolder_existsError, editTextView.getText()));
        existsErrorView.setVisibility(View.VISIBLE);
    }
}
