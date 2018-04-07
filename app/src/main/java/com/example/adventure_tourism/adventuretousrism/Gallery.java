package com.example.adventure_tourism.adventuretousrism;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.client.Firebase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Gallery extends Fragment {

    private int SportID;
    private Firebase galleryRef;
    private Query queryRef;
    private RecyclerView rc;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<Integer> imageIDs;
    private RecyclerView.Adapter imageAdapter;
    private Integer temp;

    public Gallery() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmaps = new ArrayList<>();
        imageIDs = new ArrayList<>();
        Bundle bundle=getArguments();
        SportID=bundle.getInt("SportID");
        Log.v("Before Firebase", "running");
        galleryRef = new Firebase("https://adventure-tourism.firebaseio.com/Image");
        queryRef = galleryRef.orderByChild("SportID").equalTo((SportID));
        imageAdapter = new ImageAdapter(bitmaps,imageIDs,getContext());
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(child.child("Image").getValue(String.class)!=null) {

                        String s = child.child("Image").getValue(String.class);
                        temp = child.child("ImageID").getValue(Integer.class);
                        byte[] decodedString = Base64.decode(s, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0 ,decodedString.length);
                        bitmaps.add(decodedByte);
                        imageIDs.add(temp);
                        Log.v("Bitmap size firebase",String.valueOf(bitmaps.size()));

                    }
                    else{
                        Log.v("image string", " is null");
                    }
                }
                Log.v("New Size of bitmaps", String.valueOf(bitmaps.size()));
                Log.v("New Size of image", String.valueOf(imageIDs.size()));

                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        Log.v("After Firebase", "running");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        rc = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        rc.addItemDecoration(new DividerItemDecoration(getActivity()));
        mLayoutManager = new LinearLayoutManager(getContext());
        rc.setLayoutManager(mLayoutManager);
        rc.setAdapter(imageAdapter);

        // Inflate the layout for this fragment
        return rootView;

    }

    public Bitmap getBitmap(int position){
        Log.v("Size of bitmaps", String.valueOf(bitmaps.size()));
        Log.v("bitmaps Position", String.valueOf(position));
        return bitmaps.get(position);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
