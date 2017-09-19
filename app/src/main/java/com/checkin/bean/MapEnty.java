package com.checkin.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/2/1.
 */
public class MapEnty implements Parcelable {

    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public MapEnty() {
    }

    protected MapEnty(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<MapEnty> CREATOR = new Parcelable.Creator<MapEnty>() {
        public MapEnty createFromParcel(Parcel source) {
            return new MapEnty(source);
        }

        public MapEnty[] newArray(int size) {
            return new MapEnty[size];
        }
    };
}
