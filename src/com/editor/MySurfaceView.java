package com.editor;

import android.opengl.GLSurfaceView;

import android.view.MotionEvent;

import android.content.Context;

class MySurfaceView extends GLSurfaceView
{
    private SceneRenderer _sceneRenderer;

	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        _sceneRenderer = new SceneRenderer();
        setRenderer(_sceneRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) 
    {
        return true;
    }



}
