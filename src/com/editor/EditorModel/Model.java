package com.editor.EditorModel;

/**
 * Created with IntelliJ IDEA.
 * User: walker
 * Date: 13-10-3
 * Time: ÏÂÎç1:22
 * To change this template use File | Settings | File Templates.
 */
public interface Model {

    public void draw(int arg_texId);
    public String getModelId();
    public void setPosition(float arg_x ,float arg_y ,float arg_z );
    public void setScale(float arg_x ,float arg_y ,float arg_z);
    public void setRotateX(float arg_angle,float arg_x );
    public void setRotateY(float arg_angle,float arg_y );
    public void setRotateZ(float arg_angle,float arg_z );
    public float[] getVertices();

    public float[] getPosition();


}
