package com.CornelCocioaba.Pixti.Input;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.CornelCocioaba.Pixti.Utils.Debug;
import com.CornelCocioaba.Pixti.Utils.Pool.IPoolObjectFactory;
import com.CornelCocioaba.Pixti.Utils.Pool.Pool;

public class MultiTouchHandler implements TouchHandler {
	private static final int MAX_TOUCHPOINTS = 10;

	private boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
	private int[] touchX = new int[MAX_TOUCHPOINTS];
	private int[] touchY = new int[MAX_TOUCHPOINTS];

	private Pool<TouchEvent> touchEventPool;

	private List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	private List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	

	private int h = 720;

	/**
	 * Use this constructor when you are registering the touch listener yourself Use View.setOnTouchListener(your
	 * instance of MultiTouchHandler)
	 */
	public MultiTouchHandler() {
		this(null);
	}

	public MultiTouchHandler(View view) {
		this(view, 1, 1);
	}

	public MultiTouchHandler(View view, float scaleX, float scaleY) {
		if (view != null) {
			view.setOnTouchListener(this);
		}
		Debug.log(h);
		
		IPoolObjectFactory<TouchEvent> factory = new IPoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			int pointerId = event.getPointerId(pointerIndex);
			TouchEvent touchEvent;

			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				touchEvent.pointer = pointerId;
				touchEvent.x = touchX[pointerId] = (int) event.getX(pointerIndex);
				touchEvent.y = touchY[pointerId] = h - (int) event.getY(pointerIndex);
				isTouched[pointerId] = true;
				touchEventsBuffer.add(touchEvent);
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_UP;
				touchEvent.pointer = pointerId;
				touchEvent.x = touchX[pointerId] = (int) event.getX(pointerIndex);
				touchEvent.y = touchY[pointerId] = h - (int) event.getY(pointerIndex);
				isTouched[pointerId] = false;
				touchEventsBuffer.add(touchEvent);
				break;

			case MotionEvent.ACTION_MOVE:
				int pointerCount = event.getPointerCount();
				for (int i = 0; i < pointerCount; i++) {
					pointerIndex = i;
					pointerId = event.getPointerId(pointerIndex);

					touchEvent = touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DRAGGED;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[pointerId] = (int) event.getX(pointerIndex);
					touchEvent.y = touchY[pointerId] = h - (int) event.getY(pointerIndex);
					touchEventsBuffer.add(touchEvent);
				}
				break;
			}

			return true;
		}
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if (pointer < 0 || pointer >= 20)
				return false;
			else
				return isTouched[pointer];
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			if (pointer < 0 || pointer >= 20)
				return 0;
			else
				return touchX[pointer];
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			if (pointer < 0 || pointer >= 20)
				return 0;
			else
				return touchY[pointer];
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			int len = touchEvents.size();
			for (int i = 0; i < len; i++)
				touchEventPool.recycle(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}

}
