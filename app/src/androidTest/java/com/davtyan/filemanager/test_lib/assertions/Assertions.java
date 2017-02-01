package com.davtyan.filemanager.test_lib.assertions;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Constructor;

public final class Assertions {
	public static <T> ConstructorAssert<T> assertThat(Constructor<T> constructor) {
		return new ConstructorAssert<>(constructor);
	}

	public static IntentAssert assertThat(Intent intent) {
		return new IntentAssert(intent);
	}

	public static ImageViewAssert assertThat(ImageView imageView) {
		return new ImageViewAssert(imageView);
	}

	public static ImageViewAssert assertThat(View view, @IdRes int id) {
		ImageView imageView = (ImageView) view.findViewById(id);
		return new ImageViewAssert(imageView);
	}

	public static ImageViewAssert assertThat(Activity activity, @IdRes int id) {
		ImageView imageView = (ImageView) activity.findViewById(id);
		return new ImageViewAssert(imageView);
	}
}
