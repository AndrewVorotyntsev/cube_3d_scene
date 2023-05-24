package com.example.acube;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(new MyRenderer(this));
        setContentView(mGLView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}