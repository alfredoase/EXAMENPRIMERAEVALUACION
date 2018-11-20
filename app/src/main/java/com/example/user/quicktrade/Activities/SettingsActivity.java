package com.example.user.quicktrade.Activities;

import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.user.quicktrade.R;

public class SettingsActivity extends AppCompatActivity {

    /**
     * The fragment to add the preferences
     */
    public static class AjustesFragments extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //Buscamos el archivo xml de las settings
            addPreferencesFromResource(R.xml.preferences);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsActivity.AjustesFragments()).commit();

        SharedPreferences datosPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
