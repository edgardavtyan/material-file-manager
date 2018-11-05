package com.davtyan.filemanager.components.main.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeleteConfirmDialog {
    @BindView(R.id.message) TextView messageView;

    private final AlertDialog dialog;
    private final MainPresenter presenter;

    public DeleteConfirmDialog(Context context, MainPresenter presenter) {
        this.presenter = presenter;
        View itemView = View.inflate(context, R.layout.dialog_delete_confirm, null);
        ButterKnife.bind(this, itemView);

        messageView.setText(R.string.dialog_delete_message);

        dialog = new AlertDialog.Builder(context)
                .setView(itemView)
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.dialog_delete_action, (d, w) -> onPositiveButtonClick())
                .setNegativeButton(android.R.string.cancel, (d, w) -> onNegativeButtonClick())
                .create();
    }

    private void onPositiveButtonClick() {
        presenter.onDeleteDialogConfirmed();
    }

    private void onNegativeButtonClick() {
        presenter.onDeleteDialogCanceled();
    }

    public void show() {
        dialog.show();
    }
}
