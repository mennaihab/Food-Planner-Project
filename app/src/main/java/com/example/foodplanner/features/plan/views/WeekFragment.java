package com.example.foodplanner.features.plan.views;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.core.utils.NavigationUtils;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.MealCalenderHelper;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.helpers.mappers.PlanMealMapper;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;
import com.example.foodplanner.features.common.remote.impl.PlanBackupServiceImpl;
import com.example.foodplanner.features.common.repositories.PlanRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.common.views.NavigatorProvider;
import com.example.foodplanner.features.favourites.views.FavouritesFragment;
import com.example.foodplanner.features.plan.adapters.PlanClickListener;
import com.example.foodplanner.features.plan.adapters.WeekDayAdapter;
import com.example.foodplanner.features.common.helpers.CalendarPermissionHolder;
import com.example.foodplanner.features.plan.helpers.DayData;
import com.example.foodplanner.features.plan.models.PlanModelImpl;
import com.example.foodplanner.features.plan.presenter.PlanPresenter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WeekFragment extends Fragment implements WeekFragmentView {
    private static final String TAG = "WeekFragment";
    private NavigatorProvider navigatorProvider;
    private CalendarPermissionHolder calendarPermissionHolder;
    private PlanPresenter presenter;
    private RecyclerView recyclerView;
    private WeekDayAdapter parentItemAdapter;
    private static final String WEEK_DAY = "WEEK_DAY";
    private static final String SELECTED_DAY = "SELECTED_DAY";
    private LocalDate weekStart;


    public static WeekFragment newInstance(LocalDate startOfWeek) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEEK_DAY, startOfWeek);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigatorProvider = (NavigatorProvider) context;
        calendarPermissionHolder = (CalendarPermissionHolder) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weekStart = (LocalDate) requireArguments().getSerializable(WEEK_DAY);
        PlanRepository planRepository = new PlanRepository(
                AppDatabase.getInstance(requireContext()).planDayDAO(),
                AppDatabase.getInstance(requireContext()).mealItemDAO(),
                new PlanBackupServiceImpl(FoodPlannerApplication.from(requireContext()).getFirestore()),
                new PlanMealMapper(new BaseMapper<>(MealItem.class, MealItemEntity.class))
        );
        planRepository.setMealCalenderHelper(new MealCalenderHelper(requireActivity().getContentResolver(), calendarPermissionHolder));
        presenter = new PlanPresenter(
                this,
                new PlanModelImpl(
                        savedInstanceState,
                        FoodPlannerApplication.from(requireContext()).getAuthenticationManager(),
                        planRepository,
                        weekStart
                )
        );

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(requireActivity());
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
            public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent event) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavigationUtils.<MealItem>getValue(navigatorProvider, FavouritesFragment.SELECTED_MEAL).observe(getViewLifecycleOwner(), (Observer<MealItem>) mealItem -> {
            LocalDate date = NavigationUtils.<LocalDate>getValue(navigatorProvider, SELECTED_DAY).getValue();
            if (date != null) {
                presenter.addPlanMeal(mealItem, date);
            }
        });

        parentItemAdapter = new WeekDayAdapter(new PlanClickListener() {
            @Override
            public void onAddItem(LocalDate day) {
                NavigationUtils.setValue(navigatorProvider, SELECTED_DAY, day);
                navigatorProvider.getNavController().navigate(PlanFragmentDirections.actionPlanToPickItem());
            }

            @Override
            public void onRemoveItem(PlanMealItem item) {
                presenter.removePlanMeal(item);
            }

            @Override
            public void onClick(PlanMealItem item) {
                navigatorProvider.getNavController().navigate(PlanFragmentDirections
                        .actionGlobalToMeal(item.getMeal().getId()));
            }
        });


        recyclerView.setAdapter(parentItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.init(getViewLifecycleOwner());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updatePlan(Map<LocalDate, List<PlanMealItem>> products) {
        Log.d(TAG, "updatePlan: " + products);
        recyclerView.setVisibility(View.VISIBLE);
        List<DayData> itemList = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            LocalDate current = weekStart.plusDays(i);
            List<PlanMealItem> meals = new ArrayList<>();
            if (products.containsKey(current)) {
                meals.addAll(Objects.requireNonNull(products.get(current)));
            }
            DayData item = new DayData(current, meals);
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
    public void onRemoveFailure(PlanMealItem mealItem, Throwable error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

    }


}