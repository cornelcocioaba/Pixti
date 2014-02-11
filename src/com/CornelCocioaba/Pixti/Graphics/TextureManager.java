package com.CornelCocioaba.Pixti.Graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

public class TextureManager {

	private final Map<String, Texture> mTextures;
	private final Context mContext;

	public TextureManager(Context context) {
		mContext = context;
		mTextures = new HashMap<String, Texture>();
	}

	public Texture add(String filename) {
		Texture texture = new Texture(mContext, filename);
		mTextures.put(filename, texture);
		return texture;
	}

	public void remove(String filename) {
		mTextures.get(filename).unload();
		mTextures.remove(filename);
	}

	public Texture getTexture(String filename) {
		return mTextures.get(filename);
	}

	public void load(String filename) {
		mTextures.get(filename).load();
	}

	public void unload(String filename) {
		mTextures.get(filename).unload();
	}

	public void reload(String filename) {
		mTextures.get(filename).reload();
	}

	public void loadAll() {
		for (Entry<String, Texture> kv : mTextures.entrySet()) {
			kv.getValue().load();
		}
	}

	public void unloadAll() {
		for (Entry<String, Texture> kv : mTextures.entrySet()) {
			kv.getValue().unload();
		}
	}

	public void reloadAll() {
		for (Entry<String, Texture> kv : mTextures.entrySet()) {
			kv.getValue().reload();
		}
	}
}
