package com.example.foodplanner.features.favourites.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.favourites.helpers.FavouritesAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.favourite_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        FavouritesAdapter ItemAdapter = new FavouritesAdapter(getContext());
        recyclerView.setAdapter(ItemAdapter);
        recyclerView.setLayoutManager(layoutManager);
        ItemAdapter.updateList((ItemList()));
    }

    private List<MealItem> ItemList() {
        List<MealItem> ChildItemList = new ArrayList<>();
        ChildItemList.add(new MealItem("food 1", "food1", "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg"));
        ChildItemList.add(new MealItem("food 2", "food2", "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg"));
        ChildItemList.add(new MealItem("food 3", "food3", "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg"));
        ChildItemList.add(new MealItem("food 4", "food4", "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg"));
        return ChildItemList;
    }
}