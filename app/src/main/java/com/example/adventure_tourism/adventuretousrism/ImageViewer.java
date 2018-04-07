package com.example.adventure_tourism.adventuretousrism;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewer extends AppCompatActivity {

    Bitmap temp;
    ImageView imageView;
    PhotoViewAttacher photoViewAttacher;
    int ImageID;
    Firebase fireRef;
    Query queryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Intent intent = getIntent();
        ImageID = intent.getIntExtra("ImageID", 0);
        fireRef = new Firebase("https://adventure-tourism.firebaseio.com/Image");
        queryRef = fireRef.orderByChild("ImageID").equalTo(String.valueOf(ImageID));
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(child.child("Image").getValue(String.class)!=null) {

                        String s = child.child("Image").getValue(String.class);
                        byte[] decodedString = Base64.decode(s, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                         temp=decodedByte;
                        imageView = (ImageView) findViewById(R.id.imageViewer);
                        imageView.setImageBitmap(temp);
                        photoViewAttacher = new PhotoViewAttacher(imageView);
                    }
                    else{
                        Log.v("image string", " is null");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
