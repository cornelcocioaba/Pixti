package com.CornelCocioaba.Pixti.Graphics;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_REPEAT;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
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
	
	public static class TexParams {
		public int TEXTURE_TYPE = GL_TEXTURE_2D;
		public int MIN_FILTER = GL_NEAREST;
		public int MAX_FILTER = GL_LINEAR;
		public int WRAP_S = GL_REPEAT;
		public int WRAP_T = GL_REPEAT;
	}

	public Texture(Context context, int resourceId) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		
		mBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

		mWidth = mBitmap.getWidth();
		mHeight = mBitmap.getHeight();
	}
	
	public Texture(Context context, String assetPath){
		try{
			InputStream is = context.getAssets().open(assetPath);
			
			mBitmap = BitmapFactory.decodeStream(is);

			mWidth = mBitmap.getWidth();
			mHeight = mBitmap.getHeight();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public Texture(Bitmap bitmap){
		mBitmap = bitmap;
		mWidth = mBitmap.getWidth();
		mHeight = mBitmap.getHeight();
	}

	public int load() {
		final int[] textureObjectIds = new int[1];

		glGenTextures(1, textureObjectIds, 0);

		if (textureObjectIds[0] == 0) {
			if(Debug.ON){
				Log.w(TAG, "Could not generate texture");
			}
			return 0;
		}
		
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		//triliniar filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		
		//biliniar filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		
		GLUtils.texImage2D(GL_TEXTURE_2D, 0, mBitmap, 0);
		
		mBitmap.recycle();
		
		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		this.textureId = textureObjectIds[0];
		return textureObjectIds[0];
	}
}
