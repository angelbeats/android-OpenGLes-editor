package com.editor;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-10-7
 * To change this template use File | Settings | File Templates.
 */
public class EditorGLSurfaceView extends GLSurfaceView{
    private SceneRenderer _sceneRenderer;

    public EditorGLSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        _sceneRenderer = new SceneRenderer();
        setRenderer(_sceneRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    public EditorGLSurfaceView(Context context, AttributeSet attrs) {
        super(context,attrs);
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

