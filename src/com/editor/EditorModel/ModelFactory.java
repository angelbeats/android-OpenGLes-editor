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
 * Time: ÏÂÎç4:15
 * To change this template use File | Settings | File Templates.
 */
public class ModelFactory {

    private static final String _logTag = "ModelFactory";

    public static HashMap<String, Model> buildObjModel(String[] arg_pathList , Resources arg_resources) throws Exception {
        HashMap<String, Model> objHashMap;
        objHashMap = new HashMap<String, Model>();
        MatrixState.setLightLocation(40, 10, 20);
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

    public static Model buildRectModel(float arg_width , float arg_height , Resources arg_resources)throws Exception{
        if(null == arg_resources ){

        }

        if(arg_height<0||arg_width<0){

        }



       return  new RectModel(arg_resources,arg_width,arg_height);
    }

}
