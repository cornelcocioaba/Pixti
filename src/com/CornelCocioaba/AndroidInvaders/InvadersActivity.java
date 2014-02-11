package com.CornelCocioaba.AndroidInvaders;

import android.os.Bundle;
import android.view.KeyEvent;

import com.CornelCocioaba.Pixti.Core.GameActivity;

public class InvadersActivity extends GameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mEngine.setStartScene(new MainMenuScene(mEngine));
//		mEngine.setStartScene(new InvadersScene(mEngine));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mEngine.getCurrentScene() instanceof MainMenuScene)
				return super.onKeyDown(keyCode, event);
			else{
				return true;
			}
		}
		else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
