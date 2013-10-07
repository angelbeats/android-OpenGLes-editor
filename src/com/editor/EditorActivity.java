package com.editor;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class EditorActivity extends RoboActivity {
    private EditorGLSurfaceView _editorView;

    LinearLayout imageButtonLinearLayout;
    @InjectView(R.id.button)
    Button sUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
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
        _editorView = new EditorGLSurfaceView(this);

        _editorView.requestFocus();//获取焦点
        _editorView.setFocusableInTouchMode(true);//设置为可触控

//        mGLSurfaceView.setZOrderOnTop(true);
        _editorView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        setContentView(_editorView);


        // 添加imageButtonLinearLayout (SurfaceView)
        addContentView(imageButtonLinearLayout, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.FILL_PARENT));

    }

    @Override
    protected void onResume() {
        super.onResume();
        _editorView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _editorView.onPause();
    }
}



