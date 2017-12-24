package com.davtyan.filemanager.lib_test.assertions;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.test.InstrumentationRegistry;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import org.assertj.core.api.AbstractAssert;

public class ImageViewAssert extends AbstractAssert<ImageViewAssert, ImageView> {
	private final Context context;

	public ImageViewAssert(ImageView actual) {
		super(actual, ImageViewAssert.class);
		context = InstrumentationRegistry.getTargetContext();
	}

	@SuppressWarnings("UnusedReturnValue")
	public ImageViewAssert hasImageResource(@DrawableRes int drawableRes) {
	    actual.setColorFilter(0);
		Bitmap expectedBitmap = DrawableAssert.getBitmap(ContextCompat.getDrawable(context, drawableRes));
		Bitmap actualBitmap = DrawableAssert.getBitmap(actual.getDrawable());
		String drawableId = context.getResources().getResourceEntryName(drawableRes);
		String errorMessage = "\nExpecting to have resource with id='%s'\n";

		isNotNull();
		if (!expectedBitmap.sameAs(actualBitmap))
			failWithMessage(errorMessage, drawableId);

		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public ImageViewAssert hasImageBitmap(Bitmap expectedBitmap) {
		Bitmap actualBitmap = DrawableAssert.getBitmap(actual.getDrawable());
		String errorMessage = "\nExpecting bitmap to be\n<%s>\nbut was:\n<%s>\n";

		isNotNull();
		if (!expectedBitmap.sameAs(actualBitmap))
			failWithMessage(errorMessage, expectedBitmap, actualBitmap);

		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public ImageViewAssert hasImageAlpha(int expectedAlpha) {
		int actualAlpha = actual.getImageAlpha();
		String errorMessage = "\nExpected ImageAlpha to be <%s> but was <%s>\n";

		isNotNull();
		if (expectedAlpha != actualAlpha) {
			failWithMessage(errorMessage, expectedAlpha, actualAlpha);
		}

		return this;
	}
}
