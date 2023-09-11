package com.example.javaappversion16;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterAppList extends RecyclerView.Adapter<AdapterAppList.ViewHolder> {
    private ArrayList<StructAppInfo> appList;
    private Context context;

    public AdapterAppList(ArrayList<StructAppInfo> appList, Context context) {
        this.appList = appList;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.appdrawerview , parent , false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StructAppInfo appInfo = appList.get(position);

        holder.appNameTextView.setText(appInfo.getLabel());
        holder.appIconImageView.setImageDrawable(appInfo.getIcon());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the selected app
                openApp(appInfo.getPackageName());
            }
        });


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
