package com.github.napat.sudoku.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.github.napat.sudoku.R;
import com.github.napat.sudoku.util.Music;
import com.github.napat.sudoku.view.SudokuView;

public class SudokuActivity extends AppCompatActivity {

    private SudokuView sudokuView1;
    private SudokuView sudokuView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Sudoku", "SudokuActivity...onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        // First time initial
        if (savedInstanceState == null) {
            int difficulty = getIntent().getIntExtra("difficulty", SudokuView.DIFFICULTY_EASY);

            sudokuView1 = (SudokuView) findViewById(R.id.sudokuView1);
            sudokuView1.setDifficulty(difficulty);
            sudokuView1.setEnableHint(getOptionHints(this));

            sudokuView2 = (SudokuView) findViewById(R.id.sudokuView2);
            sudokuView2.setDifficulty((difficulty == SudokuView.DIFFICULTY_CONTINUE)? difficulty: difficulty + 1);
            sudokuView2.setEnableHint(getOptionHints(this));

            Log.d("Sudoku", "SudokuActivity...onCreate...first time with " + difficulty);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.ten_cm, true);
        Music.setSoundLevel(100);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop();
    }

    public static boolean getOptionHints(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getResources().getString(R.string.setting_pref_hints)
                        , true);
    }
}
