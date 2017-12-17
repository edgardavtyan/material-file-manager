package com.davtyan.filemanager.components.entry;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryViewHolder extends EntryMvp.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.root) LinearLayout root;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.icon) ImageView iconView;

    private final EntryMvp.Presenter presenter;

    private Drawable rootNormalBackground;

    public EntryViewHolder(View itemView, EntryMvp.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        ButterKnife.bind(this, itemView);
        rootNormalBackground = root.getBackground();
    }

    @Override
    public void setTitle(String title) {
        titleView.setText(title);
    }

    @Override
    public void setIsDirectory(boolean isDirectory) {
        if (isDirectory) {
            iconView.setImageResource(R.drawable.ic_directory);
        } else {
            iconView.setImageResource(R.drawable.ic_file);
        }
    }

    @Override
    public void setIsSelected(boolean isSelected) {
        if (isSelected) {
            root.setBackgroundResource(R.color.listitem_selected);
        } else {
            root.setBackground(rootNormalBackground);
        }
    }

    @Override
    public void onClick(View v) {
        if (presenter.isInSelectMode()) {
            presenter.onEntryToggleSelected(getAdapterPosition());
        } else {
            presenter.onEntryClick(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        presenter.onEntryToggleSelected(getAdapterPosition());
        return true;
    }
}
