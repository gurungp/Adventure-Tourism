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
public class Offers extends Fragment {

    private ListView offerList;
    private Firebase offerRef;
    private Query offerQuery;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> hotelNames = new ArrayList<>();
    private ArrayList<Integer> offerIDs = new ArrayList<>();
    private ArrayList<Double> Offer_Price = new ArrayList<>();
    private ArrayList<String> Discount_Price = new ArrayList<>();
    private Bundle bundle;
    private int SportID;
    private TextView cartCount;
    private FirebaseApplication firebaseApplication;

    public Offers() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseApplication = (FirebaseApplication) getActivity().getApplicationContext();
        adapter = new listAdapter(getActivity(),R.layout.list_offers,hotelNames,offerIDs,Offer_Price,Discount_Price);
        bundle = getArguments();
        SportID = bundle.getInt("SportID");

        offerRef = new Firebase("https://adventure-tourism.firebaseio.com/Offer");
        offerQuery = offerRef.orderByChild("SportID").equalTo(String.valueOf(SportID));
        offerQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("Offers firebase", "Running");
                String hotelName = dataSnapshot.child("Hotel").getValue(String.class);
                Integer offerID=dataSnapshot.child("OfferID").getValue(Integer.class);
                double price = dataSnapshot.child("Price").getValue(Double.class);
                String discount = dataSnapshot.child("Discount").getValue(String.class);

                hotelNames.add(hotelName);
                offerIDs.add(offerID);
                Offer_Price.add(price);
                Discount_Price.add(discount);

                BasketItem basketItem = new BasketItem(offerID,ItemType.OFFER,false,String.valueOf(discount),price,hotelName);
                if(firebaseApplication.getOfferItems().containsKey(offerID)) {
                    // Already added
                }else{
                    firebaseApplication.addOfferItem(offerID,basketItem);
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

        View rootView = inflater.inflate(R.layout.fragment_offers, container, false);
        offerList = (ListView) rootView.findViewById(R.id.list_of_offers);
        cartCount = (TextView) getActivity().findViewById(R.id.cart_count);

        // Inflate the layout for this fragment
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        offerList.setAdapter(adapter);
    }

    private class listAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> names;
        private List<Integer> OfferId;
        private List<Double> OfferPrice;
        private List<String> discountValue;

        public listAdapter(Context context, int resource, List<String> objects,List<Integer> HotelID,List<Double> Price,List<String> discountValue) {
            super(context, resource, objects);
            names = objects;
            layout = resource;
            this.OfferId =HotelID;
            OfferPrice = Price;
            this.discountValue=discountValue;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView==null){
                LayoutInflater inflater=LayoutInflater.from(getContext());
                convertView=inflater.inflate(layout,parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.hotelName = (TextView) convertView.findViewById(R.id.hotel_name_offers);
                viewHolder.hotelName.setText(names.get(position));
                viewHolder.offerPrice = (TextView) convertView.findViewById(R.id.hotelPriceOffers);
                viewHolder.offerPrice.setText("Price: " + String.valueOf(OfferPrice.get(position)));
                viewHolder.discountVal = (TextView) convertView.findViewById(R.id.discount_offers);
                viewHolder.discountVal.setText("Discount: "+ String.valueOf(Discount_Price.get(position)) + " %");
                viewHolder.button = (ToggleButton) convertView.findViewById(R.id.add_basket_offers);
                viewHolder.button.setId(OfferId.get(position));
                if(firebaseApplication.getOfferItems().get(OfferId.get(position)).isAddedinList()==false){
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
                            firebaseApplication.addItems(viewHolder.button.getId(),ItemType.OFFER,true);
                            cartCount.setText((String.valueOf(firebaseApplication.getNumOfItems()))); // setting/adding the count of items in cart
                        }else{
                            Log.v("Button", "Add to basket" );
                            Log.v("HotelID", String.valueOf(viewHolder.button.getId()));
                            firebaseApplication.removeItems(viewHolder.button.getId(),ItemType.OFFER,false);
                            cartCount.setText((String.valueOf(firebaseApplication.getNumOfItems())));
                        }
                    }
                });
                // viewHolder.button.setId(position);

                convertView.setTag(viewHolder);
            }else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.button.setId(OfferId.get(position));
                mainViewHolder.hotelName.setText(names.get(position));
                mainViewHolder.offerPrice.setText("Price: " + String.valueOf(OfferPrice.get(position)));
                mainViewHolder.discountVal.setText("Discount: " + String.valueOf(Discount_Price.get(position)) + " %");
            }

            return convertView;
        }
        public class ViewHolder{
            TextView hotelName;
            TextView offerPrice;
            TextView discountVal;
            ToggleButton button;
        }
    }


}
