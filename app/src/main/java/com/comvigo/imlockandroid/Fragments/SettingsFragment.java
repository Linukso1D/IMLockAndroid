package com.comvigo.imlockandroid.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comvigo.imlockandroid.R;
import com.comvigo.imlockandroid.Services.BlockService;
import com.comvigo.imlockandroid.Services.WhiteListCreatorService;
import com.comvigo.imlockandroid.SettingsDAO;

public class SettingsFragment extends Fragment {

    View view;
    Button signOut, pauseFiltering, resumeFiltering;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        pauseFiltering = (Button) view.findViewById(R.id.button_pause);
        signOut = (Button) view.findViewById(R.id.button_signout);
        resumeFiltering = (Button) view.findViewById(R.id.button_resume);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_signout:
                        SettingsDAO settingsDAO = new SettingsDAO(getActivity());
                        settingsDAO.clear();
                        getActivity().stopService(new Intent(getActivity(), BlockService.class));
                        getActivity().stopService(new Intent(getActivity(), WhiteListCreatorService.class));
                        getActivity().finish();
                        break;
                    case R.id.button_pause:
                        getActivity().stopService(new Intent(getActivity(), BlockService.class));
                        getActivity().stopService(new Intent(getActivity(), WhiteListCreatorService.class));
                        pauseFiltering.setEnabled(false);
                        resumeFiltering.setEnabled(true);
                        break;
                    case R.id.button_resume:
                        getActivity().startService(new Intent(getActivity(), BlockService.class));
                        getActivity().startService(new Intent(getActivity(), WhiteListCreatorService.class));
                        pauseFiltering.setEnabled(true);
                        resumeFiltering.setEnabled(false);
                        break;
                }

            }
        };
        pauseFiltering.setOnClickListener(onClickListener);
        resumeFiltering.setOnClickListener(onClickListener);
        signOut.setOnClickListener(onClickListener);
        return view;
    }

}