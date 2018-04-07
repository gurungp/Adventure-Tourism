package com.example.adventure_tourism.adventuretousrism;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginUser extends Fragment {

    private Button loginButton, SignupButton;
    private EditText emailText,passwordText;

    public LoginUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_login_user, container, false);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        SignupButton = (Button) rootView.findViewById(R.id.signUp);
        emailText = (EditText) rootView.findViewById(R.id.userEmail);
        passwordText = (EditText) rootView.findViewById(R.id.userPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailText.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please enter Email",Toast.LENGTH_LONG).show();
                }else if(passwordText.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please enter Password",Toast.LENGTH_LONG).show();
                }else{
                    ((MainActivity)getActivity()).LogInUsers(emailText.getText().toString(),passwordText.getText().toString());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /* -----Fragment Processes ----- */
                SignUp signUp=new SignUp();
                android.support.v4.app.FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.map,signUp);
                trans.addToBackStack(null);
                trans.commit();
                /* -----Fragment Processes ----- */
            }
        });

        return  rootView;
    }

}
