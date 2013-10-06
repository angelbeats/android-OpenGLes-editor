package com.editor;

import android.content.res.Resources;
import android.util.Log;
import com.editor.common.LoadUtil;

import com.editor.common.Model;


import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-10-2
 * Time: ÏÂÎç1:44
 * To change this template use File | Settings | File Templates.
 */
public class ObjBuilder {
    private static final String _logTag = "ObjBuilder";
    public static HashMap<String, Model> buildObjModel(String[] arg_pathList) throws Exception {
        HashMap<String, Model> objHashMap;
        objHashMap = new HashMap<String, Model>();
        MatrixState.setLightLocation(40, 10, 20);
        Resources resource = ObstacleResMgr.getResMng();
        if (null == resource) {
            Log.e(_logTag, "Resources is null");
            throw new Exception("Resources is null");
        }
        for (String path : arg_pathList) {
            if (null == objHashMap.get(path)) {
                Log.i(_logTag, "load " + path);
                Model tmp = LoadUtil.loadObjFile(path, resource);
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

}

