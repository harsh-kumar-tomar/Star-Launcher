    package com.example.javaappversion16;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.content.pm.ResolveInfo;
    import android.graphics.Color;
    import android.os.Bundle;
    import android.view.View;
    import android.view.Window;

    import java.util.ArrayList;
    import java.util.List;

    public class AppDrawer extends AppCompatActivity {

        public static final String EXTRA_APP_LIST = "app_list";
        RecyclerView recyclerView ;
        ArrayList<StructAppInfo> appList ;
        AdapterAppList adapterAppList;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_app_drawer);
            // Hide the notification bar
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#000000"));

            initialize();
            appList = getIntent().getParcelableArrayListExtra(AppDrawer.EXTRA_APP_LIST);


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) ;
            recyclerView = findViewById(R.id.recyclerView);
            adapterAppList = new AdapterAppList(appList , this);


            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapterAppList);

        }

        private void initialize() {
        }


    }