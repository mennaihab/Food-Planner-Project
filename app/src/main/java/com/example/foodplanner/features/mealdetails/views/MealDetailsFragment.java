package com.example.foodplanner.features.mealdetails.views;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.foodplanner.R;
import com.example.foodplanner.features.common.entities.MealDetailsEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.MealDetailsRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.mealdetails.adapters.IngredientsAdapter;
import com.example.foodplanner.features.mealdetails.models.MealDetailsModelImpl;
import com.example.foodplanner.features.mealdetails.presenter.MealDetailsPresenter;
import com.google.android.material.chip.Chip;
import java.util.List;


public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private RecyclerView recyclerView;
    IngredientsAdapter listAdapter;
    ConstraintLayout constraintLayout;
    Flow flow;
    VideoView videoView;
    TextView mealName;
    TextView mealCategory;
    TextView mealArea;
    TextView mealInstructions;
    VideoView youtube;
    ImageView mealThumbnail;
    TextView mealSource;


    private static final String TAG = "MealDetailsFragment";
    private MealDetailsPresenter presenter;

    public MealDetailsFragment() {
        super(R.layout.fragment_meal_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ingredients_recycler);
        mealName = view.findViewById(R.id.meal_name_tv);
        mealArea = view.findViewById(R.id.country_tv);
        mealCategory = view.findViewById(R.id.category_tv);
        mealInstructions = view.findViewById(R.id.instructions_tv);
        mealSource = view.findViewById(R.id.source_tv);
        youtube = view.findViewById(R.id.videoView);
        mealSource = view.findViewById(R.id.source_tv);
        mealThumbnail = view.findViewById(R.id.meal_image);
        presenter = new MealDetailsPresenter(
                getViewLifecycleOwner(),
                this,
                new MealDetailsModelImpl(savedInstanceState,
                        new MealDetailsRepository(
                                MealRemoteService.create(),
                                AppDatabase.getInstance(requireContext()).mealDetailsDAO(),
                                new BaseMapper<>(Meal.class, MealDetailsEntity.class)
                        )
                )
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        IngredientsAdapter listAdapter;
        listAdapter = new IngredientsAdapter();
        recyclerView.setAdapter(listAdapter);
        //listAdapter.updateList(itemList());
        recyclerView.setLayoutManager(layoutManager);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        flow = view.findViewById(R.id.flow);
        VideoView videoView;
        videoView = view.findViewById(R.id.videoView);
        //videoView.setVideoPath("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

    }

   /* private List<Meal.Ingredient> itemList() {
        List<Meal.Ingredient> ChildItemList = new ArrayList<>(4);
        ChildItemList.add(new Meal.Ingredient("Butter", "20g"));
        ChildItemList.add(new Meal.Ingredient("Tomato", "40g"));
        ChildItemList.add(new Meal.Ingredient("Oil", "30g"));
        ChildItemList.add(new Meal.Ingredient("Ginger", "20g"));
        return ChildItemList;

    }
    */


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void updateMealDetails(Meal meal) {
        recyclerView.setVisibility(View.VISIBLE);
        listAdapter.updateList(meal.getIngredients());
        mealName.setText(meal.getName());
        mealCategory.setText(meal.getCategory());
        mealArea.setText(meal.getArea());
        mealInstructions.setText(meal.getInstructions());
        youtube.setVideoPath(meal.getYoutube());
        videoView.start();
        mealThumbnail.setImageBitmap(BitmapFactory.decodeFile(meal.getThumbnail()));
        mealSource.setText(meal.getSource());
        List<String> tags = meal.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Chip chip = new Chip(getContext());
            chip.setText(tags.get(i));
            chip.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            chip.setId(View.generateViewId());
            constraintLayout.addView(chip);
            flow.addView(chip);
        }
    }

    @Override
    public void onLoadFailure(Throwable error) {
        Log.e(TAG, error.getLocalizedMessage(), error);
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}