package com.github.napat.sudoku.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.github.napat.sudoku.fragment.StatusAFragment;
import com.github.napat.sudoku.fragment.StatusBFragment;
import com.github.napat.sudoku.fragment.StatusCFragment;

public class StatusFragmentStateAdapter extends FragmentStateAdapter {

    private static final String TAG = "Sudoku";
    private static final int MAX_ITEM_COUNT = 3;

    public StatusFragmentStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return screenSlidePageFragment(position);
    }

    private Fragment screenSlidePageFragment(int position) {
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
    public int getItemCount() {
        return MAX_ITEM_COUNT;
    }
//    private FragmentManager fm;
//    private int behavior;
//
//    public StatusFragmentStateAdapter(@NonNull FragmentManager fm, int behavior) {
//        super(fm, behavior);
//        this.fm = fm;
//        this.behavior = behavior;
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        Log.d("xxxx", "getItem: " + position);
//
//        if (position >= this.behavior) {
//            Log.e(TAG, "Error getItem out of bound " + position + "/" + this.behavior);
//            return null;
//        }
//
//        switch (position) {
//            case 0: {
//                StatusAFragment tabAFragment = StatusAFragment.newInstance("A");
//                return tabAFragment;
//            }
//            case 1: {
//                StatusBFragment tabBFragment = StatusBFragment.newInstance("B");
//                return tabBFragment;
//            }
//            case 2: {
//                StatusCFragment tabCFragment = StatusCFragment.newInstance("C");
//                return tabCFragment;
//            }
//            default:
//                return null;
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return behavior;
//    }
}
