package com.github.napat.sudoku.fragment;

import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.github.napat.sudoku.R;

public class SettingMessagesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_message, rootKey);

        if (true) {     //if(/*some feature*/) {
            EditTextPreference signaturePreference = findPreference("signature");
            // do something with this preference
            if (signaturePreference != null) {
                signaturePreference.setVisible(true);
            }
        }
    }
}
