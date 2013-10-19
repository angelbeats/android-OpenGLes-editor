package com.editor.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;

import android.opengl.Matrix;

public class MatrixState {
    private static float[] _mProjMatrix = new float[16];
    private static float[] _mVMatrix = new float[16];
    private static float[] _currMatrix;
    private static float[] _lightLocation = new float[]{0, 0, 0};
    private static FloatBuffer _cameraFB;
    private static FloatBuffer _lightPositionFB;
    private static boolean _isSetLightBF = false;
    private static boolean _isSetCameraBF = false;

    private static Stack<float[]> _mStack = new Stack<float[]>();


    public static FloatBuffer getCameraFB(){
        if(!_isSetCameraBF ){
            setCamera(0,0,35,0,0,0,1,0,10);
            _isSetCameraBF = true;
        }
        return _cameraFB;
    }
    public static  FloatBuffer getLightPositionFB(){
        if(!_isSetLightBF ){
             setLightLocation(0,0,0);
            _isSetLightBF = true;
        }
        return _lightPositionFB;
    }
    public static void setInitStack()
    {
        _currMatrix = new float[16];
        Matrix.setRotateM(_currMatrix, 0, 0, 1, 0, 0);
    }

    public static void pushMatrix()
    {
        _mStack.push(_currMatrix.clone());
    }

    public static void popMatrix()
    {
        _currMatrix = _mStack.pop();
    }

    public static void translate(float x, float y, float z)
    {
        Matrix.translateM(_currMatrix, 0, x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z)
    {
        Matrix.rotateM(_currMatrix, 0, angle, x, y, z);

    }



    public static void setCamera (
            float arg_cx,            float arg_cy,        float arg_cz,
            float arg_tx,         float arg_ty,     float arg_tz,
            float arg_upx,   float arg_upy,    float arg_upz
    ) {
        Matrix.setLookAtM  (
                        _mVMatrix,               0,
                arg_cx,                      arg_cy,            arg_cz,
                arg_tx,          arg_ty,       arg_tz,
                        arg_upx,   arg_upy,    arg_upz
                );

        float[] cameraLocation = new float[3];
        cameraLocation[0] = arg_cx;
        cameraLocation[1] = arg_cy;
        cameraLocation[2] = arg_cz;

        ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
        llbb.order(ByteOrder.nativeOrder());
        _cameraFB = llbb.asFloatBuffer();
        _cameraFB.put(cameraLocation);
        _cameraFB.position(0);
    }

    public static void setProjectFrustum
    (
            float left,
            float right,
            float bottom,
            float top,
            float near,
            float far
    ) {
        Matrix.frustumM(_mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static void setProjectOrtho
    (
            float left,
            float right,
            float bottom,
            float top,
            float near,
            float far
    ) {
        Matrix.orthoM(_mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static float[] getFinalMatrix() {
        float[] mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, _mVMatrix, 0, _currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, _mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public static float[] getMMatrix() {
        return _currMatrix;
    }

    public static void setLightLocation(float x, float y, float z) {
        _lightLocation[0] = x;
        _lightLocation[1] = y;
        _lightLocation[2] = z;
        ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
        llbb.order(ByteOrder.nativeOrder());
        _lightPositionFB = llbb.asFloatBuffer();
        _lightPositionFB.put(_lightLocation);
        _lightPositionFB.position(0);
    }

    public static void scale(float x,float y,float z){
        Matrix.scaleM(_currMatrix, 0, x, y, z);
    }
}
