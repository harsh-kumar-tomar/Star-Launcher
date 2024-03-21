package com.example.javaappversion16;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterAppList extends RecyclerView.Adapter<AdapterAppList.ViewHolder> {
    private ArrayList<StructAppInfo> appList;
    private Context context;
    String secretcode;

    public AdapterAppList(ArrayList<StructAppInfo> appList, Context context , String secretcode) {
        this.appList = appList;
        this.context = context;
        this.secretcode = secretcode;
    }
    public AdapterAppList(ArrayList<StructAppInfo> appList, Context context ) {
        this.appList = appList;
        this.context = context;
        this.secretcode = null;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.appdrawerview , parent , false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.appNameTextView.setText(appList.get(position).getLabel());
//        Log.e("", "before icon");
//        Bitmap a = getBitmapFromDrawable(appList.get(position).getIcon());
//        holder.appIconImageView.setImageDrawable(a);


        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(appList.get(position).getPackageName());

            Glide.with(context)
                    .load(icon)
                    .error(R.drawable.earth)
                    .into(holder.appIconImageView);

        }catch (Exception e)
        {
                    Log.e("", "error icon");

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the selected app

                if (secretcode.equals("secretcode")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedPosition", position);
                    ((AppDrawer) context).setResult(RESULT_OK, resultIntent);
                    ((AppDrawer) context).finish();
                    ((AppDrawer) context).overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                } else {
                    openApp(appList.get(position).getPackageName());
                }



            }
        });

        runAnimation(holder.itemView);

    }

    void runAnimation (View view)
    {
        Animation animation = AnimationUtils.loadAnimation(context , android.R.anim.slide_in_left);
        view.startAnimation(animation);
    }


    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIconImageView;
        TextView appNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appIconImageView = itemView.findViewById(R.id.appIconImageView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);

        }
    }

    private void openApp(String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            // Handle the case where the app cannot be opened
            Toast.makeText(context, "App cannot be opened", Toast.LENGTH_SHORT).show();
        }
    }
}
