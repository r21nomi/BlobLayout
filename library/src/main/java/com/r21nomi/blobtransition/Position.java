package com.r21nomi.blobtransition;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by Ryota Niinomi on 2016/11/13.
 */

public class Position implements Parcelable {

    private int left;
    private int top;
    private int right;
    private int bottom;

    public Position(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        this.left = location[0];
        this.top = location[1];
        this.right = this.left + view.getWidth();
        this.bottom = this.top + view.getHeight();
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getWidth() {
        return right - left;
    }

    public int getHeight() {
        return bottom - top;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.left);
        dest.writeInt(this.top);
        dest.writeInt(this.right);
        dest.writeInt(this.bottom);
    }

    protected Position(Parcel in) {
        this.left = in.readInt();
        this.top = in.readInt();
        this.right = in.readInt();
        this.bottom = in.readInt();
    }

    public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
        @Override
        public Position createFromParcel(Parcel source) {
            return new Position(source);
        }

        @Override
        public Position[] newArray(int size) {
            return new Position[size];
        }
    };
}
