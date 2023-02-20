package com.example.foodplanner.features.plan.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.core.utils.NavigationUtils;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.helpers.mappers.PlanMealMapper;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;
import com.example.foodplanner.features.common.repositories.PlanRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.plan.adapters.WeekDayAdapter;
import com.example.foodplanner.features.plan.helpers.DayData;
import com.example.foodplanner.features.plan.models.PlanModelImpl;
import com.example.foodplanner.features.plan.presenter.PlanPresenter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeekFragment extends Fragment implements WeekFragmentView {
    private static final String TAG = "WeekFragment";
    private PlanPresenter presenter;
    private RecyclerView recyclerView;
    WeekDayAdapter parentItemAdapter;
    private static final String WEEK_DAY = "WEEK_DAY";
    private static final String SELECTED_ITEM = "SELECTED_ITEM";
    private static final String SELECTED_DAY = "SELECTED_DAY";
    LocalDate weekStart;




    public static WeekFragment newInstance(LocalDate startOfWeek) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEEK_DAY, startOfWeek);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(
                0,
                ViewUtils.dpToPx(requireContext(), 16),
                0,
                ViewUtils.dpToPx(requireContext(), 16)
        );
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
            @Override
            public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent event) {}
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });
        return recyclerView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weekStart = (LocalDate) requireArguments().getSerializable(WEEK_DAY);
        presenter = new PlanPresenter(
                this,
                new PlanModelImpl(
                        savedInstanceState,
                        FoodPlannerApplication.from(requireContext()).getAuthenticationManager(),
                        new PlanRepository(
                                AppDatabase.getInstance(requireContext()).planDayDAO(),
                                new PlanMealMapper((new BaseMapper<>(MealItem.class, MealItemEntity.class))
                                )
                        ), weekStart));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavigationUtils.<MealItem>getValue(view,SELECTED_ITEM).observe(getViewLifecycleOwner(), (Observer<MealItem>) mealItem -> {
          LocalDate date =NavigationUtils.<LocalDate>getValue(view,SELECTED_DAY).getValue();
            presenter.addPlanMeal(mealItem,date);


        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        parentItemAdapter = new WeekDayAdapter(day->{
            NavigationUtils.setValue(view,SELECTED_DAY,day);
            Navigation.findNavController(view).navigate(PlanFragmentDirections.actionPlanToFavourites().setSelectionResultKey(SELECTED_ITEM));
        });
        recyclerView.setAdapter(parentItemAdapter);
        recyclerView.setLayoutManager(layoutManager);
        presenter.init(getViewLifecycleOwner());
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updatePlan(Map<LocalDate,List<PlanMealItem>> products) {
        recyclerView.setVisibility(View.VISIBLE);
       // ItemAdapter.updateList(products);
        List<DayData> itemList = new ArrayList<>(7);
        for (int i=0;i<7;i++) {
            LocalDate current = weekStart.plusDays(i);
            List<PlanMealItem> meals = new ArrayList<>();
            if(products.containsKey(current))
            {
                meals.addAll(products.get(current));
            }
            DayData item = new DayData(current,meals);
            //put all meals in the day
            itemList.add(item);
        }
        parentItemAdapter.submitList(itemList);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        Log.e(TAG, error.getLocalizedMessage(), error);
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemAdded(PlanMealItem planMealItem) {

    }

    @Override
    public void onItemRemoved(PlanMealItem planMealItem) {

    }

    @Override
    public void onAddFailure(MealItem mealItem, Throwable error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFailure(MealItem mealItem, Throwable error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

    }


}