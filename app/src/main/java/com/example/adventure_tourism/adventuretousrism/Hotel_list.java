package com.example.adventure_tourism.adventuretousrism;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Hotel_list extends Fragment {

    private ListView hotelList;
    private Firebase hotelRef;
    private Query hotelQuery;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> hotelNames = new ArrayList<>();
    private ArrayList<Integer> hotelIDs = new ArrayList<>();
    private ArrayList<Double> Hotel_Price = new ArrayList<>();
    private Bundle bundle;
    private int SportID;
    private TextView cartCount;
    private FirebaseApplication firebaseApplication;

    public Hotel_list() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseApplication = (FirebaseApplication) getActivity().getApplicationContext();
        adapter=new listAdapter(getActivity(), R.layout.list_items, hotelNames,hotelIDs,Hotel_Price);
        bundle = getArguments();
        SportID = bundle.getInt("SportID");
        Log.v("SportID is ", String.valueOf(SportID));

        hotelRef=new Firebase("https://adventure-tourism.firebaseio.com/Hotel_List");
        hotelQuery = hotelRef.orderByChild("SportID").equalTo(SportID);
        hotelQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String hotelName = (dataSnapshot.child("Name").getValue(String.class));
                Integer hotelid = dataSnapshot.child("HotelID").getValue(Integer.class);
                double price = dataSnapshot.child("Price").getValue(Double.class);
                hotelNames.add(hotelName);
                hotelIDs.add(hotelid);
                Hotel_Price.add(price);

                BasketItem basketItem = new BasketItem(hotelid,ItemType.HOTEL,false,"",price,hotelName);

                if(firebaseApplication.getHotelItems().containsKey(hotelid)){
                    Log.v("Added Already","Yes");
                }
                else{
                    Log.v("Added Already", "No");
                    firebaseApplication.addHotelItem(hotelid,basketItem);

                }

                adapter.notifyDataSetChanged();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hotel_list, container, false);
        hotelList = (ListView) rootView.findViewById(R.id.list_of_hotels);
        cartCount = (TextView) getActivity().findViewById(R.id.cart_count);
        Log.v("Before query method", " running");
        Log.v("Size of hotellist", String.valueOf(hotelNames.size()));
        // Inflate the layout for this fragment
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.v("Size of hotellist", String.valueOf(hotelNames.size()));
        hotelList.setAdapter(adapter);
    }

    private class listAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> names;
        private List<Integer> HotelId;
        private List<Double> HotelPrice;
        public listAdapter(Context context, int resource, List<String> objects,List<Integer> HotelID,List<Double> Price) {
            super(context, resource, objects);
            names = objects;
            layout = resource;
            this.HotelId=HotelID;
            HotelPrice = Price;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView==null){
                LayoutInflater inflater=LayoutInflater.from(getContext());
                convertView=inflater.inflate(layout,parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.hotelName = (TextView) convertView.findViewById(R.id.hotel_name);
                viewHolder.hotelName.setText(names.get(position));
                viewHolder.hotelPrice = (TextView) convertView.findViewById(R.id.hotelPrice);
                viewHolder.hotelPrice.setText("Price: " + String.valueOf(HotelPrice.get(position)));
                viewHolder.button = (ToggleButton) convertView.findViewById(R.id.add_basket);
                viewHolder.button.setId(HotelId.get(position));
                Log.v("Value of position", String.valueOf(position));
                if(firebaseApplication.getHotelItems().get(HotelId.get(position)).isAddedinList()==false){
                    viewHolder.button.setChecked(false);
                }else{
                    viewHolder.button.setChecked(true);
                }

                viewHolder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            Log.v("Button", "Remove from basket" );
                            Log.v("HotelID", String.valueOf(viewHolder.button.getId()));
                            firebaseApplication.addItems(viewHolder.button.getId(),ItemType.HOTEL,true);
                            cartCount.setText((String.valueOf(firebaseApplication.getNumOfItems()))); // setting/adding the count of items in cart
                        }else{
                            Log.v("Button", "Add to basket" );
                            Log.v("HotelID", String.valueOf(viewHolder.button.getId()));
                            firebaseApplication.removeItems(viewHolder.button.getId(),ItemType.HOTEL,false);
                            cartCount.setText((String.valueOf(firebaseApplication.getNumOfItems())));
                        }
                    }
                });
               // viewHolder.button.setId(position);

                convertView.setTag(viewHolder);
            }else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.button.setId(HotelId.get(position));
                mainViewHolder.hotelName.setText(names.get(position));
                mainViewHolder.hotelPrice.setText("Price: " + String.valueOf(HotelPrice.get(position)));
            }

            return convertView;
        }
        public class ViewHolder{
            TextView hotelName;
            TextView hotelPrice;
            ToggleButton button;
        }
    }


}
