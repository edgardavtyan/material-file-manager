package com.davtyan.filemanager.components.entry;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener,
                   View.OnLongClickListener {

    @BindView(R.id.root) LinearLayout root;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.icon_main) ImageView iconView;
    @BindView(R.id.icon_selected) ImageView selectedIconView;

    private final EntryPresenter presenter;

    private Drawable rootNormalBackground;

    public EntryViewHolder(View itemView, EntryPresenter presenter) {
        super(itemView);
        this.presenter = presenter;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        ButterKnife.bind(this, itemView);
        rootNormalBackground = root.getBackground();
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setIsDirectory(boolean isDirectory) {
        if (isDirectory) {
            iconView.setImageResource(R.drawable.ic_directory);
        } else {
            iconView.setImageResource(R.drawable.ic_file);
        }
    }

    public void setIsSelected(boolean isSelected) {
        if (isSelected) {
            root.setBackgroundResource(R.color.selectModeBackground);
            selectedIconView.setVisibility(View.VISIBLE);
            iconView.setVisibility(View.INVISIBLE);
        } else {
            root.setBackground(rootNormalBackground);
            selectedIconView.setVisibility(View.INVISIBLE);
            iconView.setVisibility(View.VISIBLE);
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
