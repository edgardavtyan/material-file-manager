package com.davtyan.filemanager.lib_test.assertions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import static org.assertj.core.api.Assertions.assertThat;

public class DrawableAssert {

    public static void assertDrawablesEqual(Context context, Drawable actual, @DrawableRes int drawableId) {
        Bitmap actualBitmap = getBitmap(actual);
        Bitmap expectedBitmap = getBitmap(ContextCompat.getDrawable(context, drawableId));
        assertThat(actualBitmap.sameAs(expectedBitmap)).isTrue();
    }

    public static void assertDrawablesNotEqual(Context context, Drawable actual, @DrawableRes int drawableId) {
        Bitmap actualBitmap = getBitmap(actual);
        Bitmap expectedBitmap = getBitmap(ContextCompat.getDrawable(context, drawableId));
        assertThat(actualBitmap.sameAs(expectedBitmap)).isFalse();
    }

    public static Bitmap getBitmap(Drawable drawable) {
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
