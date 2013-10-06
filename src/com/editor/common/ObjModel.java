package com.editor.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.opengl.GLES20;
import com.editor.MatrixState;


//加载后的物体——仅携带顶点信息，颜色随机
public class ObjModel implements Model{
    int _program;//自定义渲染管线着色器程序id
    int _finalMatrixHandle;//总变换矩阵引用
    int _tranMatrixHandle;//位置、旋转变换矩阵
    int _positionHandle; //顶点位置属性引用
    int _normalHandle; //顶点法向量属性引用
    int _lightLocationHandle;//光源位置属性引用
    int _cameraHandle; //摄像机位置属性引用
    int _texCoorHandle; //顶点纹理坐标属性引用
    String _vertexShader;//顶点着色器代码脚本
    String _fragmentShader;//片元着色器代码脚本

    FloatBuffer _vertexBuffer;//顶点坐标数据缓冲
    FloatBuffer _normalBuffer;//顶点法向量数据缓冲
    FloatBuffer _texCoorBuffer;//顶点纹理坐标数据缓冲
    int _vertexCount = 0;

    //平移
    float _tx;
    float _ty;
    float _tz;

    //缩放
    float _sx=1;
    float _sy=1;
    float _sz=1;

    //旋转
    float _rXAngle;
    float _rYAngle;
    float _rZAngle;

    float[] _vertices;

    public ObjModel(Resources arg_resources, float[] arg_vertices, float[] arg_normals, float arg_texCoors[])throws Exception{
        _vertices = arg_vertices;
        //初始化顶点坐标与着色数据
        initVertexData(arg_vertices, arg_normals, arg_texCoors);
        //初始化shader
        initShader(arg_resources);

    }

    //初始化顶点坐标与着色数据的方法
    public void initVertexData(float[] vertices, float[] normals, float texCoors[]) {
        //顶点坐标数据的初始化================begin============================
        _vertexCount = vertices.length / 3;

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        _vertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        _vertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        _vertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================

        //顶点法向量数据的初始化================begin============================
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        _normalBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        _normalBuffer.put(normals);//向缓冲区中放入顶点法向量数据
        _normalBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================

        //顶点纹理坐标数据的初始化================begin============================
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length * 4);
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
        _texCoorBuffer = tbb.asFloatBuffer();//转换为Float型缓冲
        _texCoorBuffer.put(texCoors);//向缓冲区中放入顶点纹理坐标数据
        _texCoorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================
    }

    //初始化shader
    public void initShader(Resources arg_resources)throws Exception{
            //加载顶点着色器的脚本内容
            _vertexShader = ShaderUtil.loadShaderFromAssetsFile("vertex.sh", arg_resources);
            //加载片元着色器的脚本内容
            _fragmentShader = ShaderUtil.loadShaderFromAssetsFile("frag.sh", arg_resources);
            //基于顶点着色器与片元着色器创建程序
            _program = ShaderUtil.createShaderProgram(_vertexShader, _fragmentShader);
            //获取程序中顶点位置属性引用
            _positionHandle = GLES20.glGetAttribLocation(_program, "aPosition");
            //获取程序中顶点颜色属性引用
            _normalHandle = GLES20.glGetAttribLocation(_program, "aNormal");
            //获取程序中总变换矩阵引用
            _finalMatrixHandle = GLES20.glGetUniformLocation(_program, "uMVPMatrix");
            //获取位置、旋转变换矩阵引用
            _tranMatrixHandle = GLES20.glGetUniformLocation(_program, "uMMatrix");
            //获取程序中光源位置引用
            _lightLocationHandle = GLES20.glGetUniformLocation(_program, "uLightLocation");
            //获取程序中顶点纹理坐标属性引用
            _texCoorHandle = GLES20.glGetAttribLocation(_program, "aTexCoor");
            //获取程序中摄像机位置引用
            _cameraHandle = GLES20.glGetUniformLocation(_program, "uCamera");
    }

    public void draw(int arg_texId) {
        MatrixState.pushMatrix();

        MatrixState.translate(_tx,_ty,_tz);
        MatrixState.scale(_sx,_sy ,_sz);
        MatrixState.rotate(_rXAngle ,1,0,0);
        MatrixState.rotate(_rYAngle ,0,1,0);
        MatrixState.rotate(_rZAngle ,0,0,1);
        //制定使用某套着色器程序
        GLES20.glUseProgram(_program);
        //将最终变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(_finalMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将位置、旋转变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(_tranMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //将光源位置传入着色器程序
        GLES20.glUniform3fv(_lightLocationHandle, 1, MatrixState.lightPositionFB);
        //将摄像机位置传入着色器程序
        GLES20.glUniform3fv(_cameraHandle, 1, MatrixState.cameraFB);
        // 将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        _positionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        _vertexBuffer
                );
        //将顶点法向量数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        _normalHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        _normalBuffer
                );
        //为画笔指定顶点纹理坐标数据
        GLES20.glVertexAttribPointer
                (
                        _texCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        _texCoorBuffer
                );
        //启用顶点位置、法向量、纹理坐标数据
        GLES20.glEnableVertexAttribArray(_positionHandle);
        GLES20.glEnableVertexAttribArray(_normalHandle);
        GLES20.glEnableVertexAttribArray(_texCoorHandle);
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, arg_texId);
        //绘制加载的物体
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
