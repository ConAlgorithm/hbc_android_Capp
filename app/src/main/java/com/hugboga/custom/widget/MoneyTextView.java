package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MoneyTextView extends TextView {
	
	private static volatile Typeface moneyFont;
	
	public MoneyTextView(Context context) {
		this(context, null);
	}
	
	public MoneyTextView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
}
