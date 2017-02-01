package com.davtyan.filemanager.test_lib.assertions;

import android.content.Intent;

import org.assertj.core.api.AbstractAssert;

public class IntentAssert extends AbstractAssert<IntentAssert, Intent> {
	public IntentAssert(Intent actual) {
		super(actual, IntentAssert.class);
	}

	public IntentAssert hasClass(Class clazz) {
		String expectedClass = clazz.getName();
		String actualClass = actual.getComponent().getClassName();
		String errorMessage = "\nExpecting intent class to be\n<%s>\nbut was:\n<%s>\n";

		isNotNull();
		if (!expectedClass.equals(actualClass))
			failWithMessage(errorMessage, expectedClass, actualClass);
		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public IntentAssert hasExtraString(String extraName, String extraValue) {
		String actualExtra = actual.getStringExtra(extraName);
		String errorMessage = "\nExpecting extra <%s> to have value:\n<%s>\nbut got:\n<%s>\n";

		isNotNull();
		if (!extraValue.equals(actualExtra))
			failWithMessage(errorMessage, extraName, extraValue, actualExtra);
		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public IntentAssert hasExtraInt(String extraName, int extraValue) {
		int actualExtra = actual.getIntExtra(extraName, -1);
		String errorMessage = "\nExpecting extra <%s> to have value:\n<%s>\nbut got:\n<%s>\n";

		isNotNull();
		if (extraValue != actualExtra)
			failWithMessage(errorMessage, extraName, extraValue, actualExtra);
		return this;
	}
}
