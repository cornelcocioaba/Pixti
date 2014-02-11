package com.CornelCocioaba.Pixti.Input;

public class KeyEvent {
	public static final int KEY_DOWN = 0;
	public static final int KEY_UP = 1;
	public static final int KEYCODE_BACK = android.view.KeyEvent.KEYCODE_BACK;

	public int type;
	public int keyCode;
	public char keyChar;

	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (type == KEY_DOWN)
			builder.append("key down, ");
		else
			builder.append("key up, ");
		builder.append(keyCode);
		builder.append(",");
		builder.append(keyChar);
		return builder.toString();
	}
}