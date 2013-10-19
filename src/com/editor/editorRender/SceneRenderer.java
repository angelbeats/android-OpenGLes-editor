package com.editor.editorRender;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.editor.EditorModel.Model;
import com.editor.EditorModel.ModelFactory;
import com.editor.common.MatrixState;
import com.editor.common.SceneConstant;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-10-7
 * To change this template use File | Settings | File Templates.
 */

public class SceneRenderer implements GLSurfaceView.Renderer  ,editorRender
{

    private final static String _logTag="SceneRender";
    CopyOnWriteArrayList<Model> _modelList;
    //camera position
    private float _cameraX = SceneConstant.INIT_CAMERA_X;
    private float _cameraY = SceneConstant.INIT_CAMERA_Y;
    private float _cameraZ = SceneConstant.INIT_CAMERA_Z;

    //eye position
    private float _eyeX = SceneConstant.INIT_EYE_X;
    private float _eyeY = SceneConstant.INIT_EYE_Y;
    private float _eyeZ = SceneConstant.INIT_EYE_Z;

    //up
    private float _upX = SceneConstant.INIT_UP_X;
    private float _upY = SceneConstant.INIT_UP_Y;
    private float _upZ = SceneConstant.INIT_UP_Z;

    //light
    private float _lightX = SceneConstant.INIT_LIGHT_X;
    private float _lightY = SceneConstant.INIT_LIGHT_Y;
    private float _lightZ = SceneConstant.INIT_LIGHT_Z;

    //background color
    private float _backgroundR = SceneConstant.INIT_COLOR_R;
    private float _backgroundG = SceneConstant.INIT_COLOR_G;
    private float _backgroundB = SceneConstant.INIT_COLOR_B;
    private float _backgroundAlpha = SceneConstant.INIT_COLOR_ALPHA;

    private GLSurfaceView _view;
    public SceneRenderer(GLSurfaceView v){
        _view  = v;


    }
    public boolean isFinish=false;

    private boolean _isChangeBackgroundColor=false;

    public boolean setModelList(CopyOnWriteArrayList<Model> arg_list ){
        Log.e("glthread","setModelList | id is "+Thread.currentThread().getId());
        _modelList = arg_list;
        return true;
    }
    public void setCamera(float arg_x,float arg_y,float arg_z){
        _cameraX = arg_x;
        _cameraY = arg_y;
        _cameraZ = arg_z;
    }
    public void setLightLocation(float arg_x,float arg_y,float arg_z){
        _lightX = arg_x;
        _lightY = arg_y;
        _lightZ = arg_z;
    }
    public boolean setBackgroundColor(float arg_r,float arg_g,float arg_b ,float arg_alpha){

        if(arg_b >1 || arg_b <0 ||
           arg_g > 1 || arg_g <0 ||
           arg_r > 1 || arg_r <0 ||
           arg_alpha>1 || arg_alpha<0){
          Log.e(_logTag , "color param is wrong!");
        }
        _isChangeBackgroundColor=true;
        _backgroundR =arg_r;
        _backgroundG=arg_g;
        _backgroundB=arg_b;
        _backgroundAlpha =arg_alpha;
        return true;
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        if(_isChangeBackgroundColor){
            GLES20.glClearColor(_backgroundR,_backgroundG,_backgroundB,_backgroundAlpha);
            _isChangeBackgroundColor=false;
        }
        if(_modelList!=null){
            for(Model model:_modelList){
                model.draw();
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 10, 100);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {

        GLES20.glClearColor(_backgroundR,_backgroundG,_backgroundB,_backgroundAlpha);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnable(GLES20.GL_CULL_FACE);

        MatrixState.setInitStack();

        MatrixState.setLightLocation(_lightX, _lightY, _lightZ);

    }


}