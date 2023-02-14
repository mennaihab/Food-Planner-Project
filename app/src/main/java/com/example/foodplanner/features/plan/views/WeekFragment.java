package com.example.foodplanner.features.plan.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.plan.helpers.DayData;
import com.example.foodplanner.features.plan.adapters.WeekDayAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekFragment extends Fragment {

    private static final String WEEK_DAY = "WEEK_DAY";

    private RecyclerView recyclerView;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LocalDate weekStart = (LocalDate) requireArguments().getSerializable(WEEK_DAY);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        WeekDayAdapter parentItemAdapter = new WeekDayAdapter();
        parentItemAdapter.submitList(parentItemList(weekStart));
        recyclerView.setAdapter(parentItemAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private List<DayData> parentItemList(LocalDate weekStart) {
        List<DayData> itemList = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            LocalDate current = weekStart.plusDays(i);
            DayData item = new DayData(current, childItemList());
            itemList.add(item);
        }
        return itemList;
    }

    private List<MealItem> childItemList() {
        List<MealItem> ChildItemList = new ArrayList<>(4);
        ChildItemList.add(new MealItem("food 1", "food1", "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg"));
        ChildItemList.add(new MealItem("food 2", "food2", "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg"));
        ChildItemList.add(new MealItem("food 3", "food3", "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg"));
        ChildItemList.add(new MealItem("food 4", "food4", "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg"));
        return ChildItemList;
    }
}