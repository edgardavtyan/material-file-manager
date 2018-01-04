package com.davtyan.filemanager.components.main.partials;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainActivity;
import com.davtyan.filemanager.components.main.MainPresenter;
import com.davtyan.filemanager.components.main.StoragePermissionRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PermissionsPartial {
    @BindView(R.id.permission_storage_error_neverAskAgain) ScrollView storagePermissionNeverAskAgainErrorView;
    @BindView(R.id.permission_storage_error_denied) LinearLayout storagePermissionDeniedErrorView;
    @BindView(R.id.permission_storage_link_goto_settings) TextView gotoSettingsLinkView;
    @BindView(R.id.permission_storage_link_request) TextView requestStoragePermissionLinkView;

    private final MainActivity activity;
    private final MainPresenter presenter;
    private final StoragePermissionRequest storagePermissionRequest;

    @SuppressWarnings("FieldCanBeLocal")
    private final View.OnClickListener onGotoSettingsClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final View.OnClickListener onRequestStoragePermissionLinkClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onRequestStoragePermissionLinkClicked();
        }
    };

    public PermissionsPartial(
            MainActivity activity,
            MainPresenter presenter,
            StoragePermissionRequest storagePermissionRequest) {
        this.activity = activity;
        this.presenter = presenter;
        this.storagePermissionRequest = storagePermissionRequest;
        ButterKnife.bind(this, activity);

        gotoSettingsLinkView.setOnClickListener(onGotoSettingsClickListener);
        requestStoragePermissionLinkView.setOnClickListener(onRequestStoragePermissionLinkClickListener);
    }

    public void showNeverAskAgainError() {
        storagePermissionNeverAskAgainErrorView.setVisibility(View.VISIBLE);
    }

    public void showDeniedError() {
        storagePermissionDeniedErrorView.setVisibility(View.VISIBLE);
    }

    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        storagePermissionRequest.onRequestPermissionResult(requestCode, grantResults);
    }
}
