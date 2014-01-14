package com.CornelCocioaba.Pixti.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class BufferUtils {
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_SHORT = 2;

	public static final FloatBuffer createFloatBuffer(float[] floatData) {
		FloatBuffer buffer = ByteBuffer.allocateDirect(floatData.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer().put(floatData);
		buffer.position(0);

		return buffer;
	}

	public static final FloatBuffer createFloatBuffer(int nElements, int stride) {
		FloatBuffer buffer = ByteBuffer.allocateDirect(nElements * stride * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		buffer.position(0);

		return buffer;
	}

	public static final ShortBuffer createShortBuffer(short[] shortData) {
		ShortBuffer buffer = ByteBuffer.allocateDirect(shortData.length * BYTES_PER_SHORT)
				.order(ByteOrder.nativeOrder()).asShortBuffer().put(shortData);
		buffer.position(0);
		return buffer;
	}
}
