package com.editor.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

//加载顶点Shader与片元Shader的工具类
public class ShaderUtil {

    private final static String _logTag = "ShaderUtil ES20_ERROR";

    //加载制定shader的方法
    public static int loadShader
    (
            int shaderType, //shader的类型  GLES20.GL_VERTEX_SHADER   GLES20.GL_FRAGMENT_SHADER
            String source   //shader的脚本字符串
    ) {
        //创建一个新shader
        int shader = GLES20.glCreateShader(shaderType);
        //若创建成功则加载shader
        if (shader != 0) {
            //加载shader的源代码
            GLES20.glShaderSource(shader, source);
            //编译shader
            GLES20.glCompileShader(shader);
            //存放编译成功shader数量的数组
            int[] compiled = new int[1];
            //获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {//若编译失败则显示错误日志并删除此shader
                Log.e(_logTag, "Could not compile shader " + shaderType + ":");
                Log.e(_logTag, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    //创建shader程序的方法
    public static int createShaderProgram(String arg_vertexSource, String arg_fragmentSource)throws Exception{
        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, arg_vertexSource);
        if (vertexShader == 0) {
            throw new Exception("can not create vertex shader");
        }

        //加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, arg_fragmentSource);
        if (pixelShader == 0) {
            throw new Exception("can not create pixel shader");
        }

        //创建程序
        int program = GLES20.glCreateProgram();
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0) {
            //向程序中加入顶点着色器
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            //向程序中加入片元着色器
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            //链接程序
            GLES20.glLinkProgram(program);
            //存放链接成功program数量的数组
            int[] linkStatus = new int[1];
            //获取program的链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE) {
                String msg =  GLES20.glGetProgramInfoLog(program);
                Log.e(_logTag, "Could not link program: ");
                Log.e(_logTag, msg );
                GLES20.glDeleteProgram(program);
                throw new Exception(msg);
            }
        }
        return program;
    }

    //检查每一步操作是否有错误的方法
    private static void checkGlError(String arg_op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(_logTag, arg_op + ": glError " + error);
            throw new RuntimeException(arg_op + ": glError " + error);
        }
    }

    //从sh脚本中加载shader内容的方法
    public static String loadShaderFromAssetsFile(String arg_fname, Resources arg_resources) {
        String result = null;
        try {
            InputStream in = arg_resources.getAssets().open(arg_fname);
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(_logTag,  "loadShaderFromAssetsFile err: "+e.getMessage() );
        }
        return result;
    }

}
