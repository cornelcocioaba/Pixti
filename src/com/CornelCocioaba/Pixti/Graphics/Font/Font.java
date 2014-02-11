package com.CornelCocioaba.Pixti.Graphics.Font;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.CornelCocioaba.Pixti.GameObject.Text;
import com.CornelCocioaba.Pixti.Graphics.Color;
import com.CornelCocioaba.Pixti.Graphics.Texture;
import com.CornelCocioaba.Pixti.Graphics.TextureRegion;
import com.CornelCocioaba.Pixti.Graphics.Font.programs.BatchTextProgram;
import com.CornelCocioaba.Pixti.Graphics.Font.programs.Program;

public class Font {

	public final static int CHAR_START = 32;
	public final static int CHAR_END = 126;
	public final static int CHAR_CNT = (((CHAR_END - CHAR_START) + 1) + 1);
	public final static int CHAR_NONE = 32;
	public final static int CHAR_UNKNOWN = (CHAR_CNT - 1);
	public final static int FONT_SIZE_MIN = 6;
	public final static int FONT_SIZE_MAX = 180;
	public final static int CHAR_BATCH_SIZE = 24;
	public static final String TAG = "GLTEXT";
	AssetManager assets;
	SpriteBatch batch;
	int fontPadX, fontPadY;
	float fontHeight;
	float fontAscent;
	float fontDescent;
	int textureId;
	int textureSize;
	Texture texture;
	TextureRegion textureRgn;
	float charWidthMax;
	float charHeight;
	final float[] charWidths;
	TextureRegion[] charRgn;
	int cellWidth, cellHeight;
	int rowCnt, colCnt;
	float scaleX, scaleY;
	float spaceX;
	private Program mProgram;
	private int mColorHandle;
	private int mTextureUniformHandle;

	public Font(Program program, AssetManager assets) {
		if (program == null) {
			program = new BatchTextProgram();
			program.init();
		}
		this.assets = assets;
		batch = new SpriteBatch(CHAR_BATCH_SIZE, program);
		charWidths = new float[CHAR_CNT];
		charRgn = new TextureRegion[CHAR_CNT];
		fontPadX = 0;
		fontPadY = 0;
		fontHeight = 0.0f;
		fontAscent = 0.0f;
		fontDescent = 0.0f;
		textureId = -1;
		textureSize = 0;
		charWidthMax = 0;
		charHeight = 0;
		cellWidth = 0;
		cellHeight = 0;
		rowCnt = 0;
		colCnt = 0;
		scaleX = 1.0f;
		scaleY = 1.0f;
		spaceX = 0.0f;
		mProgram = program;
		mColorHandle = GLES20.glGetUniformLocation(mProgram.getHandle(), "u_Color");
		mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram.getHandle(), "u_Texture");
	}

	public Font(AssetManager assets) {
		this(null, assets);
	}

	public boolean load(String file, int size, int padX, int padY) {

		fontPadX = padX;
		fontPadY = padY;
		Typeface tf = Typeface.createFromAsset(assets, file);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(size);
		paint.setColor(0xffffffff);
		paint.setTypeface(tf);
		Paint.FontMetrics fm = paint.getFontMetrics();
		fontHeight = (float) Math.ceil(Math.abs(fm.bottom) + Math.abs(fm.top));
		fontAscent = (float) Math.ceil(Math.abs(fm.ascent));
		fontDescent = (float) Math.ceil(Math.abs(fm.descent));
		char[] s = new char[2];
		charWidthMax = charHeight = 0;
		float[] w = new float[2];
		int cnt = 0;
		for (char c = CHAR_START; c <= CHAR_END; c++) {
			s[0] = c;
			paint.getTextWidths(s, 0, 1, w);
			charWidths[cnt] = w[0];
			if (charWidths[cnt] > charWidthMax)
				charWidthMax = charWidths[cnt];
			cnt++;
		}
		s[0] = CHAR_NONE;
		paint.getTextWidths(s, 0, 1, w);
		charWidths[cnt] = w[0];
		if (charWidths[cnt] > charWidthMax)
			charWidthMax = charWidths[cnt];
		cnt++;
		charHeight = fontHeight;
		cellWidth = (int) charWidthMax + (2 * fontPadX);
		cellHeight = (int) charHeight + (2 * fontPadY);
		int maxSize = cellWidth > cellHeight ? cellWidth : cellHeight;
		if (maxSize < FONT_SIZE_MIN || maxSize > FONT_SIZE_MAX)
			return false;
		if (maxSize <= 24)
			textureSize = 256;
		else if (maxSize <= 40)
			textureSize = 512;
		else if (maxSize <= 80)
			textureSize = 1024;
		else
			textureSize = 2048;
		Bitmap bitmap = Bitmap.createBitmap(textureSize, textureSize, Bitmap.Config.ALPHA_8);
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0x00000000);
		colCnt = textureSize / cellWidth;
		rowCnt = (int) Math.ceil((float) CHAR_CNT / (float) colCnt);
		float x = fontPadX;
		float y = (cellHeight - 1) - fontDescent - fontPadY;
		for (char c = CHAR_START; c <= CHAR_END; c++) {
			s[0] = c;
			canvas.drawText(s, 0, 1, x, y, paint);
			x += cellWidth;
			if ((x + cellWidth - fontPadX) > textureSize) {
				x = fontPadX;
				y += cellHeight;
			}
		}
		s[0] = CHAR_NONE;
		canvas.drawText(s, 0, 1, x, y, paint);
		texture = new Texture(bitmap);
		texture.load();
		textureId = texture.textureId;
		x = 0;
		y = 0;
		for (int c = 0; c < CHAR_CNT; c++) {
			charRgn[c] = new TextureRegion(texture, x, y, cellWidth - 1, cellHeight - 1);
			x += cellWidth;
			if (x + cellWidth > textureSize) {
				x = 0;
				y += cellHeight;
			}
		}

		textureRgn = new TextureRegion(texture, 0, 0, textureSize, textureSize);
		return true;
	}

	public void begin(float[] vpMatrix) {
		begin(1.0f, 1.0f, 1.0f, 1.0f, vpMatrix);
	}

	public void begin(float alpha, float[] vpMatrix) {
		begin(1.0f, 1.0f, 1.0f, alpha, vpMatrix);
	}

	public void begin(Color color, float[] vpMatrix) {
		begin(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), vpMatrix);
	}

	public void begin(float red, float green, float blue, float alpha, float[] vpMatrix) {
		initDraw(red, green, blue, alpha);
		batch.beginBatch(vpMatrix);
	}

	void initDraw(float red, float green, float blue, float alpha) {
		GLES20.glUseProgram(mProgram.getHandle());
		float[] color = { red, green, blue, alpha };
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		GLES20.glEnableVertexAttribArray(mColorHandle);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glUniform1i(mTextureUniformHandle, 0);
	}

	public void end() {
		batch.endBatch();
		GLES20.glDisableVertexAttribArray(mColorHandle);
	}

	public void draw(String text, float x, float y, float angle) {
		float chrHeight = cellHeight * scaleY;
		float chrWidth = cellWidth * scaleX;
		int len = text.length();
		x += (chrWidth / 2.0f) - (fontPadX * scaleX);
		y += (chrHeight / 2.0f) - (fontPadY * scaleY);
		float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, y, 0);
		Matrix.rotateM(modelMatrix, 0, angle, 0, 0, -1);
		float letterX, letterY;
		letterX = letterY = 0;
		for (int i = 0; i < len; i++) {
			int c = (int) text.charAt(i) - CHAR_START;
			if (c < 0 || c >= CHAR_CNT)
				c = CHAR_UNKNOWN;
			batch.drawSprite(letterX, letterY, chrWidth, chrHeight, charRgn[c], modelMatrix);
			letterX += (charWidths[c] + spaceX) * scaleX;
		}
	}

	public void draw(String text, float x, float y) {
		draw(text, x, y, 0);
	}

	public void draw(Text text) {
		this.draw(text.getText(), text.x, text.y, text.angle);
	}

	public float drawC(String text, float x, float y, float angle) {
		float len = getLength(text);
		draw(text, x - (len / 2.0f), y - (getCharHeight() / 2.0f), angle);
		return len;
	}

	public float drawC(String text, float x, float y) {
		float len = getLength(text);
		return drawC(text, x - (len / 2.0f), y - (getCharHeight() / 2.0f), 0);
	}

	public float drawCX(String text, float x, float y) {
		float len = getLength(text);
		draw(text, x - (len / 2.0f), y);
		return len;
	}

	public void drawCY(String text, float x, float y) {
		draw(text, x, y - (getCharHeight() / 2.0f));
	}

	public void setScale(float scale) {
		scaleX = scaleY = scale;
	}

	public void setScale(float sx, float sy) {
		scaleX = sx;
		scaleY = sy;
	}

	public float getScaleX() {
		return scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void unload() {
		texture.unload();
	}

	public void setSpace(float space) {
		spaceX = space;
	}

	public float getSpace() {
		return spaceX;
	}

	public float getLength(String text) {
		float len = 0.0f;
		int strLen = text.length();
		for (int i = 0; i < strLen; i++) {
			int c = (int) text.charAt(i) - CHAR_START;
			len += (charWidths[c] * scaleX);
		}
		len += (strLen > 1 ? ((strLen - 1) * spaceX) * scaleX : 0);
		return len;
	}

	public float getCharWidth(char chr) {
		int c = chr - CHAR_START;
		return (charWidths[c] * scaleX);
	}

	public float getCharWidthMax() {
		return (charWidthMax * scaleX);
	}

	public float getCharHeight() {
		return (charHeight * scaleY);
	}

	public float getAscent() {
		return (fontAscent * scaleY);
	}

	public float getDescent() {
		return (fontDescent * scaleY);
	}

	public float getHeight() {
		return (fontHeight * scaleY);
	}

	public void drawTexture(int x, int y, float[] vpMatrix) {
		initDraw(1.0f, 1.0f, 1.0f, 1.0f);
		batch.beginBatch(vpMatrix);
		float[] idMatrix = new float[16];
		Matrix.setIdentityM(idMatrix, 0);
		batch.drawSprite(x, y, textureSize, textureSize, textureRgn, idMatrix);
		batch.endBatch();
	}
}
