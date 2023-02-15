package com.example.foodplanner.features.mealdetails.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.adapters.IngredientsListAdapter;
import com.example.foodplanner.features.search.helpers.SearchCriteria;
import com.example.foodplanner.features.search.views.SearchFragmentDirections;


public class MealDetailsFragment extends Fragment {

    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ingredients_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        IngredientsListAdapter listAdapter = new IngredientsListAdapter(area -> Navigation.findNavController(view).navigate(
                MealDetailsFragmentDirections.actionMealDetailsFragmentToSearchResultsFragment(new SearchCriteria(SearchCriteria.Type.INGREDIENT, area.getName()))
        ),R.layout.item_ingredient);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}