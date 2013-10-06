package com.editor;

import android.content.res.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 13-10-2
 * Time: обнГ4:44
 * To change this template use File | Settings | File Templates.
 */
public class ObstacleResMgr {
    public static Resources resMng;

    public ObstacleResMgr(){

    }

    public static Resources getResMng() {
        return resMng;
    }

    public static void setResMng(Resources resMng) {
        ObstacleResMgr.resMng = resMng;
    }
}
