package com.example.foodplanner.features.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.views.OnBackPressedListener;
import com.example.foodplanner.features.common.views.WindowPainter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MainActivity extends AppCompatActivity implements WindowPainter {

    private NavHostFragment navHostFragment;
    private NavController navController;
    private Toolbar toolbar;
    private BottomNavigationView navView;

    private int statusColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusColor = getWindow().getStatusBarColor();

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        navView = findViewById(R.id.nav_view);
        int[] homeFragmentsIds = new int[] {
                R.id.home_meal_fragment,
                R.id.home_search_fragment,
                R.id.home_favourites_fragment,
                R.id.home_plan_fragment
        };
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(homeFragmentsIds).build();
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            boolean decorShouldShow = Arrays.stream(homeFragmentsIds).anyMatch(id -> navDestination.getId() == id);
            setToolbarVisibility(decorShouldShow);
            setBottomNavVisibility(decorShouldShow);
            if (decorShouldShow) {
                setStatusBarVisibility(true);
                clearStatusBarColor();
            }
        });
    }

    @Override
    public void setToolbarVisibility(boolean visible) {
        toolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setBottomNavVisibility(boolean visible) {
        navView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setStatusBarVisibility(boolean visible) {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (visible) {
            windowInsetsController.show(WindowInsetsCompat.Type.statusBars());
        } else {
            windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
        }
    }

    @Override
    public void setStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    @Override
    public void clearStatusBarColor() {
        setStatusBarColor(statusColor);
    }

    @Override public void onBackPressed() {
        final Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if (!(currentFragment instanceof OnBackPressedListener) ||
                !((OnBackPressedListener) currentFragment).onBackPressed() ||
                !navController.popBackStack()) {
            super.onBackPressed();
        }
    }

}