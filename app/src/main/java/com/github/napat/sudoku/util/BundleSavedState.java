package com.github.napat.sudoku.util;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class BundleSavedState extends View.BaseSavedState {

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new BundleSavedState(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new BundleSavedState[size];
        }
    };
    private Bundle bundle;

    public BundleSavedState(Parcel source) {
        super(source);
        bundle = source.readBundle();
    }

    public BundleSavedState(Parcel source, ClassLoader loader) {
        super(source, loader);
    }

    public BundleSavedState(Parcelable superState) {
        super(superState);
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeBundle(bundle);
    }
}
