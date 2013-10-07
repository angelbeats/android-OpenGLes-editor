package com.editor;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.editor.EditorModel.Model;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-10-7
 * Time: ÏÂÎç3:30
 * To change this template use File | Settings | File Templates.
 */

public class SceneRenderer implements GLSurfaceView.Renderer
{

    CopyOnWriteArrayList<Model> _modelList;
    public boolean setModelList(CopyOnWriteArrayList<Model> arg_list ){

        return true;
    }
    @Override
    public void onDrawFrame(GL10 gl)
    {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        MatrixState.pushMatrix();

        MatrixState.setCamera(0,0,35 ,0f,0f,0f,0f,1.0f,0.0f);

        MatrixState.popMatrix();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 10, 100);

        MatrixState.setCamera(0,0,35 ,0f,0f,0f,0f,1.0f,0.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {

        GLES20.glClearColor(0.2f,0.0f,0.5f,1.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnable(GLES20.GL_CULL_FACE);

        MatrixState.setInitStack();

        MatrixState.setLightLocation(40, 10, 20);


    }


}