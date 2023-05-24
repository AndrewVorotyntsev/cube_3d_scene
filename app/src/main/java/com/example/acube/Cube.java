package com.example.acube;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUseProgram;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Cube {
    private static final int COORDS_PER_VERTEX = 3;

    private static float vertices[] = {
            -1.0f,  1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,
            1.0f,  1.0f,  1.0f
    };

    private static float colors[] = {
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f
    };

    private static final short indices[] = {
            0, 1, 2, 0, 2, 3,
            3, 2, 6, 3, 6, 7,
            4, 5, 6, 4, 6, 7,
            0, 1, 5, 0, 5, 4,
            0, 3, 7, 0, 7, 4,
            1, 2, 6, 1, 6, 5
    };

    private final int program;
    private int positionHandle;

    private int uTextureUnitLocation;
    private int colorHandle;
    private int mvpMatrixHandle;

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer indexBuffer;
    private final FloatBuffer colorBuffer;

    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private int texture;
    public Cube(int texture) {
        this.texture = texture;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        indexBuffer = dlb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        ByteBuffer cb = ByteBuffer.allocateDirect(colors.length * 4);
        cb.order(ByteOrder.nativeOrder());
        colorBuffer = cb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);


        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        getLocations();
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

//-     positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture);

//-      uTextureUnitLocation = glGetUniformLocation(program, "u_TextureUnit");
        // юнит текстуры
        glUniform1i(uTextureUnitLocation, 0);
//-     colorHandle = GLES20.glGetAttribLocation(program, "aColor");
//      GLES20.glEnableVertexAttribArray(colorHandle);
//      GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

//-     mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }

//    private final String vertexShaderCode =
//            "uniform mat4 u_Matrix;" +
//                    "attribute vec4 a_Position;" +
//                    "attribute vec4 aColor;" +
//                    "varying vec4 vColor;" +
//                    "void main() {" +
//                    "  gl_Position = u_Matrix * a_Position;" +
//                    "  vColor = aColor;" +
//                    "}";
//
//    private final String fragmentShaderCode =
//            "precision mediump float;" +
//                    "varying vec4 vColor;" +
//                    "void main() {" +
//                    "  gl_FragColor = vColor;" +
//                    "}";

    private final String vertexShaderCode =
            "attribute vec4 a_Position;\n" +
                    "uniform mat4 u_Matrix;\n" +
                    "varying vec3 v_Position;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    v_Position = a_Position.xyz;\n" +
                    "    gl_Position = u_Matrix * a_Position;\n" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "\n" +
                    "uniform samplerCube u_TextureUnit;\n" +
                    "varying vec3 v_Position;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_FragColor = textureCube(u_TextureUnit, v_Position);\n" +
                    "}";


    private void getLocations() {
        positionHandle = GLES20.glGetAttribLocation(program, "a_Position");
        uTextureUnitLocation = glGetUniformLocation(program, "u_TextureUnit");
        colorHandle = GLES20.glGetAttribLocation(program, "aColor");
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "u_Matrix");
    }

    // Метод для загрузки шейдера
    private static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}