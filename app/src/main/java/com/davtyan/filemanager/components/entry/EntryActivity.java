package com.davtyan.filemanager.components.entry;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.data.Storage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryActivity extends AppCompatActivity {
    public static final String EXTRA_PATH = "extra_path";
    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.current_path) TextView currentPathView;
    @BindView(R.id.empty_directory_msg) LinearLayout emptyDirectoryView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appbar;
    private boolean deleteMenuEnabled;
    private EntryAdapter adapter;
    private EntryPresenter presenter;

    private Drawable originalAppBarBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        deleteMenuEnabled = false;

        EntryModel model = new EntryModel();
        presenter = new EntryPresenter(this, model);
        adapter = new EntryAdapter(this, presenter);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        ((SimpleItemAnimator) list.getItemAnimator()).setSupportsChangeAnimations(false);

        String path = getIntent().getStringExtra(EXTRA_PATH);
        presenter.onCreate(path);

        originalAppBarBackground = appbar.getBackground();
    }

    @Override
    public void onBackPressed() {
        presenter.onNavigateBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);

        MenuItem deleteMenuItem = menu.findItem(R.id.menuitem_delete);
        deleteMenuItem.setVisible(deleteMenuEnabled);
        deleteMenuItem.setEnabled(deleteMenuEnabled);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_delete:
                presenter.onDeleteMenuItemClicked();
                return true;
            default:
                return false;
        }
    }

    public void updateEntries(Storage[] entries) {
        adapter.updateEntries(entries);

        if (entries.length == 0) {
            list.setVisibility(View.GONE);
            emptyDirectoryView.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            emptyDirectoryView.setVisibility(View.GONE);
        }
    }

    public void updateViewSelectionAt(int position) {
        adapter.notifyItemChanged(position);
        deleteMenuEnabled = true;
        invalidateOptionsMenu();
    }

    public void clearSelections() {
        adapter.notifyDataSetChanged();
        deleteMenuEnabled = false;
        invalidateOptionsMenu();
    }

    public void setCurrentPath(String path) {
        currentPathView.setText(path);
    }

    public void close() {
        finish();
    }

    public void enterSelectMode(int entriesCount) {
        appbar.setBackgroundResource(R.color.selectMode);

        String selectModeTitle = getString(R.string.select_mode_title, entriesCount);
        toolbar.setTitle(selectModeTitle);
    }

    public void exitSelectMode() {
        appbar.setBackground(originalAppBarBackground);
        toolbar.setTitle(R.string.app_name);
    }
}
