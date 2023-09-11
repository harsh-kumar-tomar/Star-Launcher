package com.example.javaappversion16;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView txt ;
    private TextView batteryPercentageTextView;
    private TextView currentDateTimeTextView;
    private GestureDetector gestureDetector;
    ArrayList<StructAppInfo> appList ;

    private final Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDateTimeTextView = findViewById(R.id.dateTime);
        gestureDetector = new GestureDetector(this, new MyGestureListener());



        // Hide the notification bar
//        View decorView = getWindow().getDecorView();
//        WindowInsetsController insetsController = decorView.getWindowInsetsController();
//        if (insetsController != null) {
//            insetsController.hide(WindowInsets.Type.systemBars());
//        }
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#000000"));

        batteryPercentageTextView = findViewById(R.id.batteryPercentage);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, intentFilter);



        txt = findViewById(R.id.textview);
         appList = loadInstalledApps();


        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext() , AppDrawer.class ) ;
                intent.putParcelableArrayListExtra(AppDrawer.EXTRA_APP_LIST, appList);
                startActivity(intent);
            }
        });


    }

    // Define a BroadcastReceiver to handle battery status changes
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                // Calculate battery percentage
                float batteryPercentage = (level / (float) scale) * 100;

                // Display the battery percentage in the TextView
                batteryPercentageTextView.setText( " "+(int)batteryPercentage );
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the BroadcastReceiver when the activity is destroyed
        unregisterReceiver(batteryReceiver);
    }

    protected void onResume() {
        super.onResume();
        // Start updating the current date and time when the activity is resumed
        updateDateTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop updating the current date and time when the activity is paused
        handler.removeCallbacksAndMessages(null);
    }

    private void updateDateTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get the current date and time
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy\n hh:mm:ss a", Locale.getDefault());
                String currentDateTime = dateFormat.format(new Date());

                // Set the current date and time to the TextView
                currentDateTimeTextView.setText(currentDateTime);

                // Schedule the next update in 1 second (adjust as needed)
                handler.postDelayed(this, 1000);
            }
        }, 0); // Start updating immediately
    }


    // Load the list of installed apps in MainActivity
    private ArrayList<StructAppInfo> loadInstalledApps() {
        ArrayList<StructAppInfo> appList = new ArrayList<>();
        PackageManager pm = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(intent, 0);

        for (ResolveInfo ri : allApps) {
            StructAppInfo app = new StructAppInfo();
            app.setLabel(ri.loadLabel(pm).toString());
            app.setPackageName(ri.activityInfo.packageName);
            app.setIcon(ri.activityInfo.loadIcon(pm));
            appList.add(app);
        }

        // Sort the appList alphabetically based on app label
        Collections.sort(appList, new Comparator<StructAppInfo>() {
            @Override
            public int compare(StructAppInfo app1, StructAppInfo app2) {
                return app1.getLabel().compareToIgnoreCase(app2.getLabel());
            }
        });

        return appList;
    }



    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    // Swipe down, close AppDrawer if it's open
                    // You may need to implement logic to detect if AppDrawer is open
                } else {
                    // Swipe up, open AppDrawer
                    Intent intent = new Intent(getApplicationContext(), AppDrawer.class);
                    intent.putParcelableArrayListExtra(AppDrawer.EXTRA_APP_LIST, appList);
                    startActivity(intent);
                }
            }
            return false;
        }
    }




}