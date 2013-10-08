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
import com.editor.common.ArcMenu.RayMenu;
import roboguice.activity.RoboActivity;

public class EditorActivity extends RoboActivity {
    private EditorGLSurfaceView _editorView;

    private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.main);


        ArcMenu arcMenu = (ArcMenu) this.findViewById(R.id.arc_menu);
        ArcMenu arcMenu2 = (ArcMenu)this. findViewById(R.id.arc_menu_2);
        RayMenu rayMenu = (RayMenu) this.findViewById(R.id.ray_menu);



        _editorView = new EditorGLSurfaceView(this);

        _editorView.requestFocus();
        _editorView.setFocusableInTouchMode(true);

        _editorView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        setContentView(_editorView);

        initArcMenu(arcMenu, ITEM_DRAWABLES);
        initArcMenu(arcMenu2, ITEM_DRAWABLES);


        final int itemCount = ITEM_DRAWABLES.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(ITEM_DRAWABLES[i]);

            final int position = i;
            rayMenu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(EditorActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                }
            });// Add a menu item
        }



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



