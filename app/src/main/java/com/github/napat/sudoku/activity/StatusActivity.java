package com.github.napat.sudoku.activity;

import android.os.Bundle;
import android.view.FrameStats;

import androidx.appcompat.app.AppCompatActivity;

import com.github.napat.sudoku.R;
import com.github.napat.sudoku.fragment.StatusFragment;

public class StatusActivity extends AppCompatActivity
        implements StatusFragment.FragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        initInstance();

        //PhotoItemDao dao = getIntent().getParcelableExtra("dao");

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentContainer, StatusFragment.newInstance("String from MoreInfoFragment.newInstance(...)"))
                    .commit();
        }
    }

    private void initInstance() {
    }

    @Override
    public void onUpItemClicked() {
        finish();
    }
}
