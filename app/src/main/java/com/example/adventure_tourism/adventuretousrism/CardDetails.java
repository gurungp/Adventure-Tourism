package com.example.adventure_tourism.adventuretousrism;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardDetails extends AppCompatActivity {

    private EditText cardNumber,date,cvc;
    private FirebaseApplication firebaseApplication;
    private RelativeLayout relativeLayout;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();
        firebaseApplication = (FirebaseApplication) getApplicationContext();
        relativeLayout = (RelativeLayout) findViewById(R.id.CardDetailsLayout);
        cardNumber = (EditText)findViewById(R.id.CardNumber);
        date = (EditText) findViewById(R.id.date);
        cvc = (EditText) findViewById(R.id.cvc);
        final Button checkOut = (Button) findViewById(R.id.CheckOutButton);

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cardnum = cardNumber.getText().toString();
                String CVC = cvc.getText().toString();
                if(cardnum.isEmpty()){
                    Toast.makeText(CardDetails.this,"Please enter Card Number",Toast.LENGTH_LONG).show();
                }else if(date.getText().toString().isEmpty()){
                    Toast.makeText(CardDetails.this,"Please enter Date",Toast.LENGTH_LONG).show();
                }else if(CVC.isEmpty()){
                    Toast.makeText(CardDetails.this,"Please enter CVC Number",Toast.LENGTH_LONG).show();
                }else{
                   if(cardnum.length()!=16){
                       Toast.makeText(CardDetails.this,"Invalid Card Number",Toast.LENGTH_LONG).show();
                   }else{
                       if(CVC.length()!=3)
                       {
                           Toast.makeText(CardDetails.this,"Invalid CVC Number",Toast.LENGTH_LONG).show();
                       }else{
                           cardNumber.setVisibility(View.GONE);
                           date.setVisibility(View.GONE);
                           cvc.setVisibility(View.GONE);
                           checkOut.setVisibility(View.GONE);

                           firebaseApplication.checkOut();


                           TextView textView = new TextView(CardDetails.this);
                           textView.setText("CONGRATULATIONS!!");
                           textView.setGravity(Gravity.CENTER);
                           relativeLayout.addView(textView);

                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   Intent intent = new Intent();
                                   setResult(Activity.RESULT_OK);
                                   finish();
                               }
                           }, 2000);


                       }
                   }
                }
            }
        });

    }

}
