package com.editor.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

//���ض���Shader��ƬԪShader�Ĺ�����
public class ShaderUtil {

    private final static String _logTag = "ShaderUtil ES20_ERROR";

    //�����ƶ�shader�ķ���
    public static int loadShader
    (
            int shaderType, //shader������  GLES20.GL_VERTEX_SHADER   GLES20.GL_FRAGMENT_SHADER
            String source   //shader�Ľű��ַ���
    ) {
        //����һ����shader
        int shader = GLES20.glCreateShader(shaderType);
        //�������ɹ������shader
        if (shader != 0) {
            //����shader��Դ����
            GLES20.glShaderSource(shader, source);
            //����shader
            GLES20.glCompileShader(shader);
            //��ű���ɹ�shader����������
            int[] compiled = new int[1];
            //��ȡShader�ı������
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {//������ʧ������ʾ������־��ɾ����shader
                Log.e(_logTag, "Could not compile shader " + shaderType + ":");
                Log.e(_logTag, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    //����shader����ķ���
    public static int createShaderProgram(String arg_vertexSource, String arg_fragmentSource)throws Exception{
        //���ض�����ɫ��
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, arg_vertexSource);
        if (vertexShader == 0) {
            throw new Exception("can not create vertex shader");
        }

        //����ƬԪ��ɫ��
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, arg_fragmentSource);
        if (pixelShader == 0) {
            throw new Exception("can not create pixel shader");
        }

        //��������
        int program = GLES20.glCreateProgram();
        //�����򴴽��ɹ���������м��붥����ɫ����ƬԪ��ɫ��
        if (program != 0) {
            //������м��붥����ɫ��
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            //������м���ƬԪ��ɫ��
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            //���ӳ���
            GLES20.glLinkProgram(program);
            //������ӳɹ�program����������
            int[] linkStatus = new int[1];
            //��ȡprogram���������
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //������ʧ���򱨴�ɾ������
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

    //���ÿһ�������Ƿ��д���ķ���
    private static void checkGlError(String arg_op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(_logTag, arg_op + ": glError " + error);
            throw new RuntimeException(arg_op + ": glError " + error);
        }
    }

    //��sh�ű��м���shader���ݵķ���
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
