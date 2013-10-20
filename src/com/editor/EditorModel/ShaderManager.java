package com.editor.EditorModel;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;
import com.editor.common.ShaderUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-10-19
 * Time: 下午8:26
 * To change this template use File | Settings | File Templates.
 */
public class ShaderManager {

    private static final String _logTag = "ShaderManager";
    private static boolean _isRectInit = false;


    private static String _rectVertexShaderContent;
    private static String _rectFragmentShaderContent;
    private static int _rectShaderProgramId;
    private static int _rectFinalTranslMatrixHandle;
    private static int _rectPositionVertexHandle;
    private static int _rectTexCoorHandle;

    public static void initRectShader(Resources arg_resources) throws Exception {
        Log.i(_logTag, "init Rect Shader start");
        _rectVertexShaderContent = ShaderUtil.loadShaderFromAssetsFile("RectModel/vertex.sh", arg_resources);

        _rectFragmentShaderContent = ShaderUtil.loadShaderFromAssetsFile("RectModel/frag.sh", arg_resources);

        _rectShaderProgramId = ShaderUtil.createShaderProgram(_rectVertexShaderContent, _rectFragmentShaderContent);

        _rectPositionVertexHandle = GLES20.glGetAttribLocation(_rectShaderProgramId, "aPosition");

        _rectTexCoorHandle = GLES20.glGetAttribLocation(_rectShaderProgramId, "aTexCoor");

        _rectFinalTranslMatrixHandle = GLES20.glGetUniformLocation(_rectShaderProgramId, "uMVPMatrix");
        Log.i(_logTag, "init Rect Shader done");
        _isRectInit = true;
    }

    public int getRectShaderProgramId() {
        if (_isRectInit) {
            return -1;
        }
        return _rectShaderProgramId;
    }
    public int getRectFinalTranslMatrixHandle(){
        if (_isRectInit) {
            return -1;
        }
        return _rectFinalTranslMatrixHandle;
    }
    public int getRectPositionVertexHandle(){
        if (_isRectInit) {
            return -1;
        }
        return _rectPositionVertexHandle;
    }


    static int _objShaderProgramId;
    static int _objFinalMatrixHandle;
    static int _objTranMatrixHandle;
    static int _objPositionHandle;
    static int _objNormalHandle;
    static int _objLightLocationHandle;
    static int _objCameraHandle;
    static int _objTexCoorHandle;
    static String _objVertexShaderContent;
    static String _objFragmentShaderContent;

    public static void initObjShader(Resources arg_resources) throws Exception {
        Log.i(_logTag, "init Obj Shader start");

        _objVertexShaderContent = ShaderUtil.loadShaderFromAssetsFile("ObjModel/vertex.sh", arg_resources);
        _objFragmentShaderContent = ShaderUtil.loadShaderFromAssetsFile("ObjModel/frag.sh", arg_resources);
        _objShaderProgramId = ShaderUtil.createShaderProgram(_objVertexShaderContent, _objFragmentShaderContent);
        _objPositionHandle = GLES20.glGetAttribLocation(_objShaderProgramId, "aPosition");
        _objNormalHandle = GLES20.glGetAttribLocation(_objShaderProgramId, "aNormal");
        _objFinalMatrixHandle = GLES20.glGetUniformLocation(_objShaderProgramId, "uMVPMatrix");
        _objTranMatrixHandle = GLES20.glGetUniformLocation(_objShaderProgramId, "uMMatrix");
        _objLightLocationHandle = GLES20.glGetUniformLocation(_objShaderProgramId, "uLightLocation");
        _objTexCoorHandle = GLES20.glGetAttribLocation(_objShaderProgramId, "aTexCoor");
        _objCameraHandle = GLES20.glGetUniformLocation(_objShaderProgramId, "uCamera");

        Log.i(_logTag, "init Obj Shader done");

    }


}
