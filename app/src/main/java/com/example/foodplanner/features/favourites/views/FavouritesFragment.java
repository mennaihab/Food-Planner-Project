package com.example.foodplanner.features.favourites.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.favourites.helpers.FavouritesAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    public FavouritesFragment() {
        super(R.layout.fragment_favourites);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.items_list);
        recyclerView.addItemDecoration(
                new MarginItemDecoration(ViewUtils.dpToPx(requireContext(), 16), 2, LinearLayoutManager.VERTICAL)
        );
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        FavouritesAdapter ItemAdapter = new FavouritesAdapter();
        recyclerView.setAdapter(ItemAdapter);
        recyclerView.setLayoutManager(layoutManager);
        ItemAdapter.updateList(ItemList());
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