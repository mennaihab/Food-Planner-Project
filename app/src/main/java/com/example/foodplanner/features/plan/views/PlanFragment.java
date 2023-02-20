package com.example.foodplanner.features.plan.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.helpers.CalendarPermissionHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class PlanFragment extends Fragment implements CalendarPermissionHolder {
    private static final String PERMISSIONS_RESULT = "PERMISSIONS_RESULT";
    private static final String[] permissions = new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};

    private ViewPager2 viewPager;
    private boolean hasCalenderPermissions = false;

    public PlanFragment() {
        super(R.layout.fragment_plan);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ActivityResultLauncher<String[]> permissionsRequest = requireActivity().getActivityResultRegistry().register(PERMISSIONS_RESULT, getViewLifecycleOwner(), new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                hasCalenderPermissions = result.values().stream().allMatch(Predicate.isEqual(true));
            }
        });

        if (savedInstanceState != null) {
            hasCalenderPermissions = savedInstanceState.getBoolean(PERMISSIONS_RESULT, false);
        }
        if (!hasCalenderPermissions) {
            permissionsRequest.launch(permissions);
        }

        viewPager = view.findViewById(R.id.week_pager);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        LocalDate weekStart = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();

        WeekSlideAdapter adapter = new WeekSlideAdapter(weekStart);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(WeekSlideAdapter.START_INDEX, false);

        TextView tv = view.findViewById(R.id.week_tv);
        Button rightBtn = view.findViewById(R.id.rigth_btn);
        Button leftBtn = view.findViewById(R.id.left_btn);
        DateTimeFormatter sdf = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();

        Consumer<Integer> updateTextView = (position) -> {
            LocalDate selected = weekStart.plusWeeks(position - WeekSlideAdapter.START_INDEX);
            String formatted = sdf.format(selected);
            tv.setText(formatted);
        };

        updateTextView.accept(WeekSlideAdapter.START_INDEX);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTextView.accept(position);
            }
        });

        rightBtn.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        });
        leftBtn.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(PERMISSIONS_RESULT, hasCalenderPermissions);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean hasPermissions() {
        return hasCalenderPermissions;
    }

    private class WeekSlideAdapter extends FragmentStateAdapter {
        private static final int START_INDEX = Integer.MAX_VALUE / 2;
        private final LocalDate currentWeekStart;
        public WeekSlideAdapter(LocalDate currentWeekStart) {
            super(PlanFragment.this);
            this.currentWeekStart = currentWeekStart;
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            int normalizedPosition = position - START_INDEX;
            LocalDate weekStart = currentWeekStart.plus(normalizedPosition, ChronoUnit.WEEKS);
            return WeekFragment.newInstance(weekStart);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }
}



