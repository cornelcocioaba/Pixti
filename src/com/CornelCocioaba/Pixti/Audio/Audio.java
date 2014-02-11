package com.CornelCocioaba.Pixti.Audio;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

public class Audio {
	private final SoundPool soundPool;
	private final AssetManager assetManager;

	public Audio(Context context) {
		soundPool = new SoundPool(20, 3, 0);
		assetManager = context.getAssets();
	}

	public Sound createSound(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assetManager.openFd(filename);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new Sound(soundPool, soundId);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load sound '" + filename + "'");
		}
	}

	public Music createMusic(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assetManager.openFd(filename);
			return new Music(assetDescriptor);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load music '" + filename + "'");
		}
	}
}
