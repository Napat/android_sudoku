package com.github.napat.sudoku.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.napat.sudoku.R;
import com.github.napat.sudoku.util.Music;
import com.github.napat.sudoku.view.SudokuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Sudoku";
    private BottomNavigationView bottomNavigationView;
    private Button btnContinue, btnNewGame, btnAbout, btnExit;
    private Button btnHelp;
    private Button btnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBar();
        initInstance();
    }

    private void initBar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void initInstance() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.item_favorite); // set default item

        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);

        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(this);

        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(this);

        btnHelp = (Button) findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(this);

        btnStatus = (Button) findViewById(R.id.btnStatus);
        btnStatus.setOnClickListener(this);

        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.actionSetting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                continueGame();
                return;
            case R.id.btnNewGame:
                openNewGameDialog();
                return;
            case R.id.btnAbout: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return;
            }
            case R.id.btnHelp: {
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return;
            }
            case R.id.btnStatus: {
                Intent intent = new Intent(this, StatusActivity.class);
                startActivity(intent);
                return;
            }
            case R.id.btnExit:
                finish();
                return;
        }
    }

    private void openNewGameDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Select Game Level");
        dialog.setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame(which);
            }
        });

        dialog.show();
    }

    private void startGame(int which) {
        //Log.d(DB_TAG, "Your option: " + which);
        int difficulty;
        Intent intent = new Intent(this, SudokuActivity.class);
        switch (which) {
            case 2:
                difficulty = SudokuView.DIFFICULTY_HARD;
                break;
            case 1:
                difficulty = SudokuView.DIFFICULTY_MEDIUM;
                break;
            case 0:
            default:
                difficulty = SudokuView.DIFFICULTY_EASY;
        }
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }

    private void continueGame() {
        int difficulty = SudokuView.DIFFICULTY_CONTINUE;
        Intent intent = new Intent(this, SudokuActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.all_about_you, true);
        Music.setSoundLevel(50);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_recent:
                Log.d(TAG, "item_recent");
                return true;
            case R.id.item_favorite:
                Log.d(TAG, "item_favorite");
                return true;
            case R.id.item_nearby:
                Log.d(TAG, "item_nearby");
                return true;
        }

        Log.d(TAG, "unknown item id: " + item.getItemId());
        return false;
    }
}
