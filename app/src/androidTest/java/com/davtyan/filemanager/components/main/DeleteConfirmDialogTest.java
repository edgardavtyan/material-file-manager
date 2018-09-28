package com.davtyan.filemanager.components.main;


import android.support.test.rule.ActivityTestRule;

import com.davtyan.filemanager.R;
import com.davtyan.filemanager.lib_test.base.BaseTest;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class DeleteConfirmDialogTest extends BaseTest {
    @Rule public final ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private MainPresenter presenter;
    private DeleteConfirmDialog deleteConfirmDialog;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        runOnMainSync(() -> {
            presenter = mock(MainPresenter.class);
            deleteConfirmDialog = new DeleteConfirmDialog(activityRule.getActivity(), presenter);
        });
    }

    @Test
    public void show_displayDialog() {
        runOnMainSync(deleteConfirmDialog::show);
        onView(withText(R.string.dialog_delete_title)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_delete_message)).check(matches(isDisplayed()));
    }

    @Test
    public void positiveButtonClick_callPresenter() {
        runOnMainSync(deleteConfirmDialog::show);
        onView(withId(android.R.id.button1)).perform(click());
        verify(presenter).onDeleteDialogConfirmed();
    }

    @Test
    public void negativeButtonClick_notCallPresenter() {
        runOnMainSync(deleteConfirmDialog::show);
        onView(withId(android.R.id.button2)).perform(click());
        verify(presenter, never()).onDeleteDialogConfirmed();
    }
}
