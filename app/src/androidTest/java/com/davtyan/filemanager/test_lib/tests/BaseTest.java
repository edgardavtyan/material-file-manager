package com.davtyan.filemanager.test_lib.tests;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.v7.view.ContextThemeWrapper;

import com.davtyan.filemanager.R;

import org.junit.Before;

public class BaseTest {
    protected Context context;
    protected Instrumentation instrumentation;

    @Before
    public void beforeEach() {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        context = new ContextThemeWrapper(instrumentation.getTargetContext(), R.style.AppTheme);
    }

    @SuppressWarnings("unchecked")
    protected <T> T startActivity(Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return (T) instrumentation.startActivitySync(intent);
    }

    protected void runOnUiThread(Runnable runnable) {
        instrumentation.runOnMainSync(runnable);
    }
}
