package com.editor.EditorModel;

import android.content.res.Resources;
import android.util.Log;
import com.editor.common.MatrixState;

import com.editor.common.LoadUtil;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-10-3
 * Time: ����4:15
 * To change this template use File | Settings | File Templates.
 */
public class ModelFactory {

    private static final String _logTag = "ModelFactory";

    public static HashMap<String, Model> buildObjModel(String[] arg_pathList, Resources arg_resources) throws Exception {
        HashMap<String, Model> objHashMap;
        objHashMap = new HashMap<String, Model>();
        if (null == arg_resources) {
            Log.e(_logTag, "Resources is null");
            throw new Exception("Resources is null");
        }
        for (String path : arg_pathList) {
            if (null == objHashMap.get(path)) {
                Log.i(_logTag, "load " + path);
                Model tmp = LoadUtil.loadObjFile(path, arg_resources);
                if (null != tmp) {
                    objHashMap.put(path, tmp);
                    continue;
                }
                Log.e(_logTag, path + " is not exists !");

                throw new Exception("can not find the obj file " + path + " in assets!");

            } else {
                Log.e(_logTag, path + " is already loaded !");
            }
        }
        return objHashMap;
    }
    public static Model buildObjModel(String arg_path, Resources arg_resources) throws Exception {
        if (null == arg_resources) {
            Log.e(_logTag, "Resources is null");
            throw new Exception("Resources is null");
        }
        if (null == arg_path) {
            Log.e(_logTag, "Path is null");
            throw new Exception("Path is null");
        }

        Log.i(_logTag, "load " + arg_path);
        Model tmp = LoadUtil.loadObjFile(arg_path, arg_resources);
        if (null != tmp) {
            Log.e(_logTag, "could not load the obj in "+arg_path);
            throw new Exception("could not load the obj in "+arg_path);
        }
        return tmp;
    }

    public static Model buildRectModel(float arg_width, float arg_height, Resources arg_resources) throws Exception {
        if (null == arg_resources) {
            Log.e(_logTag, "Resources is null");
            throw new Exception("Resources is null");
        }

        if (arg_height < 0 || arg_width < 0) {
            Log.e(_logTag, "the height or width is Illegal :height "+arg_height+" , width "+arg_width);
            throw new Exception("Resources is null");
        }
        Log.e("glthread","buildRectModel | id is "+Thread.currentThread().getId());
        return new RectModel(arg_resources, arg_width, arg_height);
    }



}
