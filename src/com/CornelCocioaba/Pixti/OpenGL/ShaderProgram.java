package com.CornelCocioaba.Pixti.OpenGL;

import static android.opengl.GLES20.glUseProgram;
import android.content.Context;

import com.CornelCocioaba.Pixti.Utils.TextResourceReader;

public class ShaderProgram {
	// Uniform constants
	public static final String U_MATRIX = "u_Matrix";
	public static final String U_TEXTURE_UNIT = "u_TextureUnit";
	public static final String U_COLOR = "u_Color";

	// Attribute constants
	protected static final String A_POSITION = "a_Position";
	protected static final String A_COLOR = "a_Color";
	protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

	// Varying
	protected static final String V_COLOR = "v_Color";
	protected static final String V_TEXTURE_COORDINATES = "v_TextureCoordinates";

	public final int program;

	protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
		this(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
				TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
	}

	protected ShaderProgram(String vertexShader, String fragmentShader) {
		program = ShaderHelper.buildProgram(vertexShader, fragmentShader);
	}

	public void useProgram() {
		glUseProgram(program);
	}
}
