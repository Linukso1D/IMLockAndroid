package com.comvigo.imlockandroid.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comvigo.imlockandroid.Connection;
import com.comvigo.imlockandroid.R;
import com.comvigo.imlockandroid.SettingsDAO;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        login = (EditText) view.findViewById(R.id.login);
        password = (EditText) view.findViewById(R.id.password);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login_value = String.valueOf(login.getText());
                password_value = String.valueOf(password.getText());
                Connection connection = new Connection();
                String result = connection.validateUser(login_value,password_value);
                switch (result) {
                    case "-1":
                        Toast.makeText(getActivity().getApplication(), "Wrong login or password", Toast.LENGTH_LONG).show();
                        break;
                    case "0":
                        Toast.makeText(getActivity().getApplication(), "Server connection error", Toast.LENGTH_LONG).show();
                        break;
                    case "1":
                        String model = android.os.Build.MODEL;
                        SettingsDAO settingsDAO= new SettingsDAO(getActivity());
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                        String formattedDate = df.format(c.getTime());
                        String comuterID = formattedDate + model.replaceAll(" ", "");
                        String userID = connection.getUser(login_value, comuterID);
                        settingsDAO.setUserID(userID);
                        settingsDAO.setComuterID(comuterID);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.activity_container, new SettingsChooserFragment());
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