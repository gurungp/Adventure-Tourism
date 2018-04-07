package com.example.adventure_tourism.adventuretousrism;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;



/**
 * A simple {@link Fragment} subclass.
 * Sport Detail
 */
public class Hotel_detail extends Fragment {

    TextView temp;
    Firebase ref;
    int SportID;
    Query queryref;
    String desc;
    Button hotel,offers,gallery;

    public Hotel_detail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ref=new Firebase("https://adventure-tourism.firebaseio.com/Location");
        Bundle bundle = getArguments();
        SportID = bundle.getInt("HotelID");
        Log.v("The id received is", String.valueOf(SportID));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hotel_detail, container, false);

        temp = (TextView) rootView.findViewById(R.id.hotel_detail);
        hotel = (Button) rootView.findViewById(R.id.hotel_button);
        offers = (Button) rootView.findViewById(R.id.offer_button);
        gallery = (Button) rootView.findViewById(R.id.gallery_button);

        setClickListeners();

        Log.v("Before query method", " running");
        query();
        // Inflate the layout for this fragment
        return rootView;

    }

    public void setClickListeners(){
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Offers offers=new Offers();
                Bundle args = new Bundle();
                args.putInt("SportID", SportID);
                offers.setArguments(args);
                android.support.v4.app.FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.map,offers);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hotel_list hotel_list=new Hotel_list();
                Bundle args = new Bundle();
                args.putInt("SportID", SportID);
                hotel_list.setArguments(args);
                android.support.v4.app.FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.map,hotel_list);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gallery gallery=new Gallery();
                Bundle args = new Bundle();
                args.putInt("SportID", SportID);
                gallery.setArguments(args);
                android.support.v4.app.FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.map,gallery);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }

    public void query(){
        queryref = ref.orderByChild("HotelID").equalTo(SportID);
        Log.v("Query method", " running");
        queryref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("child added or not"," this method ran ");
                desc = dataSnapshot.child("Description").getValue().toString();

                if(desc!=null){
                    temp.setText(desc);
                }else{
                    Log.v("textview", "null data in textview");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
