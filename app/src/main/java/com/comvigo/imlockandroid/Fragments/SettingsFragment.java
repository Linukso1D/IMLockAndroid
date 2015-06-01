package com.comvigo.imlockandroid.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comvigo.imlockandroid.R;

public class SettingsFragment extends Fragment {

    View view;
    Button signOut;
    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    SharedPreferences mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        signOut = (Button) view.findViewById(R.id.button_signout);
        signOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences mySharedPreferences = getActivity().
                        getSharedPreferences(APP_PREFERENCES_SETTINGS, getActivity().getApplicationContext().MODE_PRIVATE);
                mSettings = getActivity().getSharedPreferences(APP_PREFERENCES_SETTINGS,
                        getActivity().getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
            }
        });
        return view;
    }

}
