package com.editor.EditorModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.opengl.GLES20;
import com.editor.common.MatrixState;
import com.editor.common.ShaderUtil;


public class ObjModel implements Model {
    int _program;
    int _finalMatrixHandle;
    int _tranMatrixHandle;
    int _positionHandle;
    int _normalHandle;
    int _lightLocationHandle;
    int _cameraHandle;
    int _texCoorHandle;
    String _vertexShader;
    String _fragmentShader;

    FloatBuffer _vertexBuffer;
    FloatBuffer _normalBuffer;
    FloatBuffer _texCoorBuffer;
    int _vertexCount = 0;

    float _tx;
    float _ty;
    float _tz;

    float _sx = 1;
    float _sy = 1;
    float _sz = 1;

    float _rXAngle;
    float _rYAngle;
    float _rZAngle;

    float[] _vertices;

    int _texId;

    public ObjModel(Resources arg_resources, float[] arg_vertices, float[] arg_normals, float arg_texCoors[]) throws Exception {
        _vertices = arg_vertices;
        initVertexData(arg_vertices, arg_normals, arg_texCoors);
        initShader(arg_resources);

    }

    public void initVertexData(float[] vertices, float[] normals, float texCoors[]) {
        _vertexCount = vertices.length / 3;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        _vertexBuffer.put(vertices);
        _vertexBuffer.position(0);
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        _normalBuffer = cbb.asFloatBuffer();
        _normalBuffer.put(normals);
        _normalBuffer.position(0);
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        _texCoorBuffer = tbb.asFloatBuffer();
        _texCoorBuffer.put(texCoors);
        _texCoorBuffer.position(0);
    }

    public void initShader(Resources arg_resources) throws Exception {
        _vertexShader = ShaderUtil.loadShaderFromAssetsFile("vertex.sh", arg_resources);
        _fragmentShader = ShaderUtil.loadShaderFromAssetsFile("frag.sh", arg_resources);
        _program = ShaderUtil.createShaderProgram(_vertexShader, _fragmentShader);
        _positionHandle = GLES20.glGetAttribLocation(_program, "aPosition");
        _normalHandle = GLES20.glGetAttribLocation(_program, "aNormal");
        _finalMatrixHandle = GLES20.glGetUniformLocation(_program, "uMVPMatrix");
        _tranMatrixHandle = GLES20.glGetUniformLocation(_program, "uMMatrix");
        _lightLocationHandle = GLES20.glGetUniformLocation(_program, "uLightLocation");
        _texCoorHandle = GLES20.glGetAttribLocation(_program, "aTexCoor");
        _cameraHandle = GLES20.glGetUniformLocation(_program, "uCamera");
    }

    public void draw() {
        MatrixState.pushMatrix();

        MatrixState.translate(_tx, _ty, _tz);
        MatrixState.scale(_sx, _sy, _sz);
        MatrixState.rotate(_rXAngle, 1, 0, 0);
        MatrixState.rotate(_rYAngle, 0, 1, 0);
        MatrixState.rotate(_rZAngle, 0, 0, 1);
        GLES20.glUseProgram(_program);
        GLES20.glUniformMatrix4fv(_finalMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(_tranMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES20.glUniform3fv(_lightLocationHandle, 1, MatrixState.getLightPositionFB());
        GLES20.glUniform3fv(_cameraHandle, 1, MatrixState.getCameraFB());
        GLES20.glVertexAttribPointer
                (
                        _positionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        _vertexBuffer
                );
        GLES20.glVertexAttribPointer
                (
                        _normalHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        _normalBuffer
                );
        GLES20.glVertexAttribPointer
                (
                        _texCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        _texCoorBuffer
                );
        GLES20.glEnableVertexAttribArray(_positionHandle);
        GLES20.glEnableVertexAttribArray(_normalHandle);
        GLES20.glEnableVertexAttribArray(_texCoorHandle);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _texId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, _vertexCount);
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
        _rXAngle = arg_angle;
    }

    public void setRotateY(float arg_angle, float arg_y) {
        _rYAngle = arg_angle;
    }

    public void setRotateZ(float arg_angle, float arg_z) {
        _rZAngle = arg_angle;
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

    public boolean setTextureId(int arg_texId) {
        return true;
    }
}
