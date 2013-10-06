package com.editor.common;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;
import com.editor.MatrixState;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-8-26
 * To change this template use File | Settings | File Templates.
 */
public class RectModel implements Model {


    private FloatBuffer _vertexBuffer = null;
    private FloatBuffer _texCoorBuffer = null;

    private final static String _logTag = "RectModel";

    private int _rectID;
    private float _width;
    private float _height;

    //Æ½ÒÆ
    float _tx;
    float _ty;
    float _tz;

    //Ëõ·Å
    float _sx=1;
    float _sy=1;
    float _sz=1;

    //Ðý×ª
    float _rx;
    float _rXAngle;
    float _ry;
    float _rYAngle;
    float _rz;
    float _rZAngle;

    float[] _vertices;

    private String _vertexShaderContent;
    private String _fragmentShaderContent;
    private int _shaderProgramID;
    private int _finalTranslMatrixID;
    private int _positionVertexHandleID;
    private int _texCoorHandleID;


    public RectModel(Resources arg_resources, float arg_w, float arg_h) throws Exception {

        _width = arg_w;
        _height = arg_h;
        initVertexData();
        initShader(arg_resources);
    }

    private void initVertexData() {

        float vertices[] = new float[]
                {
                        -_width / 2, _height / 2, 0,
                        -_width / 2, -_height / 2, 0,
                        _width / 2, -_height / 2, 0,

                        _width / 2, -_height / 2, 0,
                        _width / 2, _height / 2, 0,
                        -_width / 2, _height / 2, 0
                };

        _vertices = vertices;
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        _vertexBuffer.put(vertices);
        _vertexBuffer.position(0);

        float texCoor[] = new float[]
                {
                        0, 0, 0, 1, 1, 1,
                        1, 1, 1, 0, 0, 0
                };

        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        _texCoorBuffer = cbb.asFloatBuffer();
        _texCoorBuffer.put(texCoor);
        _texCoorBuffer.position(0);
    }

    public void initShader(Resources arg_resources) throws Exception {

        Log.i(_logTag, "initShader");
        _vertexShaderContent = ShaderUtil.loadShaderFromAssetsFile("vertex.sh", arg_resources);

        _fragmentShaderContent = ShaderUtil.loadShaderFromAssetsFile("frag.sh", arg_resources);

        _shaderProgramID = ShaderUtil.createShaderProgram(_vertexShaderContent, _fragmentShaderContent);

        _positionVertexHandleID = GLES20.glGetAttribLocation(_shaderProgramID, "aPosition");

        _texCoorHandleID = GLES20.glGetAttribLocation(_shaderProgramID, "aTexCoor");

        _finalTranslMatrixID = GLES20.glGetUniformLocation(_shaderProgramID, "uMVPMatrix");
        Log.i(_logTag, "initShader done");
    }


    public void draw(int arg_t) {


//        Log.i(_logTag, "Shader Program ID £º"+_shaderProgramID);
        GLES20.glUseProgram(_shaderProgramID);

        MatrixState.pushMatrix();

        MatrixState.rotate(_rXAngle, 1, 0, 0);
        MatrixState.rotate(_rYAngle, 0, 1, 0);
        MatrixState.rotate(_rZAngle, 0, 0, 1);
        MatrixState.translate(_tx, _ty, _tz);
        MatrixState.scale(_sx, _sy, _sz);

        GLES20.glUniformMatrix4fv(_finalTranslMatrixID, 1, false, MatrixState.getFinalMatrix(), 0);

        GLES20.glVertexAttribPointer
                (
                        _positionVertexHandleID,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        _vertexBuffer
                );

        GLES20.glVertexAttribPointer
                (
                        _texCoorHandleID,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        _texCoorBuffer
                );
        GLES20.glEnableVertexAttribArray(_positionVertexHandleID);
        GLES20.glEnableVertexAttribArray(_texCoorHandleID);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, arg_t);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        MatrixState.popMatrix();
    }

    public void setPosition(float arg_x, float arg_y, float arg_z) {
        _tx = arg_x;
        _ty = arg_y;
        _tz = arg_z;
    }

    public void setScale(float arg_x, float arg_y, float arg_z) {
        _sx = arg_x;
        _sy = arg_y;
        _sz = arg_z;
    }

    public void setRotateX(float arg_angle, float arg_x) {
        _rx = arg_x;
        _rXAngle = arg_angle;
    }

    public void setRotateY(float arg_angle, float arg_y) {
        _ry = arg_y;
        _rYAngle = arg_angle;
    }

    public void setRotateZ(float arg_angle, float arg_z) {
        _rZAngle = arg_angle;
        _rz = arg_z;
    }

    public String getModelId() {
        return "test";
    }

    public float[] getVertices() {
        return _vertices;
    }

    public float[] getPosition() {
        return new float[]{_tx, _ty, _tz};
    }


}
