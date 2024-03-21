    package com.example.javaappversion16;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.annotation.SuppressLint;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.content.pm.ResolveInfo;
    import android.graphics.Color;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.view.GestureDetector;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.Toast;

    import java.util.ArrayList;
    import java.util.List;

    public class AppDrawer extends AppCompatActivity {

        public static final String EXTRA_APP_LIST = "app_list";
        RecyclerView recyclerView ;
        ArrayList<StructAppInfo> appList ;
        AdapterAppList adapterAppList;
        private GestureDetector gestureDetector;


        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_app_drawer);

            gestureDetector = new GestureDetector(this, new MyGestureListener());

            // Hide the notification bar
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent)); // Set to transparent
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);





//
//            Window window = getWindow();
//            window.setStatusBarColor(Color.parseColor("#000000"));
            String secretcode = getIntent().getStringExtra("secretcode");

            initialize();
            appList = getIntent().getParcelableArrayListExtra(AppDrawer.EXTRA_APP_LIST);


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) ;
            recyclerView = findViewById(R.id.recyclerView);
            adapterAppList = new AdapterAppList(appList , this , secretcode);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapterAppList);



        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

        }


        private void initialize() {
        }

        public boolean onTouchEvent(MotionEvent event) {
            // Pass touch events to the GestureDetector
            gestureDetector.onTouchEvent(event);
            return super.onTouchEvent(event);
        }


        private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(diffY) &&
                        Math.abs(diffX) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Swipe right
                        onSwipeRight();
                    } else {
                        // Swipe left
                        onSwipeLeft();
                    }
                    return true;
                }

                if (Math.abs(diffY) > Math.abs(diffX) &&
                        Math.abs(diffY) > SWIPE_THRESHOLD &&
                        Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        // Swipe down
                        onSwipeDown();
                    } else {
                        // Swipe up
                        onSwipeUp();
                    }
                    return true;
                }


                return false;
            }
        }

        private void onSwipeUp() {
            // Implement your action for swiping up



        }

        private void onSwipeDown() {
            // Implement your action for swiping down
            finish();
            overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

        }

        private void onSwipeLeft() {
            // Implement your action for left swipe


        }

        private void onSwipeRight() {
            // Implement your action for right swipe

        }




    }