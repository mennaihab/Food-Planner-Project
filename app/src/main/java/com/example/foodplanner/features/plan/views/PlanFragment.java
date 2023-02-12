package com.example.foodplanner.features.plan.views;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.foodplanner.R;

import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.views.OnBackPressedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class PlanFragment extends Fragment  implements OnBackPressedListener {


    private ViewPager2 viewPager;
    TextView tv;
    Button rightBtn;
    Button leftBtn;
    dayAdapter adapter;
    RecyclerView recyclerView;


    public PlanFragment() {
        super(R.layout.fragment_plan);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(new weekSlideAdapter(Arrays.asList(
                planPageFragment.newInstance(R.layout.fragment_week),
                planPageFragment.newInstance(R.layout.fragment_week),
                planPageFragment.newInstance(R.layout.fragment_week)

        )));

        RecyclerView ParentRecyclerViewItem = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dayAdapter parentItemAdapter = new dayAdapter(ParentItemList(),getContext());
        ParentRecyclerViewItem.setAdapter(parentItemAdapter);
        ParentRecyclerViewItem.setLayoutManager(layoutManager);


        rightBtn = view.findViewById(R.id.rigth_btn);
        leftBtn = view.findViewById(R.id.left_btn);


        String[] dt = {"2023-01-01"};  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt[0]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                final LayoutInflater factory = getLayoutInflater();
                final View textEntryView = factory.inflate(R.layout.fragment_week, null);
                tv = textEntryView.findViewById(R.id.date_tv);
                c.add(Calendar.DATE, 7);  // number of days to add
                dt[0] = sdf.format(c.getTime());  // dt is now the new date
                tv.setText(dt[0]);
            }
        });


        rightBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < viewPager.getRight())
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (viewPager.getCurrentItem() > viewPager.getLeft())
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        });
    }

        @Override
        public boolean onBackPressed () {
            if (viewPager.getCurrentItem() == 0) {
                return true;
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                return false;
            }
        }


    private List<DayData> ParentItemList()
    {
        List<DayData> itemList = new ArrayList<>();

        DayData item = new DayData(
                "Saturday", ChildItemList());
        itemList.add(item);
        DayData item1 = new DayData(
                "Sunday", ChildItemList());
        itemList.add(item1);
        DayData item2 = new DayData("Monday", ChildItemList());
        itemList.add(item2);
        DayData item3 = new DayData(
                "Tuesday", ChildItemList());
        itemList.add(item3);
        DayData item4 = new DayData(
                "Wednesday", ChildItemList());
        itemList.add(item);
        DayData item5 = new DayData(
                "Thursday", ChildItemList());
        itemList.add(item1);
        DayData item6 = new DayData("Friday", ChildItemList());
        itemList.add(item2);
        return itemList;
    }

    private List<MealItem> ChildItemList()
    {
        List<MealItem> ChildItemList = new ArrayList<>();

       ChildItemList.add(new MealItem("food 1","food1","https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F6%2F6d%2FGood_Food_Display_-_NCI_Visuals_Online.jpg&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FFood&tbnid=aXAU-PO_SNbH4M&vet=12ahUKEwiYtP3gtZD9AhUHVqQEHXulARAQMygAegUIARDoAQ..i&docid=F4fzcSFVcTK1kM&w=2700&h=1800&itg=1&q=food&ved=2ahUKEwiYtP3gtZD9AhUHVqQEHXulARAQMygAegUIARDoAQ"));
        ChildItemList.add(new MealItem("food 2","food2","https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F6%2F6d%2FGood_Food_Display_-_NCI_Visuals_Online.jpg&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FFood&tbnid=aXAU-PO_SNbH4M&vet=12ahUKEwiYtP3gtZD9AhUHVqQEHXulARAQMygAegUIARDoAQ..i&docid=F4fzcSFVcTK1kM&w=2700&h=1800&itg=1&q=food&ved=2ahUKEwiYtP3gtZD9AhUHVqQEHXulARAQMygAegUIARDoAQ"));
        ChildItemList.add(new MealItem("food 3","food3","https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F6%2F6d%2FGood_Food_Display_-_NCI_Visuals_Online.jpg&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FFood&tbnid=aXAU-PO_SNbH4M&vet=12ahUKEwiYtP3gtZD9AhUHVqQEHXulARAQMygAegUIARDoAQ..i&docid=F4fzcSFVcTK1kM&w=2700&h=1800&itg=1&q=food&ved=2ahUKEwiYtP3gtZD9AhUHVqQEHXulARAQMygAegUIARDoAQ"));
        ChildItemList.add(new MealItem("food 4","food4","https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F6%2F6d%2FGood_Food_Display_-_NCI_Visuals_Online.jpg&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FFood&tbnid=aXAU-PO_SNbH4M&vet=12ahUKEwiYtP3gtZD9AhUHVqQEHXulARAQMygAegUIARDoAQ..i&docid=F4fzcSFVcTK1kM&w=2700&h=1800&itg=1&q=food&ved=2ahUKEwiYtP3gtZD9AhUHVqQEHXulARAQMygAegUIARDoAQ"));
        return ChildItemList;
    }


    private class weekSlideAdapter extends FragmentStateAdapter {
            private final List<Fragment> fragmentList;

            public weekSlideAdapter(List<Fragment> items) {
                super(PlanFragment.this);
                fragmentList = items;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        }

        public static class planPageFragment extends Fragment {
            private static final String LAYOUT = "layout";
            public static planPageFragment newInstance(@LayoutRes int layoutResource) {
                planPageFragment fragment = new planPageFragment();
                Bundle args = new Bundle();
                args.putInt(LAYOUT, layoutResource);
                fragment.setArguments(args);
                return fragment;
            }

            @Nullable
            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                return inflater.inflate(requireArguments().getInt(LAYOUT), container, false);
            }
        }
    }



