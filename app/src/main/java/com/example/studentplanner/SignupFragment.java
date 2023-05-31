package com.example.studentplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SignupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup,container,false);
        Button signUp;
        EditText signupEmail,signupPassword,signupConfirm;
        signupEmail = view.findViewById(R.id.signupEmail);
        signupPassword = view.findViewById(R.id.signupPassword);
        signupConfirm = view.findViewById(R.id.signupConfirm);
        signUp = view.findViewById(R.id.btnSignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String snEmail = signupEmail.getText().toString();
                String snPass = signupPassword.getText().toString();
                String snConf = signupConfirm.getText().toString();
                if(snEmail.equals("")){
                    signupEmail.setError("Enter Email");
                } else if (snPass.equals("")) {
                    signupPassword.setError("Enter Password");
                } else if (snConf.equals("")) {
                    signupConfirm.setError("Re-enter Password");
                } else if (!snPass.equals(snConf)) {
                    signupConfirm.setError("Password Not Matching");
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