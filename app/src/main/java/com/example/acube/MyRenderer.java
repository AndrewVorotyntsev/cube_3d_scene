package com.example.acube;

import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glEnable;

import android.content.Context;
import android.opengl.GLSurfaceView;

import android.opengl.GLES20;
import android.opengl.Matrix;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private Cube mCubeBox, mCubeWall, mCubeGround;
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int boxTexture, bricksTexture, groundTexture;
    public MyRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Заполнить фон цветом
        GLES20.glClearColor(0.52f,0.80f,0.92f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        boxTexture = TextureUtils.loadTextureCube(context, new int[]{R.drawable.box, R.drawable.box,
                R.drawable.box, R.drawable.box,
                R.drawable.box, R.drawable.box});

        bricksTexture = TextureUtils.loadTextureCube(context, new int[]{R.drawable.bricks, R.drawable.bricks,
                R.drawable.bricks, R.drawable.bricks,
                R.drawable.bricks, R.drawable.bricks});

        groundTexture = TextureUtils.loadTextureCube(context, new int[]{R.drawable.ground, R.drawable.ground,
                R.drawable.ground, R.drawable.ground,
                R.drawable.ground, R.drawable.ground});

        mCubeBox = new Cube(boxTexture);
        mCubeWall = new Cube(bricksTexture);
        mCubeGround = new Cube(groundTexture);

        createViewMatrix();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        Matrix.translateM(mMVPMatrix, 0, -2.0f, 0.0f, 0.0f);
        mCubeBox.draw(mMVPMatrix);
        Matrix.translateM(mMVPMatrix, 0, 2.0f, 0.0f, 0.0f);
        mCubeBox.draw(mMVPMatrix);
        Matrix.translateM(mMVPMatrix, 0, -2.0f, 2.0f, 0.0f);
        mCubeBox.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 0.0f, 0.0f, -2.0f);

        Matrix.translateM(mMVPMatrix, 0, -2.0f, 0.0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 0.0f, -2.0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);


        Matrix.translateM(mMVPMatrix, 0, 0.0f, 0.0f, 0.0f);
        Matrix.translateM(mMVPMatrix, 0, 2.0f, 0.0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 0.0f, 2.0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 2.0f, .0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 2.0f, .0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 0.0f, -2.0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 2.0f, 0.0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 0.0f, 2.0f, 0.0f);
        mCubeWall.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 0f, 0.0f, 0.0f);

        Matrix.translateM(mMVPMatrix, 0, 0f, -4.0f, 2.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, -2f, 0.0f, 0.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, -2f, 0.0f, 0.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, -2f, 0.0f, 0.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, -2f, 0.0f, 0.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 0f, 0.0f, 2.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 2f, 0.0f, 0.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 2f, 0.0f, 0.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 2f, 0.0f, 0.0f);
        mCubeGround.draw(mMVPMatrix);

        Matrix.translateM(mMVPMatrix, 0, 2f, 0.0f, 0.0f);
        mCubeGround.draw(mMVPMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
    }

    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 12;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 4;
        float eyeZ = 7.7f;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }
}
