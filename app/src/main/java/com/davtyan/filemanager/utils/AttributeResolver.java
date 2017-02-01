package com.davtyan.filemanager.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.util.TypedValue;

public class AttributeResolver {
	private final Context context;

	public AttributeResolver(Context context) {
		this.context = context;
	}

	public Drawable getDrawable(int attrId) {
		TypedArray attr = getAttribute(attrId);
		return attr.getDrawable(0);
	}

	public int getDimen(int attrId) {
		TypedArray attr = getAttribute(attrId);
		return attr.getDimensionPixelSize(0, 0);
	}

	public int getColor(@AttrRes int attrId) {
		TypedArray attr = getAttribute(attrId);
		return attr.getColor(0, 0);
	}

	private TypedArray getAttribute(int attrId) {
		TypedValue typedValue = new TypedValue();
		return context.obtainStyledAttributes(typedValue.data, new int[]{attrId});
	}
}
