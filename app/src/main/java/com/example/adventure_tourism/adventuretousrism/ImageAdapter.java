package com.example.adventure_tourism.adventuretousrism;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Context context;
    ArrayList<Bitmap> bitmapList;
    ArrayList<Integer> ImageIDS=new ArrayList<>();


    public ImageAdapter(ArrayList<Bitmap> iv,ArrayList<Integer> imageID,Context context){
        bitmapList=iv;
        ImageIDS=imageID;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.galleryimage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imageView.setImageBitmap(bitmapList.get(position));
        holder.imageView.setId(ImageIDS.get(position));


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewer.class);
                intent.putExtra("ImageID", ImageIDS.get(position));
                context.startActivity(intent);
            }
        });

        /*String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/adventure";

        File dir=context.getCacheDir();

        //final File file = new File(dir, String.valueOf(ImageIDS.get(position)) + ".png");
       try{
           final File outputFile = new File("image",String.valueOf(ImageIDS.get(position)) + "image.png");
           FileOutputStream fOut=new FileOutputStream(outputFile);
           Bitmap bmp = bitmapList.get(position);
           bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
           fOut.flush();
           fOut.close();

       }catch (Exception e) {
           e.printStackTrace();
           Log.i(null, "Save file error!");
       }*/


    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageViewThumb);
        }

        public ImageView getImageView(){
            return imageView;
        }
    }


}
