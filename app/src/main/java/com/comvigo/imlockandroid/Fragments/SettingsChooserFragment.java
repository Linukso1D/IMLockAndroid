package com.comvigo.imlockandroid.Fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.comvigo.imlockandroid.Connection;
import com.comvigo.imlockandroid.Models.SettingItem;
import com.comvigo.imlockandroid.R;
import com.comvigo.imlockandroid.Services.BlockService;
import com.comvigo.imlockandroid.Services.WhiteListCreatorService;
import com.comvigo.imlockandroid.SettingsAdapter;
import com.comvigo.imlockandroid.SettingsDAO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsChooserFragment extends Fragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_chooser, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        final SettingsDAO settingsDAO = new SettingsDAO(getActivity());
        Connection connection = new Connection();
        final List<SettingItem> settings = connection.getSettingsList(settingsDAO.getUserID());
        SettingsAdapter adapter = new SettingsAdapter(getActivity(), settings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                settingsDAO.setSettingsID(settings.get(position).getSettingID());
                getActivity().startService( new Intent(getActivity().getBaseContext(), WhiteListCreatorService.class));
                getActivity().startService(new Intent(getActivity().getApplicationContext(), BlockService.class));
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_container, new SettingsFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}