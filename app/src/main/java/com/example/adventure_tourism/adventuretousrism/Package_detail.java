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
public class Package_detail extends Fragment {


    private ListView packageList;
    private Firebase packageRef;
    private Query packageQuery;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> hotelNames = new ArrayList<>();
    private ArrayList<Integer> packageIDs = new ArrayList<>();
    private ArrayList<Double> Package_Price = new ArrayList<>();
    private ArrayList<String> Package_Detail = new ArrayList<>();
    private Bundle bundle;
    private int PackageID;
    private TextView cartCount;
    private FirebaseApplication firebaseApplication;

    public Package_detail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseApplication = (FirebaseApplication) getActivity().getApplicationContext();
        adapter = new listAdapter(getActivity(),R.layout.list_package,hotelNames,packageIDs,Package_Price,Package_Detail);
        bundle = getArguments();
        PackageID = bundle.getInt("PackageID");

        packageRef = new Firebase("https://adventure-tourism.firebaseio.com/Package");
        packageQuery = packageRef.orderByChild("PackageID").equalTo(PackageID);
        packageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String hotelName = dataSnapshot.child("Hotel").getValue(String.class);
                Integer packageID = dataSnapshot.child("PackageID").getValue(Integer.class);
                double price = dataSnapshot.child("Price").getValue(Double.class);
                String details = dataSnapshot.child("Detail").getValue(String.class);

                hotelNames.add(hotelName);
                packageIDs.add(packageID);
                Package_Price.add(price);
                Package_Detail.add(details);

                BasketItem basketItem = new BasketItem(packageID,ItemType.PACKAGE,false,details,price,hotelName);
                if(firebaseApplication.getPackageItems().containsKey(packageID)){
                    // already added
                }else{
                    firebaseApplication.addPackageItem(packageID,basketItem);
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

        View rootView = inflater.inflate(R.layout.fragment_package_detail, container, false);
        packageList = (ListView) rootView.findViewById(R.id.list_of_package);
        cartCount = (TextView) getActivity().findViewById(R.id.cart_count);

        // Inflate the layout for this fragment
        return rootView;

    }
    @Override
    public void onStart() {
        super.onStart();
        packageList.setAdapter(adapter);
    }

    private class listAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> names;
        private List<Integer> PackageIDS;
        private List<Double> Package_price;
        private List<String> details;

        public listAdapter(Context context, int resource, List<String> objects,List<Integer> HotelID,List<Double> Price,List<String> details) {
            super(context, resource, objects);
            names = objects;
            layout = resource;
            this.PackageIDS =HotelID;
            Package_price = Price;
            this.details = details;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView==null){
                LayoutInflater inflater=LayoutInflater.from(getContext());
                convertView=inflater.inflate(layout,parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.hotelName = (TextView) convertView.findViewById(R.id.hotel_name_package);
                viewHolder.hotelName.setText(names.get(position));
                viewHolder.packagePrice = (TextView) convertView.findViewById(R.id.PackagePrice);
                viewHolder.packagePrice.setText("Price: " + String.valueOf(Package_price.get(position)));
                viewHolder.packageDetails = (TextView) convertView.findViewById(R.id.package_detail);
                viewHolder.packageDetails.setText(details.get(position));
                viewHolder.button = (ToggleButton) convertView.findViewById(R.id.add_basket_package);
                viewHolder.button.setId(PackageIDS.get(position));

                if(firebaseApplication.getPackageItems().get(PackageIDS.get(position)).isAddedinList()==false){
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
                            firebaseApplication.addItems(viewHolder.button.getId(),ItemType.PACKAGE,true);
                            cartCount.setText((String.valueOf(firebaseApplication.getNumOfItems()))); // setting/adding the count of items in cart
                        }else{
                            Log.v("Button", "Add to basket" );
                            Log.v("HotelID", String.valueOf(viewHolder.button.getId()));
                            firebaseApplication.removeItems(viewHolder.button.getId(),ItemType.PACKAGE,false);
                            cartCount.setText((String.valueOf(firebaseApplication.getNumOfItems())));
                        }
                    }
                });
                // viewHolder.button.setId(position);

                convertView.setTag(viewHolder);
            }else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.button.setId(PackageIDS.get(position));
                mainViewHolder.hotelName.setText(names.get(position));
                mainViewHolder.packagePrice.setText("Price: " + String.valueOf(Package_price.get(position)));
                mainViewHolder.packageDetails.setText(details.get(position));
            }

            return convertView;
        }
        public class ViewHolder{
            TextView hotelName;
            TextView packagePrice;
            TextView packageDetails;
            ToggleButton button;
        }
    }

}
