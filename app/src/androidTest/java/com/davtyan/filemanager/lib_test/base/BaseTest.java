package com.davtyan.filemanager.lib_test.base;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.v7.view.ContextThemeWrapper;

import com.davtyan.filemanager.R;

import org.junit.Before;

import static org.mockito.Mockito.spy;

public abstract class BaseTest {
    private ContextThemeWrapper context;
    private Instrumentation instrumentation;

    @Before
    public void setUp() throws Exception {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        context = spy(new ContextThemeWrapper(instrumentation.getTargetContext(), R.style.AppTheme));
    }

    protected ContextThemeWrapper getContext() {
        return context;
    }

    protected void runOnMainSync(Runnable runnable) {
        instrumentation.runOnMainSync(runnable);
    }
}
