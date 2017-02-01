package com.davtyan.filemanager.test_lib.assertions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
		Bitmap expectedBitmap = getBitmap(ContextCompat.getDrawable(context, drawableRes));
		Bitmap actualBitmap = getBitmap(actual.getDrawable());
		String drawableId = context.getResources().getResourceEntryName(drawableRes);
		String errorMessage = "\nExpecting to have resource with id='%s'\n";

		isNotNull();
		if (!expectedBitmap.sameAs(actualBitmap))
			failWithMessage(errorMessage, drawableId);

		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public ImageViewAssert hasImageBitmap(Bitmap expectedBitmap) {
		Bitmap actualBitmap = getBitmap(actual.getDrawable());
		String errorMessage = "\nExpecting bitmap to be\n<%s>\nbut was:\n<%s>\n";

		isNotNull();
		if (!expectedBitmap.sameAs(actualBitmap))
			failWithMessage(errorMessage, expectedBitmap, actualBitmap);

		return this;
	}

	private Bitmap getBitmap(Drawable drawable) {
		Bitmap result;
		if (drawable instanceof BitmapDrawable) {
			result = ((BitmapDrawable) drawable).getBitmap();
		} else {
			int width = drawable.getIntrinsicWidth();
			int height = drawable.getIntrinsicHeight();
			// Some drawables have no intrinsic width - e.g. solid colours.
			if (width <= 0) {
				width = 1;
			}
			if (height <= 0) {
				height = 1;
			}

			result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
		}
		return result;
	}
}
