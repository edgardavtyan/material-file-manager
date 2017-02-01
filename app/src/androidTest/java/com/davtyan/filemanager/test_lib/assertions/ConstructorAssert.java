package com.davtyan.filemanager.test_lib.assertions;

import org.assertj.core.api.AbstractAssert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ConstructorAssert<T> extends AbstractAssert<ConstructorAssert<T>, Constructor<T>> {
	public ConstructorAssert(Constructor<T> actual) {
		super(actual, ConstructorAssert.class);
	}

	@SuppressWarnings("UnusedReturnValue")
	public ConstructorAssert isPrivate() {
		isNotNull();

		try {
			actual.setAccessible(true);
			actual.newInstance();

			if (!Modifier.isPrivate(actual.getModifiers())) {
				failWithMessage("\nExpecting constructor of %s to be private\n", actual.getName());
			}
		} catch (Exception e) {
			failWithMessage(
					"\nException occurred\nmessage: %s\ntrace:%s\n",
					e.getMessage(),
					e.getStackTrace());
		}

		return this;
	}
}
