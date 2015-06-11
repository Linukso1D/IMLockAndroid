package com.comvigo.imlockandroid.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comvigo.imlockandroid.DAO;
import com.comvigo.imlockandroid.R;
import com.comvigo.imlockandroid.Services.BlockService;
import com.comvigo.imlockandroid.Services.WhiteListCreatorService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginFragment extends Fragment {

    Button send;
    EditText login, password;
    TextView retrivePassword;
    String login_value, password_value;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        login = (EditText) view.findViewById(R.id.login);
        password = (EditText) view.findViewById(R.id.password);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login_value = String.valueOf(login.getText());
                password_value = String.valueOf(password.getText());
                DAO dao = new DAO();
                String result = dao.validateUser(login_value,password_value);
                switch (result) {
                    case "-1":
                        Toast.makeText(getActivity().getApplication(), "Wrong login or password", Toast.LENGTH_LONG).show();
                        break;
                    case "0":
                        Toast.makeText(getActivity().getApplication(), "Server connection error", Toast.LENGTH_LONG).show();
                        break;
                    case "1":
                        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                        //     String imei = telephonyManager.getDeviceId();
                        String model = android.os.Build.MODEL;
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                        String formattedDate = df.format(c.getTime());
                        String comuterID = formattedDate + model.replaceAll(" ", "") ;//+ imei;
                        String userID = dao.getUser(login_value, comuterID);
                        getActivity().startService(new Intent(getActivity().getApplicationContext(), BlockService.class));
                        Intent intent = new Intent(getActivity().getBaseContext(), WhiteListCreatorService.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("comuterID", comuterID);
                        getActivity().startService(intent);

                        //To delete app icon
//                        PackageManager p = getPackageManager();
//                        ComponentName componentName = new ComponentName(getApplication(),
//                                com.comvigo.imlockandroid.MainActivity.class);
//                        p.setComponentEnabledSetting(componentName,
//                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.activity_container, new SettingsFragment());
                        fragmentTransaction.commit();
                        break;
                }
            }
        });
        retrivePassword = (TextView) view.findViewById(R.id.retrive_password);
        retrivePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_container, new RetrievePassword());
                fragmentTransaction.commit();
            }
        });
        return view;
    }




}
