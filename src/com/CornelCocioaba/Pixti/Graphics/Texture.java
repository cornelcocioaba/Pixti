package com.CornelCocioaba.Pixti.Graphics;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import com.CornelCocioaba.Pixti.Utils.Debug;

public class Texture {

	public static final String TAG = "Texture";

	public int mWidth;
	public int mHeight;
	public Bitmap mBitmap;
	public int textureId;
	private InputStream inputStream;
	private TextureParams textureParams = new TextureParams();

	public Texture(Context context, String assetPath) {
		try {
			inputStream = context.getAssets().open(assetPath);

			mBitmap = BitmapFactory.decodeStream(inputStream);

			mWidth = mBitmap.getWidth();
			mHeight = mBitmap.getHeight();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Texture(Bitmap bitmap) {
		mBitmap = bitmap;
		mWidth = mBitmap.getWidth();
		mHeight = mBitmap.getHeight();
	}

	public void setTextureParams(TextureParams params) {
		this.textureParams = params;
	}

	public int load() {
		final int[] textureObjectIds = new int[1];

		glGenTextures(1, textureObjectIds, 0);

		if (textureObjectIds[0] == 0) {
			if (Debug.ON) {
				Log.w(TAG, "Could not generate texture");
			}
			return 0;
		}

		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, textureParams.getWrapS());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, textureParams.getWrapT());

		// triliniar filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureParams.getMinFilter());

		// biliniar filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, textureParams.getMaxFilter());

		GLUtils.texImage2D(GL_TEXTURE_2D, 0, mBitmap, 0);

		mBitmap.recycle();

		// glGenerateMipmap(GL_TEXTURE_2D);
		// glBindTexture(GL_TEXTURE_2D, 0);

		this.textureId = textureObjectIds[0];
		return textureObjectIds[0];
	}

	public void unload() {
		glDeleteTextures(1, new int[] { textureId }, 0);
		textureId = -1;
	}

	public void reload() {
		mBitmap = BitmapFactory.decodeStream(inputStream);
		// unload();
		load();
	}
}
