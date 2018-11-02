package com.davtyan.filemanager.components.main.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class EditTextDialog {
    @BindView(R.id.edittext) EditText editTextView;
    @BindView(R.id.error) TextView existsErrorView;

    private final Context context;
    private final AlertDialog dialog;

    public EditTextDialog(Context context) {
        this.context = context;

        View view = View.inflate(context, R.layout.dialog_edittext, null);
        ButterKnife.bind(this, view);

        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(getTitleRes())
                .setPositiveButton(getPositiveRes(), null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    @StringRes
    protected abstract int getTitleRes();

    @StringRes
    protected abstract int getPositiveRes();

    @StringRes
    protected abstract int getErrorRes();

    protected String getText() {
        return editTextView.getText().toString();
    }

    protected void showError(Object... args) {
        existsErrorView.setText(context.getString(getErrorRes(), args));
        existsErrorView.setVisibility(View.VISIBLE);
    }

    protected void onPositiveButtonClick() {
    }

    public void show() {
        existsErrorView.setVisibility(View.INVISIBLE);
        editTextView.setText(null);

        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> onPositiveButtonClick());
    }

    public void close() {
        dialog.dismiss();
    }
}
