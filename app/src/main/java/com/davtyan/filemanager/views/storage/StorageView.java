package com.davtyan.filemanager.views.storage;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StorageView extends LinearLayout {
    @BindView(R.id.icon) ImageView iconView;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.space_bar) ProgressBar spaceBarView;
    @BindView(R.id.space_text) TextView spaceTextView;

    public StorageView(Context context) {
        super(context);
        init();
    }

    public StorageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StorageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setIcon(@DrawableRes int drawableId) {
        iconView.setImageResource(drawableId);
    }

    public void setTitle(@StringRes int stringId) {
        titleView.setText(getResources().getString(stringId));
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setSpace(long freeSize, long totalSize) {
        double freeGB = freeSize / 1000000000.0;
        double totalGB = totalSize / 1000000000.0;
        double usedGB = totalGB - freeGB;
        int percentage = (int) (usedGB / totalGB * 100);
        String spaceText = getResources().getString(R.string.storage_space_text, freeGB, totalGB, percentage);
        spaceTextView.setText(spaceText);
        spaceBarView.setProgress((int) (usedGB / totalGB * 1000));
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_storage, this, true);
        ButterKnife.bind(this);
    }
}
