package com.comvigo.imlockandroid.Fragments;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.comvigo.imlockandroid.DAO;
import com.comvigo.imlockandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsChooserFragment extends Fragment {

    public static final String APP_PREFERENCES_SETTINGS = "Settings";
    public static final String APP_PREFERENCES_NAME = "";
    SharedPreferences mSettingsBlack, mSettingsWhite, mSettings;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_chooser, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(APP_PREFERENCES_SETTINGS, getActivity().MODE_PRIVATE);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES_SETTINGS, getActivity().MODE_PRIVATE);
        Log.d("!!!", mSettings.getString("userID",""));

        DAO dao = new DAO();
//        final List<SettingItem> settings =
        dao.getSettingsList(mSettings.getString("userID",""));
    //    Log.d("SETTTTTT", settings.toString());
//        ArrayAdapter<SettingItem> adapter = new ArrayAdapter<SettingItem>(getActivity(), R.layout.settings_item_layout , settings);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
//                                    long id) {
//                SharedPreferences.Editor editor = mSettings.edit();
//                editor.putString("settingsID", settings.get(position).getSettingID());
//                editor.commit();
//                getActivity().startService( new Intent(getActivity().getBaseContext(), WhiteListCreatorService.class));
//                getActivity().startService(new Intent(getActivity().getApplicationContext(), BlockService.class));
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.activity_container, new SettingsFragment());
//                fragmentTransaction.commit();
//            }
//        });
        return view;
    }




}
