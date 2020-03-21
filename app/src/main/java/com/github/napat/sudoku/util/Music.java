package com.github.napat.sudoku.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.github.napat.sudoku.R;

public class Music {
    private static MediaPlayer mPlayer = null;
    private final static int maxVolumeStep = 100;
    private static int currentVolume = 80;

    public static void play(Context context, int resId, boolean isLooping) {
        stop();

        if (getOptionMusic(context) == true) {
            mPlayer = MediaPlayer.create(context, resId);
            mPlayer.setLooping(isLooping);
            setSoundLevel(currentVolume);
            mPlayer.start();

            //Log.d("xxxxxx", "music is enable");
        } else {
            //Log.d("xxxxxx", "music is disable");
        }
    }

    public static void stop() {
        if (mPlayer == null)
            return;
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    public static void setSoundLevel(int percentLevel) {
        if (mPlayer == null)
            return;

        currentVolume = percentLevel;

        final float log1 = (float)(1 - (Math.log(maxVolumeStep - currentVolume)/Math.log(maxVolumeStep)));
        mPlayer.setVolume(log1, log1);      //set volume takes two parameter
    }

    public static boolean getOptionMusic(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getResources().getString(R.string.setting_pref_music)
                        , true);
    }
}

