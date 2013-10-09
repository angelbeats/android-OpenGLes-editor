package com.editor;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.editor.common.ArcMenu.ArcMenu;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class EditorActivity extends RoboActivity {
    private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with };

    @InjectView(R.id.arc_menu) ArcMenu _arcMenu;
    @InjectView(R.id.editorGLSurfaceView) EditorGLSurfaceView _editorView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main);
        _editorView.requestFocus();
        _editorView.setFocusableInTouchMode(true);
        _editorView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        initArcMenu(_arcMenu, ITEM_DRAWABLES);
    }
    private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(itemDrawables[i]);
            final int position = i;
            menu.addItem(item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(EditorActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
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



