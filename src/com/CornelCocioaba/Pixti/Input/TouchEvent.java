package com.CornelCocioaba.Pixti.Input;

import android.view.MotionEvent;

public class TouchEvent {

	public static final int TOUCH_DOWN = MotionEvent.ACTION_DOWN;
	public static final int TOUCH_UP = MotionEvent.ACTION_UP;
	public static final int TOUCH_DRAGGED = MotionEvent.ACTION_MOVE;
	public static final int TOUCH_CANCEL = MotionEvent.ACTION_CANCEL;
	public static final int TOUCH_OUTSIDE = MotionEvent.ACTION_OUTSIDE;

	public int type;
	public int x, y;
	public int pointer;
}
