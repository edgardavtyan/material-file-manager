package com.davtyan.filemanager.views.storage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.davtyan.filemanager.R;

import org.junit.Before;
import org.junit.Test;

import static com.davtyan.filemanager.test_lib.assertions.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class StorageViewTest {
    private StorageView storageView;

    @Before
    public void beforeEach() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.AppTheme);
        storageView = new StorageView(contextThemeWrapper);
    }

    @Test
    public void setIcon_setIconViewDrawableToGivenResource() {
        ImageView iconView = (ImageView) storageView.findViewById(R.id.icon);
        storageView.setIcon(R.drawable.ic_smartphone);
        assertThat(iconView).hasImageResource(R.drawable.ic_smartphone);
    }

    @Test
    public void setTitle_setTitleViewTextToGivenResource() {
        TextView titleView = (TextView) storageView.findViewById(R.id.title);
        storageView.setTitle(R.string.app_name);
        assertThat(titleView.getText()).isEqualTo("Material File Manager");
    }

    @Test
    public void setSpace_setSpaceTextViewTextToGivenValuesInGigaBytes() {
        TextView spaceTextView = (TextView) storageView.findViewById(R.id.space_text);
        storageView.setSpace(1363487549l, 3489764313l);
        assertThat(spaceTextView.getText()).isEqualTo("1.36 GB free of 3.49 GB");
    }

    @Test
    public void setSpace_setSpaceBarProgress() {
        ProgressBar spaceBarView = (ProgressBar) storageView.findViewById(R.id.space_bar);
        storageView.setSpace(32454567864l, 47454557764l);
        assertThat(spaceBarView.getProgress()).isEqualTo(683);
    }
}
