package com.example.foodplanner.features.mealdetails.views;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.mealdetails.adapters.IngredientsAdapter;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;


public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private RecyclerView recyclerView;
    IngredientsAdapter listAdapter;
    private static final String TAG = "MealDetailsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ingredients_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        IngredientsAdapter listAdapter;
        listAdapter = new IngredientsAdapter();
        recyclerView.setAdapter(listAdapter);
        listAdapter.updateList(itemList());
        recyclerView.setLayoutManager(layoutManager);
        ConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout);
        Flow flow = view.findViewById(R.id.flow);
        for (int i = 0; i < 10; i++) {
            Chip chip = new Chip(getContext());
            chip.setText("food");
            chip.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            chip.setId(View.generateViewId());
            constraintLayout.addView(chip);
            flow.addView(chip);

        }
        VideoView videoView = view.findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video);
        videoView.start();
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

    }

    private List<Meal.Ingredient> itemList() {
        List<Meal.Ingredient> ChildItemList = new ArrayList<>(4);
        ChildItemList.add(new Meal.Ingredient("Butter", "20g"));
        ChildItemList.add(new Meal.Ingredient("Tomato", "40g"));
        ChildItemList.add(new Meal.Ingredient("Oil", "30g"));
        ChildItemList.add(new Meal.Ingredient("Ginger", "20g"));
        return ChildItemList;
    }



    @Override
    public void updateMealDetails(Meal meal) {
        recyclerView.setVisibility(View.VISIBLE);
        listAdapter.updateList(meal.getIngredients());
    }



    @Override
    public void onLoadFailure(Throwable error) {
        Log.e(TAG, error.getLocalizedMessage(), error);
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}