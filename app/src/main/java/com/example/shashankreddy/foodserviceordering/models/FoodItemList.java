package com.example.shashankreddy.foodserviceordering.models;


import java.util.ArrayList;

public class FoodItemList {

    String foodId,foodName,foodReciepee,foodPrice,foodCat,foodImage;
    ArrayList<FoodItemList> mFoodItemLists;


    public FoodItemList(){
        mFoodItemLists = new ArrayList<>();
    }
    public FoodItemList(String foodId, String foodName, String foodReciepee, String foodPrice, String foodCat, String foodImage) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodReciepee = foodReciepee;
        this.foodPrice = foodPrice;
        this.foodCat = foodCat;
        this.foodImage = foodImage;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodReciepee() {
        return foodReciepee;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getFoodCat() {
        return foodCat;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public ArrayList<FoodItemList> getmFoodItemLists() {
        return mFoodItemLists;
    }

    public void setmFoodItemLists(ArrayList<FoodItemList> mFoodItemLists) {
        this.mFoodItemLists = mFoodItemLists;
    }
}
