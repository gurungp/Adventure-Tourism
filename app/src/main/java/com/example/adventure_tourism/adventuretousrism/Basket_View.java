package com.example.adventure_tourism.adventuretousrism;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Basket_View extends Fragment {

    private ListView Items_List;
    private ArrayList<Integer> allID;
    private ArrayList<BasketItem> allBasketItem;
    private ArrayList<String> hotelNames;
    private FirebaseApplication firebaseApplication;
    private ArrayAdapter<String> adapter;
    private TextView cartCount;
    private double TotalPrice;
    private Button checkOut;
    private FloatingActionButton floatingActionButton;

    public Basket_View() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TotalPrice = 0;
        firebaseApplication = (FirebaseApplication) getActivity().getApplicationContext();
        allID = new ArrayList<>();
        allBasketItem = new ArrayList<>();
        hotelNames = new ArrayList<>();

        /*---------------ADDING-ALL-BASKET-DATA-FROM-ALL-LISTS----------START-------*/
        allID.addAll(firebaseApplication.getAllHotelID());
        allID.addAll(firebaseApplication.getAllOfferID());
        allID.addAll(firebaseApplication.getAllPackageID());

        addHotelNames(firebaseApplication.getAllHotelItems());
        addHotelNames(firebaseApplication.getAllOfferItems());
        addHotelNames(firebaseApplication.getAllPackageItems());

        allBasketItem.addAll(firebaseApplication.getAllHotelItems());
        allBasketItem.addAll(firebaseApplication.getAllOfferItems());
        allBasketItem.addAll(firebaseApplication.getAllPackageItems());

        /*---------------ADDING-ALL-BASKET-DATA-FROM-ALL-LISTS----------END-------*/


        adapter = new listAdapter(getActivity(), R.layout.list_all_items,hotelNames,allID,allBasketItem);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot =  inflater.inflate(R.layout.fragment_basket__view, container, false);
        checkOut = (Button) viewRoot.findViewById(R.id.checkout);
        Items_List = (ListView) viewRoot.findViewById(R.id.list_of_all_items);
        cartCount = (TextView) getActivity().findViewById(R.id.cart_count);
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) cartCount.getLayoutParams();
        p.setAnchorId(View.NO_ID);
        floatingActionButton.setLayoutParams(p);
        floatingActionButton.setVisibility(View.INVISIBLE);

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allID.isEmpty()){
                    Toast.makeText(getContext(),"Basket Is Empty",Toast.LENGTH_LONG).show();
                }else{

                    Intent intent = new Intent(getActivity(), CardDetails.class);
                    getActivity().startActivityForResult(intent,1);
                }
            }
        });

        TextView textView=(TextView) viewRoot.findViewById(R.id.amount);
        textView.setText(String.format("%.2f",TotalPrice) + " Euro");
        return viewRoot;
    }

    public void addHotelNames(ArrayList<BasketItem> basketItems){
      for(int i=0;i<basketItems.size();i++){
        if(basketItems.get(i).isAddedinList()) {
            TotalPrice = TotalPrice + basketItems.get(i).getPrice();
            hotelNames.add(basketItems.get(i).getHotelName());
        }else{}
      }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


        Log.v("Size of basket", String.valueOf(allBasketItem.size()));
        Log.v("Size of ID", String.valueOf(allID.size()));
        Log.v("Size of hotelNames", String.valueOf(hotelNames.size()));

        Items_List.removeAllViewsInLayout();
        allBasketItem.clear();
        allID.clear();
        hotelNames.clear();
        adapter.clear();
        adapter.notifyDataSetChanged();

        CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        p.anchorGravity = Gravity.BOTTOM | Gravity.END;
        p.setAnchorId(R.id.fab);
        floatingActionButton.setLayoutParams(p);
        floatingActionButton.setVisibility(View.VISIBLE);


        Log.v("Size of basket", String.valueOf(allBasketItem.size()));
        Log.v("Size of ID", String.valueOf(allID.size()));
        Log.v("Size of hotelNames",String.valueOf(hotelNames.size()));

        Log.v("onDESTROY", "CALLED");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();



    }

    @Override
    public void onStart() {
        super.onStart();
        Items_List.setAdapter(adapter);
    }

    private class listAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> names;
        private List<Integer> allID;
        private List<BasketItem> basketItems;

        public listAdapter(Context context, int resource, List<String> objects,List<Integer> allid,List<BasketItem> basketItems) {
            super(context, resource, objects);
            names = objects;
            layout = resource;
            this.allID =allid;
            this.basketItems = basketItems;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView==null){
                LayoutInflater inflater=LayoutInflater.from(getContext());
                convertView=inflater.inflate(layout,parent, false);
                final ViewHolder viewHolder = new ViewHolder();

                viewHolder.Details = (TextView) convertView.findViewById(R.id.ALLITEMS_Detail);
                viewHolder.Details.setText(basketItems.get(position).getDetails());
                viewHolder.hotelName = (TextView) convertView.findViewById(R.id.hotel_name_ALLITEMS);
                viewHolder.hotelName.setText(names.get(position));
                viewHolder.hotelPrice = (TextView) convertView.findViewById(R.id.Price_ALLITEMS);
                viewHolder.hotelPrice.setText("Price: " + String.valueOf(basketItems.get(position).getPrice()));
                viewHolder.button = (ToggleButton) convertView.findViewById(R.id.add_basket_ALLITEMS);
                viewHolder.button.setId(allID.get(position));
                viewHolder.button.setVisibility(View.INVISIBLE);

                Log.v("New size of allID",String.valueOf(allID.size()));
                Log.v("New size of allitems",String.valueOf(allBasketItem.size()));
                Log.v("New position value",String.valueOf(position));

                if(allBasketItem.get(position).isAddedinList()==false){
                    viewHolder.button.setChecked(false);
                }else{
                    viewHolder.button.setChecked(true);
                }

                viewHolder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            firebaseApplication.addItems(viewHolder.button.getId(),ItemType.HOTEL,true);
                            cartCount.setText((String.valueOf(firebaseApplication.getNumOfItems()))); // setting/adding the count of items in cart
                        }else{

                            firebaseApplication.removeItems(viewHolder.button.getId(),ItemType.HOTEL,false);
                            cartCount.setText((String.valueOf(firebaseApplication.getNumOfItems())));
                        }
                    }
                });
                // viewHolder.button.setId(position);

                convertView.setTag(viewHolder);
            }else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.button.setVisibility(View.INVISIBLE);
                mainViewHolder.button.setId(allID.get(position));
                mainViewHolder.hotelName.setText(names.get(position));
                mainViewHolder.hotelPrice.setText("Price: " + String.valueOf(basketItems.get(position).getPrice()));
            }

            return convertView;
        }
        public class ViewHolder{
            TextView hotelName;
            TextView hotelPrice;
            TextView Details;
            ToggleButton button;
        }
    }
}
