package com.CornelCocioaba.AndroidInvaders;

import android.os.Bundle;

import com.CornelCocioaba.Pixti.System.GameActivity;

public class InvadersActivity extends GameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mEngine.setCurrentScene(new InvadersScene(this));
	}
}
