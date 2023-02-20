package com.example.foodplanner.features.mealdetails.views;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.core.utils.GeneralUtils;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.MealEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.MealDetailsRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.mealdetails.adapters.IngredientsAdapter;
import com.example.foodplanner.features.mealdetails.models.MealDetailsModelImpl;
import com.example.foodplanner.features.mealdetails.presenter.MealDetailsPresenter;
import com.google.android.material.chip.Chip;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;


public class MealDetailsFragment extends Fragment implements MealDetailsView {
    private static final String TAG = "MealDetailsFragment";

    private RecyclerView recyclerView;
    private IngredientsAdapter listAdapter;
    private ConstraintLayout mealDetailsRoot;
    private ProgressBar loader;
    private TextView errorTv;
    private Flow flow;
    private TextView mealName;
    private TextView mealCategory;
    private TextView mealArea;
    private TextView mealInstructions;
    private YouTubePlayerView youtube;
    private ImageView mealThumbnail;
    private TextView mealSource;
    private MealDetailsPresenter presenter;

    public MealDetailsFragment() {
        super(R.layout.fragment_meal_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String mealId = MealDetailsFragmentArgs.fromBundle(requireArguments()).getMealId();
        mealDetailsRoot = view.findViewById(R.id.meal_details);
        loader = view.findViewById(R.id.meal_loader);
        errorTv = view.findViewById(R.id.meal_error_tv);
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
                        mealId,
                        new MealDetailsRepository(
                                MealRemoteService.create(),
                                AppDatabase.getInstance(requireContext()).mealDetailsDAO(),
                                new BaseMapper<>(Meal.class, MealEntity.class)
                        )
                )
        );
        listAdapter = new IngredientsAdapter();
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        flow = view.findViewById(R.id.flow);
        getViewLifecycleOwner().getLifecycle().addObserver(youtube);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void updateMealDetails(Meal meal) {
        mealDetailsRoot.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
        listAdapter.updateList(meal.getIngredients());
        mealName.setText(meal.getName());
        mealCategory.setText(meal.getCategory());
        mealArea.setText(meal.getArea());
        mealInstructions.setText(meal.getInstructions());
        setupVideoPlayer(meal.getYoutube());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        ViewUtils.loadImageInto(meal.getThumbnail(), mealThumbnail, width);
        mealSource.setText(meal.getSource());
        List<String> tags = meal.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Chip chip = new Chip(getContext());
            chip.setText(tags.get(i));
            chip.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            chip.setId(View.generateViewId());
            mealDetailsRoot.addView(chip);
            flow.addView(chip);
        }
    }

    private void setupVideoPlayer(String youtubeUrl) {
        final String videoId;
        if (youtubeUrl != null) {
            Uri uri = Uri.parse(youtubeUrl);
            videoId = uri.getQueryParameter("v");
        } else {
            videoId = null;
        }
        if (videoId != null) {
            if (Boolean.TRUE.equals(youtube.getTag())) {
                return;
            }
            youtube.setVisibility(View.VISIBLE);
            youtube.setTag(true);
            youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0);
                }
            });
        } else {
            youtube.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadFailure(Throwable error) {
        mealDetailsRoot.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);
        errorTv.setText(GeneralUtils.getErrorMessage(error));
        Toast.makeText(getActivity(), GeneralUtils.getErrorMessage(error), Toast.LENGTH_SHORT).show();
    }
}