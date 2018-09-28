package com.davtyan.filemanager.components.main;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeleteConfirmDialog {
    @BindView(R.id.message) TextView messageView;

    private final AlertDialog dialog;
    private final MainPresenter presenter;

    @SuppressWarnings("FieldCanBeLocal")
    private final DialogInterface.OnClickListener positiveButtonListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            presenter.onDeleteDialogConfirmed();
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final DialogInterface.OnClickListener negativeButtonListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            presenter.onDeleteDialogCanceled();
        }
    };

    public DeleteConfirmDialog(Context context, MainPresenter presenter) {
        this.presenter = presenter;
        View itemView = View.inflate(context, R.layout.dialog_delete_confirm, null);
        ButterKnife.bind(this, itemView);

        messageView.setText(R.string.dialog_delete_message);

        dialog = new AlertDialog.Builder(context)
                .setView(itemView)
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.dialog_delete_action, positiveButtonListener)
                .setNegativeButton(android.R.string.cancel, negativeButtonListener)
                .create();
    }

    public void show() {
        dialog.show();
    }
}
