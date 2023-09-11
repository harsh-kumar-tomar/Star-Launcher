package com.example.javaappversion16;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StructAppInfo implements Parcelable {

    private String label;
    private String packageName;
    private Drawable icon;
    public StructAppInfo()
    {

    }

    protected StructAppInfo(Parcel in) {
        label = in.readString();
        packageName = in.readString();
    }

    public static final Creator<StructAppInfo> CREATOR = new Creator<StructAppInfo>() {
        @Override
        public StructAppInfo createFromParcel(Parcel in) {
            return new StructAppInfo(in);
        }

        @Override
        public StructAppInfo[] newArray(int size) {
            return new StructAppInfo[size];
        }
    };

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(packageName);
    }
}
