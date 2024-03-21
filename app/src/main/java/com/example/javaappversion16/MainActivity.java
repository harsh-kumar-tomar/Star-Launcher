package com.example.javaappversion16;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;


import com.bumptech.glide.Glide;
import com.example.javaappversion16.databinding.ActivityMainBinding;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    private TextView batteryPercentageTextView;
    private TextView currentDateTimeTextView;
    private GestureDetector gestureDetector;
    TextView secondsTV, minuteTV, hourTV, date_month_year;
    ImageView percent10, percent20, percent30, percent40, percent50, percent60, percent70, percent80, percent90, percent100;
    ImageView app1, app2, app3, app4;
    ArrayList<StructAppInfo> appList;
    Animation blinkAnimation;
    int selectedValue, batteryFlag = 0;
    int selectedPosition = 0;
    ArrayList<ImageView> percentageImageView;
    private static final int REQUEST_CODE = 123;

    HashMap<View, String> selected_icon = new HashMap<>();
    ImageView selectedImageView;

    ActivityMainBinding binding;

    private final Handler handler = new Handler(Looper.getMainLooper());
    Drawable wallpaperDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        gestureDetector = new GestureDetector(this, new MyGestureListener());


        hourTV = findViewById(R.id.hourTV);
        date_month_year = findViewById(R.id.date_month_year);
        percent10 = findViewById(R.id.percent10);
        percent20 = findViewById(R.id.percent20);
        percent30 = findViewById(R.id.percent30);
        percent40 = findViewById(R.id.percent40);
        percent50 = findViewById(R.id.percent50);
        percent60 = findViewById(R.id.percent60);
        percent70 = findViewById(R.id.percent70);
        percent80 = findViewById(R.id.percent80);
        percent90 = findViewById(R.id.percent90);
        percent100 = findViewById(R.id.percent100);
        app1 = findViewById(R.id.app1);
        app2 = findViewById(R.id.app2);
        app3 = findViewById(R.id.app3);
        app4 = findViewById(R.id.app4);

        percentageImageView = new ArrayList<>();
        percentageImageView.add(percent10);
        percentageImageView.add(percent20);
        percentageImageView.add(percent30);
        percentageImageView.add(percent40);
        percentageImageView.add(percent50);
        percentageImageView.add(percent60);
        percentageImageView.add(percent70);
        percentageImageView.add(percent80);
        percentageImageView.add(percent90);
        percentageImageView.add(percent100);


        // Create a blinking animation
        blinkAnimation = new AlphaAnimation(1.0f, 0.0f);
        blinkAnimation.setDuration(1000); // 1000 milliseconds (1 second) for one cycle
        blinkAnimation.setInterpolator(new LinearInterpolator());
        blinkAnimation.setRepeatCount(Animation.INFINITE); // Infinite loop
        blinkAnimation.setRepeatMode(Animation.REVERSE); // Reverse the animation when it repeats


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent)); // Set to transparent
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(Color.parseColor("#120f16"));
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, intentFilter);


        appList = loadInstalledApps();


        // add app to home screen by long pressing on the app
        //open app from home screen by single app

        binding.app1.setOnClickListener(clickListener);
        binding.app2.setOnClickListener(clickListener);
        binding.app3.setOnClickListener(clickListener);
        binding.app4.setOnClickListener(clickListener);

        binding.app1.setOnLongClickListener(longClickListener);
        binding.app2.setOnLongClickListener(longClickListener);
        binding.app3.setOnLongClickListener(longClickListener);
        binding.app4.setOnLongClickListener(longClickListener);


    }





    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PackageManager pm = getApplicationContext().getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(selected_icon.get(v));
                getApplicationContext().startActivity(intent);

        }
    } ;

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            selectedImageView = (ImageView) v ;

            Intent intent = new Intent(getApplicationContext() , AppDrawer.class);
            intent.putExtra("secretcode" , "secretcode");
            intent.putParcelableArrayListExtra(AppDrawer.EXTRA_APP_LIST, appList);
            startActivityForResult(intent, REQUEST_CODE);
            overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            return true;
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG", "activity result:" );
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {


                    selectedPosition = data.getIntExtra("selectedPosition", -1);
                    selected_icon.put(selectedImageView,appList.get(selectedPosition).getPackageName());

                    Glide.with(getApplicationContext())
                            .load(appList.get(selectedPosition).getIcon())
                            .error(R.drawable.earth)
                            .into(selectedImageView);


                }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle the case where the user canceled the operation in AppDrawer
            }
        }
    }



    // BroadcastReceiver to handle battery status changes
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                // Calculate battery percentage
                float batteryPercentage = (level / (float) scale) * 100;
                setBatteryImage(batteryPercentage);


                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING);

                if (isCharging && batteryFlag==0) {

                    setChargingAnimation();

                } else {
                    setBatteryImage(batteryPercentage);
                }
            }
        }


        private void setBatteryImage(float batteryPercentage) {
            int[] percentages = {10, 20, 30, 40, 50, 60, 70, 80, 90, 99};

            for (int i = percentages.length - 1; i >= 0; i--) {
                if (batteryPercentage > percentages[i]) {
                    //set red color
                    setGreenColor(percentageImageView.subList(0, i + 1));
                    //set green color
                    setRedColor(percentageImageView.subList(i + 1, percentageImageView.size()));
                    break;
                }
            }
        }

        private void setRedColor(List<ImageView> imageViews) {

            for (ImageView imageView : imageViews) {
                imageView.setImageResource(R.drawable.background3);
//                imageView.clearAnimation();
            }
        }


        private void setGreenColor(List<ImageView> imageViews) {

            if (!imageViews.isEmpty()) {
                for (ImageView imageView : imageViews) {
                    imageView.setImageResource(R.drawable.background2);
//                    imageView.startAnimation(blinkAnimation); // Start animation
                }
            }

        }

        private void setChargingAnimation() {


            Drawable desiredDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.background3, null);

            for (ImageView a : percentageImageView) {
                Log.e("ANIM", "initial setGreenColor animation :battery ");

                if (a.getBackground().getConstantState().equals(desiredDrawable.getConstantState())) {
                    {
                        a.setImageResource(R.drawable.background2);
                        a.startAnimation(blinkAnimation);
                        batteryFlag = 1 ;
                        Log.e("ANIM", "setGreenColor animation :battery ");
                        break;
                    }

                }

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


    public boolean onTouchEvent(MotionEvent event) {
        // Pass touch events to the GestureDetector
        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
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
        Intent intent = new Intent(getApplicationContext() , AppDrawer.class ) ;
        intent.putParcelableArrayListExtra(AppDrawer.EXTRA_APP_LIST, appList);
        intent.putExtra("secretcode" , "fromapp1");
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);


    }

    private void onSwipeDown() {
        // Implement your action for swiping down
        try {
            Object service = getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");

            // If the StatusBarManager class is found, use reflection to call the expandNotificationsPanel method
            if (service != null && statusBarManager != null) {
                Method expandNotificationsPanel = statusBarManager.getMethod("expandNotificationsPanel");
                expandNotificationsPanel.invoke(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSwipeLeft() {
        // Implement your action for left swipe

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(cameraIntent);
        } else {
            Toast.makeText(this, "Camera app not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void onSwipeRight() {
        // Implement your action for right swipe
       Intent cameraIntent = new Intent(Intent.ACTION_DIAL);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(cameraIntent);
        } else {
            Toast.makeText(this, "Camera app not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDateTime() {


        handler.postDelayed(new Runnable() {

            @NonNull
            String getMonthName(int month)
            {
                    switch (month)
                    {
                        case 1:     return "January";
                        case 2:     return "February";
                        case 3:     return "March";
                        case 4:     return "April";
                        case 5:     return "May";
                        case 6:     return "June";
                        case 7:     return "July";
                        case 8:     return "August";
                        case 9:     return "September";
                        case 10:     return "October";
                        case 11:     return "November";
                        case 12:     return "December";


                    }

                    return "Nov";
            }
            @Override
            public void run() {
                // Get the current date and time
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
                int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY) ; // 24-hour format
                if (hour>12)
                {
                    hour -= 12;
                }
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);



//                // Create formatted strings

                String currentsecond = String.format(Locale.getDefault(), "%02d", second);
                String currenthour = String.format(Locale.getDefault(), "%02d", hour);
                String currentminute = String.format(Locale.getDefault(), "%02d", minute);
                String currentMonth = getMonthName(month);

                String current_Hour_Minute = String.format(Locale.getDefault(), "%2d:%02d ",hour , minute);
                String current_date_month_year = String.valueOf(day) + " " + currentMonth + " "+"'"+ String.valueOf(year).substring(2,4) ;


                hourTV.setText(String.valueOf(current_Hour_Minute));
                date_month_year.setText(current_date_month_year);
                binding.seconds.setText("."+currentsecond);




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
            Log.e("", "app name "+(ri.loadLabel(pm).toString()));
            app.setPackageName(ri.activityInfo.packageName);
            Log.e("", "package name "+ri.activityInfo.packageName);
            app.setIcon(ri.activityInfo.loadIcon(pm));
            Log.e("", "package icon "+ri.activityInfo.loadIcon(pm));

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


//    public boolean onTouchEvent(MotionEvent event) {
//
//        int action = MotionEventCompat.getActionMasked(event);
//
//
//        switch (action) {
//            case (MotionEvent.ACTION_DOWN):
//                Log.d("DEBUG_TAG", "Action was DOWN");
//                return true;
//            case (MotionEvent.ACTION_MOVE):
//                Log.d("DEBUG_TAG", "Action was MOVE");
//                return true;
//            case (MotionEvent.ACTION_UP):
//                Log.d("DEBUG_TAG", "Action was UP");
//                return true;
//            case (MotionEvent.ACTION_CANCEL):
//                Log.d("DEBUG_TAG", "Action was CANCEL");
//                return true;
//            case (MotionEvent.ACTION_OUTSIDE):
//                Log.d("DEBUG_TAG", "Movement occurred outside bounds " +
//                        "of current screen element");
//                return true;
//            default:
//                return super.onTouchEvent(event);
//        }
//
//
//    }



}