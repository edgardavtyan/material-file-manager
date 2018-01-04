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
    @BindView(R.id.error_neverAskAgain) ScrollView neverAskAgainErrorView;
    @BindView(R.id.error_denied) LinearLayout deniedErrorView;
    @BindView(R.id.link_goto_settings) TextView gotoSettingsLinkView;
    @BindView(R.id.link_request) TextView requestPermissionLinkView;

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
    private final View.OnClickListener onRequestPermissionClickListener
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
        requestPermissionLinkView.setOnClickListener(onRequestPermissionClickListener);
    }

    public void showNeverAskAgainError() {
        neverAskAgainErrorView.setVisibility(View.VISIBLE);
    }

    public void showDeniedError() {
        deniedErrorView.setVisibility(View.VISIBLE);
    }

    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        storagePermissionRequest.onRequestPermissionResult(requestCode, grantResults);
    }
}
