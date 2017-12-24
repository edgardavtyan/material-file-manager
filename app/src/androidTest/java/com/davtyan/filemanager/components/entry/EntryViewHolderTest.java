package com.davtyan.filemanager.components.entry;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davtyan.filemanager.lib_test.base.BaseTest;
import com.davtyan.filemanager.R;

import org.junit.Before;
import org.junit.Test;

import static com.davtyan.filemanager.lib_test.assertions.Assertions.assertThat;
import static com.davtyan.filemanager.lib_test.assertions.DrawableAssert.assertDrawablesEqual;
import static com.davtyan.filemanager.lib_test.assertions.DrawableAssert.assertDrawablesNotEqual;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EntryViewHolderTest extends BaseTest {
    private View itemView;
    private EntryPresenter presenter;
    private EntryViewHolder viewHolder;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        itemView = View.inflate(getContext(), R.layout.listitem_entry, null);
        presenter = mock(EntryPresenter.class);
        viewHolder = new EntryViewHolder(itemView, presenter);
    }

    @Test
    public void onEntryClick_notInSelectMode_callOnEntryClick() {
        when(presenter.isInSelectMode()).thenReturn(false);
        itemView.performClick();
        verify(presenter).onEntryClick(-1);
    }

    @Test
    public void onEntryClick_inSelectMode_toggleEntrySelected() {
        when(presenter.isInSelectMode()).thenReturn(true);
        itemView.performClick();
        verify(presenter).onEntryToggleSelected(-1);
    }

    @Test
    public void onEntryLongClick_toggleEntrySelected() {
        itemView.performLongClick();
        verify(presenter).onEntryToggleSelected(-1);
    }

    @Test
    public void onIconClick_toggleEntrySelected() {
        FrameLayout iconWrapper = (FrameLayout) itemView.findViewById(R.id.icon_wrapper);
        iconWrapper.performClick();
        verify(presenter).onEntryToggleSelected(-1);
    }

    @Test
    public void setTitle_setTitleViewText() {
        TextView titleView = (TextView) itemView.findViewById(R.id.title);
        viewHolder.setTitle("title");
        assertThat(titleView.getText()).isEqualTo("title");
    }

    @Test
    public void setIsDirectory_true_setDirectoryIcon() {
        ImageView iconView = (ImageView) itemView.findViewById(R.id.icon_main);
        viewHolder.setIsDirectory(true);
        assertThat(iconView).hasImageResource(R.drawable.ic_directory);
    }

    @Test
    public void setIsDirectory_false_setFileIcon() {
        ImageView iconView = (ImageView) itemView.findViewById(R.id.icon_main);
        viewHolder.setIsDirectory(false);
        assertThat(iconView).hasImageResource(R.drawable.ic_file);
    }

    @Test
    public void setIsSelected_true_displayIconAndBackground() {
        ImageView iconView = (ImageView) itemView.findViewById(R.id.icon_main);
        ImageView selectedIconView = (ImageView) itemView.findViewById(R.id.icon_selected);
        LinearLayout rootView = (LinearLayout) itemView.findViewById(R.id.root);
        viewHolder.setIsSelected(true);

        assertDrawablesEqual(getContext(), rootView.getBackground(), R.color.selectModeBackground);
        assertThat(selectedIconView.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(iconView.getVisibility()).isEqualTo(View.INVISIBLE);
    }

    @Test
    public void setIsSelected_false_hideIconAndBackground() {
        ImageView iconView = (ImageView) itemView.findViewById(R.id.icon_main);
        ImageView selectedIconView = (ImageView) itemView.findViewById(R.id.icon_selected);
        LinearLayout rootView = (LinearLayout) itemView.findViewById(R.id.root);
        viewHolder.setIsSelected(false);

        assertDrawablesNotEqual(getContext(), rootView.getBackground(), R.color.selectModeBackground);
        assertThat(selectedIconView.getVisibility()).isEqualTo(View.INVISIBLE);
        assertThat(iconView.getVisibility()).isEqualTo(View.VISIBLE);
    }
}
