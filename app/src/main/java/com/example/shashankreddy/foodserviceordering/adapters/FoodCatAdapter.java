package com.example.shashankreddy.foodserviceordering.adapters;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shashankreddy.foodserviceordering.R;
import com.example.shashankreddy.foodserviceordering.appController.AppController;
import com.example.shashankreddy.foodserviceordering.fragments.FoodItemViewFragment;
import com.example.shashankreddy.foodserviceordering.models.FoodItemList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodCatAdapter extends RecyclerView.Adapter<FoodCatAdapter.MyViewHolder> {
    public static final int VEG=1;
    public static final int NON_VEG =2;
    ArrayList<FoodItemList> mFoodItemList = new ArrayList<>();
    private Context mContext;

    public FoodCatAdapter(Context mContext, int cat){
        this.mContext = mContext;
        if(cat == VEG)
            mFoodItemList = AppController.getInstance().getmFoodItemList().getVegFood();
        else if(cat == NON_VEG)
            mFoodItemList = AppController.getInstance().getmFoodItemList().getNonVegFood();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_catagery_layout,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        TextView foodName = holder.foodName;
        TextView foodReciepee = holder.foodReciepee,foodCat = holder.foodCat,foodPrice = holder.foodPrice;
        ImageView foodImage = holder.foodImage;

        foodName.setText(mFoodItemList.get(position).getFoodName());
        foodReciepee.setText(mFoodItemList.get(position).getFoodReciepee());
        foodPrice.setText(mFoodItemList.get(position).getFoodPrice());
        foodCat.setText(mFoodItemList.get(position).getFoodCat());
        CardView foodItemCardView = holder.foodItemCardView;
        Picasso.with(mContext).load(mFoodItemList.get(position).getFoodImage()).into(foodImage);

        foodItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(FoodCatAdapter.class.getSimpleName(),mFoodItemList.get(position).getFoodId()+"  "+position);
                FoodItemViewFragment foodItemViewFragment = FoodItemViewFragment.newInstance(mFoodItemList.get(position).getFoodId(),position);
                FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_replaceable,foodItemViewFragment).commit();
                fragmentTransaction.addToBackStack("FoodItemView");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoodItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView foodName,foodReciepee,foodCat,foodPrice;
        ImageView foodImage;
        CardView foodItemCardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            foodItemCardView = (CardView) itemView.findViewById(R.id.food_catageroy_layout_cardview);
            foodName = (TextView) itemView.findViewById(R.id.food_title);
            foodReciepee= (TextView) itemView.findViewById(R.id.food_recieppe);
            foodCat =(TextView) itemView.findViewById(R.id.food_cat);
            foodPrice =(TextView) itemView.findViewById(R.id.food_price);
            foodImage = (ImageView) itemView.findViewById(R.id.food_image_view);
        }
    }
}
