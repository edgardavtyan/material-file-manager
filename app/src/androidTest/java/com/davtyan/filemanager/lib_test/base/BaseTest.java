package com.davtyan.filemanager.lib_test.base;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.v7.view.ContextThemeWrapper;

import com.davtyan.filemanager.R;

import org.junit.Before;

import static org.mockito.Mockito.spy;

public abstract class BaseTest {
    private ContextThemeWrapper context;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context = spy(new ContextThemeWrapper(appContext, R.style.AppTheme));
    }

    protected ContextThemeWrapper getContext() {
        return context;
    }
}
