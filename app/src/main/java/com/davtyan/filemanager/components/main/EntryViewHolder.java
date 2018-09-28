package com.davtyan.filemanager.components.main;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.root) LinearLayout root;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.icon_wrapper) FrameLayout iconWrapperView;
    @BindView(R.id.icon_main) ImageView iconView;
    @BindView(R.id.icon_selected) ImageView selectedIconView;
    @BindView(R.id.button_menu) ImageButton menuButtonView;

    private final MainPresenter presenter;

    private final Drawable originalRootBackground;

    @SuppressWarnings("FieldCanBeLocal")
    private final View.OnClickListener onIconWrapperClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onEntryToggleSelected(getAdapterPosition());
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final View.OnClickListener onEntryClickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onEntryClick(getAdapterPosition());
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final View.OnLongClickListener onEntryLongClickListener
            = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            presenter.onEntryToggleSelected(getAdapterPosition());
            return true;
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final PopupMenu.OnMenuItemClickListener onMenuItemClickListener
            = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuitem_delete:
                    presenter.onDeleteEntryClicked(getAdapterPosition());
                    return true;
                case R.id.menuitem_rename:
                    presenter.onRenameMenuItemClicked(getAdapterPosition());
                default:
                    return false;
            }
        }
    };

    public EntryViewHolder(View itemView, MainPresenter presenter) {
        super(itemView);
        this.presenter = presenter;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(onEntryClickListener);
        itemView.setOnLongClickListener(onEntryLongClickListener);
        iconWrapperView.setOnClickListener(onIconWrapperClickListener);
        originalRootBackground = root.getBackground();

        PopupMenu menu = new PopupMenu(itemView.getContext(), menuButtonView);
        menu.inflate(R.menu.menu_entry);
        menu.setOnMenuItemClickListener(onMenuItemClickListener);
        menuButtonView.setOnClickListener(v -> menu.show());
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
            root.setBackground(originalRootBackground);
            selectedIconView.setVisibility(View.INVISIBLE);
            iconView.setVisibility(View.VISIBLE);
        }
    }
}
