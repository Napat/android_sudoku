package com.github.napat.sudoku.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.github.napat.sudoku.R;
import com.github.napat.sudoku.view.SudokuView;

public class SudokuKeypadDialog extends Dialog {
    protected static final String TAG = "Sudoku";
    private final View keys[] = new View[10];
    private final int useds[];
    private final SudokuView sudokuView;

    public SudokuKeypadDialog(@NonNull Context context, int useds[], SudokuView sudokuView) {
        super(context);
        this.useds = useds;
        this.sudokuView = sudokuView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.keypad_title);
        setContentView(R.layout.dialog_sudoku_keypad_grid);

        initInstance();
    }

    private void initInstance() {
        keys[0] = findViewById(R.id.keypadClear);
        keys[1] = findViewById(R.id.keypad1);
        keys[2] = findViewById(R.id.keypad2);
        keys[3] = findViewById(R.id.keypad3);
        keys[4] = findViewById(R.id.keypad4);
        keys[5] = findViewById(R.id.keypad5);
        keys[6] = findViewById(R.id.keypad6);
        keys[7] = findViewById(R.id.keypad7);
        keys[8] = findViewById(R.id.keypad8);
        keys[9] = findViewById(R.id.keypad9);

        for ( int element : useds) {
            keys[element].setVisibility(View.INVISIBLE);
        }

        for ( int i = 0 ; i < keys.length; i++) {
            final int keypadValue = i;      // TODO: Code Smell
            keys[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnResult(keypadValue);  // TODO: rewrite this onClick to switch case
                }
            });
        }
    }

    // D-pad hardware keyboard support function
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        int tile = 0;

        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE:
                tile = 0;
                break;
        case KeyEvent.KEYCODE_1:
                tile = 1;
                break;
        case KeyEvent.KEYCODE_2:
                tile = 2;
                break;
        case KeyEvent.KEYCODE_3:
                tile = 3;
                break;
        case KeyEvent.KEYCODE_4:
                tile = 4;
                break;
        case KeyEvent.KEYCODE_5:
                tile = 5;
                break;
        case KeyEvent.KEYCODE_6:
                tile = 6;
                break;
        case KeyEvent.KEYCODE_7:
                tile = 7;
                break;
        case KeyEvent.KEYCODE_8:
                tile = 8;
                break;
        case KeyEvent.KEYCODE_9:
                tile = 9;
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }

        Log.d(TAG, "onKeyDown with " + tile);
        returnResult(tile);
        return true;
    }

    private void returnResult(int keypadValue) {
        Log.d(TAG, "returnResult with " + keypadValue);

        // Bad input will be ignore
        if (isValidUsedTile(keypadValue) == false) {
            return;
        }

        sudokuView.setSelectTile(keypadValue);
        dismiss();  // close this Dialog
    }

    private boolean isValidUsedTile(int tile) {
        for (int used : this.useds) {
            if (tile == used)
                return false;
        }
        return true;
    }
}
