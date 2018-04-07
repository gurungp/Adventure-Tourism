package com.example.adventure_tourism.adventuretousrism;



import android.app.Application;
import android.util.Log;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseApplication extends Application {

    private HashMap<Integer, BasketItem> hotelBasket;
    private HashMap<Integer, BasketItem> offerBasket;
    private HashMap<Integer, BasketItem> packageBasket;
    private ArrayList<BasketItem> ItemList;
    private int numOfItems;

    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        hotelBasket = new HashMap<>();
        offerBasket = new HashMap<>();
        packageBasket = new HashMap<>();
        ItemList = new ArrayList<>();
        numOfItems = 0;

    }

    public void addItems(int index, ItemType itemType, boolean b) {
        numOfItems++;
        if (itemType == itemType.HOTEL) {
            hotelBasket.get(index).setAddedinList(b);
        } else if(itemType == itemType.OFFER){
            offerBasket.get(index).setAddedinList(b);
        }else{
            packageBasket.get(index).setAddedinList(b);
        }
    }

    public void removeItems(int index, ItemType itemType, boolean b) {

        if (numOfItems > 0) {
            numOfItems--;
        } else {
        }

        if (itemType == itemType.HOTEL) {
            hotelBasket.get(index).setAddedinList(b);
        } else if(itemType == itemType.OFFER){
            offerBasket.get(index).setAddedinList(b);
        }else{
            packageBasket.get(index).setAddedinList(b);
        }
    }


    public void addHotelItem(Integer i, BasketItem basketItem) {
        hotelBasket.put(i, basketItem);
    }

    public HashMap<Integer, BasketItem> getHotelItems() {
        return hotelBasket;
    }


    public void addOfferItem(Integer i, BasketItem basketItem) {
        offerBasket.put(i, basketItem);
    }

    public HashMap<Integer, BasketItem> getOfferItems() {
        return offerBasket;
    }

    public void addPackageItem(Integer i, BasketItem basketItem) {
        packageBasket.put(i, basketItem);
    }

    public HashMap<Integer, BasketItem> getPackageItems() {
        Log.v("Size of package", String.valueOf(packageBasket.size()));
        return packageBasket;
    }





    public ArrayList<Integer> getAllHotelID(){
        ArrayList<Integer> hotelIDS = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();
        ArrayList<BasketItem> temp2 = new ArrayList<>();

        temp.addAll(getHotelItems().keySet());
        temp2.addAll(getHotelItems().values());

        for(int i=0;i<temp.size();i++){
            if(temp2.get(i).isAddedinList()){
                hotelIDS.add(temp.get(i));
            }else{}
        }

        return hotelIDS;
    }
    public ArrayList<Integer> getAllOfferID(){
        ArrayList<Integer> offerIDS = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();
        ArrayList<BasketItem> temp2 = new ArrayList<>();

        temp.addAll(getOfferItems().keySet());
        temp2.addAll(getOfferItems().values());

        for(int i=0;i<temp.size();i++){
            if(temp2.get(i).isAddedinList()){
                offerIDS.add(temp.get(i));
            }else{}
        }
        return offerIDS;
    }
    public ArrayList<Integer> getAllPackageID(){
        ArrayList<Integer> packageIDS = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();
        ArrayList<BasketItem> temp2 = new ArrayList<>();

        temp.addAll(getPackageItems().keySet());
        temp2.addAll(getPackageItems().values());

        for(int i=0;i<temp.size();i++){
            if(temp2.get(i).isAddedinList()){
                packageIDS.add(temp.get(i));
            }else{}
        }
        return packageIDS;
    }


    public ArrayList<BasketItem> getAllHotelItems(){
        ArrayList<BasketItem> hotelItems = new ArrayList<>();
        ArrayList<BasketItem> temp = new ArrayList<>();
        temp.addAll(getHotelItems().values());
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).isAddedinList()){
                hotelItems.add(temp.get(i));
            }
        }
        return hotelItems;
    }
    public ArrayList<BasketItem> getAllOfferItems(){
        ArrayList<BasketItem> offerItems = new ArrayList<>();
        ArrayList<BasketItem> temp = new ArrayList<>();
        temp.addAll(getOfferItems().values());
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).isAddedinList()){
                offerItems.add(temp.get(i));
            }
        }        return offerItems;
    }
    public ArrayList<BasketItem> getAllPackageItems(){
        ArrayList<BasketItem> packageItems = new ArrayList<>();
        ArrayList<BasketItem> temp = new ArrayList<>();
        temp.addAll(getPackageItems().values());
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).isAddedinList()){
                packageItems.add(temp.get(i));
            }
        }
        return packageItems;
    }

    public void checkOut(){ //when the user check outs , set items added to basket to false
        for(BasketItem basketItem : hotelBasket.values()){
            basketItem.setAddedinList(false);
        }
        for(BasketItem basketItem : offerBasket.values()){
            basketItem.setAddedinList(false);
        }
        for(BasketItem basketItem : packageBasket.values()){
            basketItem.setAddedinList(false);
        }
    }

    public int getNumOfItems() {
        return numOfItems;
    }


}
