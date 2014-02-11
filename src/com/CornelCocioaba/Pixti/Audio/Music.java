package com.CornelCocioaba.Pixti.Audio;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Music implements IMusic, OnCompletionListener {
	private MediaPlayer mediaPlayer;
	private boolean prepared = false;

	public Music(AssetFileDescriptor assetFileDescritor) {
		try {
			mediaPlayer.setDataSource(assetFileDescritor.getFileDescriptor(), assetFileDescritor.getStartOffset(),
					assetFileDescritor.getLength());
			mediaPlayer.prepare();
			prepared = true;
			mediaPlayer.setOnCompletionListener(this);

		} catch (Exception e) {
			throw new RuntimeException("Couldn't load music");
		}
	}

	@Override
	public void play() {
		if (mediaPlayer.isPlaying())
			return;
		try {
			synchronized (this) {
				if (!prepared)
					mediaPlayer.prepare();
				mediaPlayer.start();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		mediaPlayer.stop();
		synchronized (this) {
			prepared = false;
		}
	}

	@Override
	public void pause() {
		mediaPlayer.pause();
	}

	@Override
	public void setLooping(boolean looping) {
		mediaPlayer.setLooping(looping);
	}

	@Override
	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
	}

	@Override
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	@Override
	public boolean isStopped() {
		return prepared;
	}

	@Override
	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}

	@Override
	public void dispose() {
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer.release();

		mediaPlayer = null;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		synchronized (this) {
			prepared = false;
		}
	}
}
