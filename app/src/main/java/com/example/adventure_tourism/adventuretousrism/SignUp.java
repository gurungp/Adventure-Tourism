package com.example.adventure_tourism.adventuretousrism;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment {

    private Firebase signUpRef;
    private Button SignupButton;
    private EditText emailText,passwordText,passwordConfirm;

    public SignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        emailText = (EditText) rootView.findViewById(R.id.userEmailSignUp);
        passwordText = (EditText) rootView.findViewById(R.id.userPasswordSignup);
        passwordConfirm = (EditText) rootView.findViewById(R.id.userPasswordSignupConfirm);
        SignupButton = (Button) rootView.findViewById(R.id.signUpButton);

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String passwordRetype = passwordConfirm.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(getContext(), "Please enter Email", Toast.LENGTH_LONG).show();
                }else if(password.isEmpty() || passwordRetype.isEmpty()){
                    Toast.makeText(getContext(),"Please enter Password",Toast.LENGTH_LONG).show();
                }else{

                    if(password.compareTo(passwordRetype)==0){
                        ((MainActivity)getActivity()).SignUpUsers(emailText.getText().toString(),passwordText.getText().toString());
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else{
                        Toast.makeText(getContext(),"Password Doesn't Match",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        return rootView;
    }

}
