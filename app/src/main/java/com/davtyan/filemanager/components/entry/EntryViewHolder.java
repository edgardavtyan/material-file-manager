package com.davtyan.filemanager.components.entry;

import android.view.View;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryViewHolder extends EntryMvp.ViewHolder {
    @BindView(R.id.title) TextView titleView;

    public EntryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setTitle(String title) {
        titleView.setText(title);
    }
}
