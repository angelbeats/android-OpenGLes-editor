package com.editor.editorRender;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-10-19
 * Time: 下午8:26
 * To change this template use File | Settings | File Templates.
 */
public class ShaderManager {
    private static HashMap<String, Integer> _shaderMap = new HashMap<String, Integer>();
    private static boolean _isInit = false;

    public static boolean addNewShader() {

        return false;
    }

    public static int getShader(String arg_id) {
        if (_isInit) {


            _isInit = true;
        }
        return _shaderMap.get(arg_id);
    }

}
