package com.bn.Sample9_4;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.opengl.GLSurfaceView;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.bn.Sample9_4.common.LoadUtil;
import com.bn.Sample9_4.common.Model;
import com.bn.Sample9_4.common.ObjModel;
import com.bn.Sample9_4.common.RectModel;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/360;
    private SceneRenderer mRenderer;
    
    private float mPreviousY; 
    private float mPreviousX; 
    
    int textureId;

    int sUp=0;

    public void setUp(){
        sUp++;
    }

	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
	

    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
//            float dy = y - mPreviousY;
//            float dx = x - mPreviousX;
//            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;
//            mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;
//            requestRender();
        }
//        mPreviousY = y;
//        mPreviousX = x;
        return true;
    }
       private float t = 0;
	private class SceneRenderer implements GLSurfaceView.Renderer
    {  
		float yAngle;
    	float xAngle;
       Model _map;
        private final static String _logTag = "View";
        HashMap<String, Model> test;
        public void onDrawFrame(GL10 gl) 
        { 

            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);


            MatrixState.pushMatrix();

//            MatrixState.rotate(yAngle, 0, 1, 0);
//            MatrixState.rotate(xAngle, 1, 0, 0);

            MatrixState.setCamera(sUp,0,35 ,0f,0f,0f,0f,1.0f,0.0f);
            t+=0.01;
            if(t>30){
               t=-10;
            }
            if(test!=null)
            {

                Model tmp = test.get("test/ch_t.obj");
                tmp.setScale(0.05f,0.05f,0.05f);
                tmp.setPosition(0,0,4);
                tmp.draw(textureId);

                tmp= test.get("ch_t_old.obj");
                tmp.setScale(0.05f,0.05f,0.05f);
                tmp.draw(textureId);
                _map.draw(textureId);

            }
            MatrixState.popMatrix();                  
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {

            GLES20.glViewport(0, 0, width, height);
         
            float ratio = (float) width / height;
         
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 10, 100);
       
            MatrixState.setCamera(0,0,35 ,0f,0f,0f,0f,1.0f,0.0f);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {

            GLES20.glClearColor(0.2f,0.0f,0.5f,1.0f);

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
         
            GLES20.glEnable(GLES20.GL_CULL_FACE);

            MatrixState.setInitStack();

            MatrixState.setLightLocation(40, 10, 20);

            try{
                ObstacleResMgr.setResMng(MySurfaceView.this.getResources());
                test = ObjBuilder.buildObjModel(new String[]{"test/ch_t.obj","ch_t_old.obj"});
                _map = new RectModel(getResources(),100,100 );
                _map.setPosition(0,0,0);
            }catch (Exception e){
                Log.i(_logTag, "onSurfaceCreated err "+e.getMessage());
            }

            textureId=initTexture(R.drawable.test);
        }
    }
  	public int initTexture(int drawableId)
	{

		int[] textures = new int[1];
        GLES20.glGenTextures
                (
                        1,
                        textures,
                        0
                );
        int textureId=textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        

        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }

	   	GLUtils.texImage2D
	    (
	    		GLES20.GL_TEXTURE_2D,
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp), 
	     		bitmapTmp,
	     		GLUtils.getType(bitmapTmp), 
	     		0
	     );
	    bitmapTmp.recycle();
        return textureId;
	}
}
