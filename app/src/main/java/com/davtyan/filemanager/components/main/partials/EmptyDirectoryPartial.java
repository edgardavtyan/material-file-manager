package com.davtyan.filemanager.components.main.partials;

import android.view.View;
import android.widget.LinearLayout;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.components.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmptyDirectoryPartial {
    @BindView(R.id.empty_directory_msg) LinearLayout emptyDirectoryView;

    public EmptyDirectoryPartial(MainActivity activity) {
        ButterKnife.bind(this, activity);
    }

    public void show() {
        emptyDirectoryView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        emptyDirectoryView.setVisibility(View.GONE);

    }
}
