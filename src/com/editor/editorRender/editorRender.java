package com.editor.editorRender;

import android.opengl.GLSurfaceView;
import com.editor.EditorModel.Model;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Fantasy
 * Date: 13-10-19
 * Time: 下午12:13
 * To change this template use File | Settings | File Templates.
 */
public interface editorRender {

    public boolean setBackgroundColor(float arg_r,float arg_g,float arg_b ,float arg_alpha);

    public boolean setModelList(CopyOnWriteArrayList<Model> arg_list );

}
