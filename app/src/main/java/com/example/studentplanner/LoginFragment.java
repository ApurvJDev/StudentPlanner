package com.example.studentplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        Button login;
        EditText loginEmail,loginPassword;
        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);
        login = view.findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEm = loginEmail.getText().toString();
                String loginPass = loginPassword.getText().toString();
                if(loginEm.equals("")){
                    loginEmail.setError("Please Enter Email");
                } else if (loginPass.equals("")) {
                    loginPassword.setError("Please Enter Password");
                } else {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    assert mainActivity != null;
                    mainActivity.signIn();
                }
            }
        });
        return view;
    }
}