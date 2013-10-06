package com.editor.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.opengl.GLES20;
import com.editor.MatrixState;


//���غ�����塪����Я��������Ϣ����ɫ���
public class ObjModel implements Model{
    int _program;//�Զ�����Ⱦ������ɫ������id
    int _finalMatrixHandle;//�ܱ任��������
    int _tranMatrixHandle;//λ�á���ת�任����
    int _positionHandle; //����λ����������
    int _normalHandle; //���㷨������������
    int _lightLocationHandle;//��Դλ����������
    int _cameraHandle; //�����λ����������
    int _texCoorHandle; //��������������������
    String _vertexShader;//������ɫ������ű�
    String _fragmentShader;//ƬԪ��ɫ������ű�

    FloatBuffer _vertexBuffer;//�����������ݻ���
    FloatBuffer _normalBuffer;//���㷨�������ݻ���
    FloatBuffer _texCoorBuffer;//���������������ݻ���
    int _vertexCount = 0;

    //ƽ��
    float _tx;
    float _ty;
    float _tz;

    //����
    float _sx=1;
    float _sy=1;
    float _sz=1;

    //��ת
    float _rXAngle;
    float _rYAngle;
    float _rZAngle;

    float[] _vertices;

    public ObjModel(Resources arg_resources, float[] arg_vertices, float[] arg_normals, float arg_texCoors[])throws Exception{
        _vertices = arg_vertices;
        //��ʼ��������������ɫ����
        initVertexData(arg_vertices, arg_normals, arg_texCoors);
        //��ʼ��shader
        initShader(arg_resources);

    }

    //��ʼ��������������ɫ���ݵķ���
    public void initVertexData(float[] vertices, float[] normals, float texCoors[]) {
        //�����������ݵĳ�ʼ��================begin============================
        _vertexCount = vertices.length / 3;

        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        _vertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        _vertexBuffer.put(vertices);//�򻺳����з��붥����������
        _vertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================

        //���㷨�������ݵĳ�ʼ��================begin============================
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        _normalBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        _normalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        _normalBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //������ɫ���ݵĳ�ʼ��================end============================

        //���������������ݵĳ�ʼ��================begin============================
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length * 4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        _texCoorBuffer = tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        _texCoorBuffer.put(texCoors);//�򻺳����з��붥��������������
        _texCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
    }

    //��ʼ��shader
    public void initShader(Resources arg_resources)throws Exception{
            //���ض�����ɫ���Ľű�����
            _vertexShader = ShaderUtil.loadShaderFromAssetsFile("vertex.sh", arg_resources);
            //����ƬԪ��ɫ���Ľű�����
            _fragmentShader = ShaderUtil.loadShaderFromAssetsFile("frag.sh", arg_resources);
            //���ڶ�����ɫ����ƬԪ��ɫ����������
            _program = ShaderUtil.createShaderProgram(_vertexShader, _fragmentShader);
            //��ȡ�����ж���λ����������
            _positionHandle = GLES20.glGetAttribLocation(_program, "aPosition");
            //��ȡ�����ж�����ɫ��������
            _normalHandle = GLES20.glGetAttribLocation(_program, "aNormal");
            //��ȡ�������ܱ任��������
            _finalMatrixHandle = GLES20.glGetUniformLocation(_program, "uMVPMatrix");
            //��ȡλ�á���ת�任��������
            _tranMatrixHandle = GLES20.glGetUniformLocation(_program, "uMMatrix");
            //��ȡ�����й�Դλ������
            _lightLocationHandle = GLES20.glGetUniformLocation(_program, "uLightLocation");
            //��ȡ�����ж�������������������
            _texCoorHandle = GLES20.glGetAttribLocation(_program, "aTexCoor");
            //��ȡ�����������λ������
            _cameraHandle = GLES20.glGetUniformLocation(_program, "uCamera");
    }

    public void draw(int arg_texId) {
        MatrixState.pushMatrix();

        MatrixState.translate(_tx,_ty,_tz);
        MatrixState.scale(_sx,_sy ,_sz);
        MatrixState.rotate(_rXAngle ,1,0,0);
        MatrixState.rotate(_rYAngle ,0,1,0);
        MatrixState.rotate(_rZAngle ,0,0,1);
        //�ƶ�ʹ��ĳ����ɫ������
        GLES20.glUseProgram(_program);
        //�����ձ任��������ɫ������
        GLES20.glUniformMatrix4fv(_finalMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //��λ�á���ת�任��������ɫ������
        GLES20.glUniformMatrix4fv(_tranMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //����Դλ�ô�����ɫ������
        GLES20.glUniform3fv(_lightLocationHandle, 1, MatrixState.lightPositionFB);
        //�������λ�ô�����ɫ������
        GLES20.glUniform3fv(_cameraHandle, 1, MatrixState.cameraFB);
        // ������λ�����ݴ�����Ⱦ����
        GLES20.glVertexAttribPointer
                (
                        _positionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        _vertexBuffer
                );
        //�����㷨�������ݴ�����Ⱦ����
        GLES20.glVertexAttribPointer
                (
                        _normalHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        _normalBuffer
                );
        //Ϊ����ָ������������������
        GLES20.glVertexAttribPointer
                (
                        _texCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        _texCoorBuffer
                );
        //���ö���λ�á���������������������
        GLES20.glEnableVertexAttribArray(_positionHandle);
        GLES20.glEnableVertexAttribArray(_normalHandle);
        GLES20.glEnableVertexAttribArray(_texCoorHandle);
        //������
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, arg_texId);
        //���Ƽ��ص�����
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, _vertexCount);
        MatrixState.popMatrix();
    }

    public void setPosition(float arg_x ,float arg_y ,float arg_z ){
        _tx=arg_x;
        _ty=arg_y;
        _tz=arg_z;
    }

    public void setScale(float arg_x ,float arg_y ,float arg_z){
        _sx=arg_x;
        _sy=arg_y;
        _sz=arg_z;
    }

    public void setRotateX(float arg_angle,float arg_x ){
        _rXAngle = arg_angle;
    }
    public void setRotateY(float arg_angle,float arg_y ){
        _rYAngle = arg_angle;
    }
    public void setRotateZ(float arg_angle,float arg_z ){
        _rZAngle = arg_angle;
    }

    public String getModelId(){
        return "test";
    }

    public float[] getVertices(){
        return _vertices;
    }

    public float[] getPosition(){
        return new float[]{_tx,_ty,_tz};
    }
}
