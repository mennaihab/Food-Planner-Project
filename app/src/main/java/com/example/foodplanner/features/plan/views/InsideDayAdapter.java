package com.example.foodplanner.features.plan.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.Collections;
import java.util.List;

public class InsideDayAdapter extends RecyclerView.Adapter<InsideDayAdapter.InsideDayViewHolder>{

        List<MealItem> list=Collections.emptyList();

        Context context;


public InsideDayAdapter(List<MealItem> list,Context context){
        this.list=list;
        this.context=context;

        }

@Override
public InsideDayAdapter.InsideDayViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);

        // Inflate the layout
        View photoView=inflater.inflate(R.layout.inside_day_item,parent,false);
    InsideDayAdapter.InsideDayViewHolder viewHolder=new InsideDayAdapter.InsideDayViewHolder(photoView);
        return viewHolder;
        }




    @Override
public void onBindViewHolder(final InsideDayAdapter.InsideDayViewHolder viewHolder,final int position){
        viewHolder.name.setText(list.get(position).getName());
       // String img = list.get(position).getThumbnail();
        //int id = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
        //Drawable drawable = context.getResources().getDrawable(id);
        Glide.with(context).load(list.get(position).getThumbnail()).into(viewHolder.img);
       // viewHolder.img.setImageDrawable(drawable);
        }


@Override
public int getItemCount(){
        return list.size();
        }

@Override
public void onAttachedToRecyclerView(
        RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        }

    public class InsideDayViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView img;

        InsideDayViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
        }
    }

}



