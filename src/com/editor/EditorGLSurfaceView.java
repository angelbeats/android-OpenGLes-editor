package com.editor;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import com.editor.EditorModel.ShaderManager;
import com.editor.editorRender.SceneRenderer;

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
        initView();
    }
    public EditorGLSurfaceView(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView();
    }
    private void initView(){
        this.setEGLContextClientVersion(2);

        _sceneRenderer = new SceneRenderer(this);
        setRenderer(_sceneRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


}

