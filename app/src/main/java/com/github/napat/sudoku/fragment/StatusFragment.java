package com.github.napat.sudoku.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.github.napat.sudoku.R;
import com.github.napat.sudoku.adapter.StatusFragmentStateAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment implements View.OnClickListener {
    /**** Variables Zone ****/

    public interface FragmentListener {
        void onUpItemClicked();
    }

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    String[] tabLayoutStrings = new String[]{
            "Tab A",
            "Tab B",
            "Tab C"
    };
    int[] tabLayoutIcons = {
            R.drawable.ic_history_black_24dp,
            R.drawable.ic_favorite_black_24dp,
            R.drawable.ic_nearby_black_24dp
    };
    private Toolbar toolbar;
    private FloatingActionButton btnFab;

    /**** Functions Zone ****/

    public StatusFragment() {
        // Required empty public constructor
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StatusFragment.
     */
    public static StatusFragment newInstance(String param1) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBar(view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {

                // DO NOT DO THIS: may issue if we have many fragments in a activity
                //getActivity().finish();

                // Callback: call event listener in parent(Activity)
                FragmentListener listener = (FragmentListener) getActivity();
                listener.onUpItemClicked();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBar(@NonNull View view) {

        // Init ToolBar
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);  //view.setSupportActionBar(toolbar);

        // Enable Home Button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Init TabLayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        // Init ViewPager2
        viewPager = (ViewPager2) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new StatusFragmentStateAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabLayoutStrings[position]);
                tab.setIcon(tabLayoutIcons[position]);
            }
        }).attach();
    }

    private void init(Bundle savedInstanceState) {
        // Initialize Fragment level's variables
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        setHasOptionsMenu(true);
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here

        btnFab = (FloatingActionButton) rootView.findViewById(R.id.btnFab);
        btnFab.setOnClickListener(this);

        // Run only at first view created operation here
        if (savedInstanceState == null) {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFab:

                Snackbar.make(v, "Hello. I am Snackbar!", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "Button FAB UNDO on Snackbar is clicked!");
                            }
                        })
                        .show();

                return;
        }
    }
}
