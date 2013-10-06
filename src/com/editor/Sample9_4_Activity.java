package com.editor;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.bn.editor.R;

public class Sample9_4_Activity extends Activity {
	private MySurfaceView mGLSurfaceView;

    LinearLayout imageButtonLinearLayout;

    private Button sUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);


        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//设置为横屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.main);


    /* -------------------- Start add ImageButtons------------------------------------- */
        Context context = this.getApplicationContext();
        imageButtonLinearLayout = new LinearLayout(this);
        imageButtonLinearLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
        imageButtonLinearLayout.setOrientation(LinearLayout.VERTICAL);
        // add imagebuttons
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout imagebuttonLinearLayout = (LinearLayout) inflater.inflate(R.layout.imagebutton, imageButtonLinearLayout, false);
        imageButtonLinearLayout.addView(imagebuttonLinearLayout);
    /* -------------------- End add ImageButtons------------------------------------- */



        //初始化GLSurfaceView
        mGLSurfaceView = new MySurfaceView(this);

        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控

//        mGLSurfaceView.setZOrderOnTop(true);
        mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        setContentView(mGLSurfaceView);


        // 添加imageButtonLinearLayout (SurfaceView)
        addContentView(imageButtonLinearLayout, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.FILL_PARENT));


        sUp=(Button)  findViewById(R.id.button2);
         sUp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //To change body of implemented methods use File | Settings | File Templates.

                  mGLSurfaceView.setUp();
             }
         });

        sUp=(Button)  findViewById(R.id.button3);
        sUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To change body of implemented methods use File | Settings | File Templates.

                mGLSurfaceView.setDown();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }    
}



