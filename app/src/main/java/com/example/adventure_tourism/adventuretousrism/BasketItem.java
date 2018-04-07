package com.example.adventure_tourism.adventuretousrism;


public class BasketItem {

    private int ID;
    private ItemType itemType;
    private boolean addedinList;
    private String Details;
    private double Price;
    private String HotelName;

    public BasketItem(int id, ItemType it,boolean b,String details,double price,String hotelName){
        ID=id;
        itemType = it;
        addedinList=b;
        Details=details;
        Price=price;
        HotelName=hotelName;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isAddedinList() {
        return addedinList;
    }

    public void setAddedinList(boolean addedinList) {
        this.addedinList = addedinList;
    }


}
