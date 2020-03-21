package com.github.napat.sudoku.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.github.napat.sudoku.fragment.StatusAFragment;
import com.github.napat.sudoku.fragment.StatusBFragment;
import com.github.napat.sudoku.fragment.StatusCFragment;

public class StatusFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "Sudoku";
    private FragmentManager fm;
    private int behavior;

    public StatusFragmentStatePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.fm = fm;
        this.behavior = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("xxxx", "getItem: " + position);

        if (position >= this.behavior) {
            Log.e(TAG, "Error getItem out of bound " + position + "/" + this.behavior);
            return null;
        }

        switch (position) {
            case 0: {
                StatusAFragment tabAFragment = StatusAFragment.newInstance("A");
                return tabAFragment;
            }
            case 1: {
                StatusBFragment tabBFragment = StatusBFragment.newInstance("B");
                return tabBFragment;
            }
            case 2: {
                StatusCFragment tabCFragment = StatusCFragment.newInstance("C");
                return tabCFragment;
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return behavior;
    }
}
