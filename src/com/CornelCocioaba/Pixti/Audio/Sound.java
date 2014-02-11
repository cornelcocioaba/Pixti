package com.CornelCocioaba.Pixti.Audio;

import android.media.SoundPool;

public class Sound implements ISound{

	int soundId;
    SoundPool soundPool;
    
    public Sound(SoundPool soundPool,int soundId) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    public void play(){
    	this.play(1);
    }
    
    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void unload() {
        soundPool.unload(soundId);
    }

}
