package com.example.foodplanner.features.main;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.BuildCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.R;
import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.features.common.views.LoadingFragmentDirections;
import com.example.foodplanner.features.common.views.OnBackPressedListener;
import com.example.foodplanner.features.common.views.OperationSink;
import com.example.foodplanner.features.common.views.WindowPainter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.subjects.CompletableSubject;

@OptIn(markerClass = BuildCompat.PrereleaseSdkCheck.class)
public class MainActivity extends AppCompatActivity implements WindowPainter, OperationSink {
    private static final String TAG = "MainActivity";

    private NavHostFragment navHostFragment;
    private NavController navController;
    private Toolbar toolbar;
    private BottomNavigationView navView;

    private int statusColor;
    private Drawable toolbarBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusColor = getWindow().getStatusBarColor();
        Log.d(TAG, "color " + statusColor);

        toolbar = findViewById(R.id.main_toolbar);
        toolbarBG = toolbar.getBackground();

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

        navView.setOnItemSelectedListener(item -> {
            // TODO handle with regard to authentication
            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        if (BuildCompat.isAtLeastT()) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> {
                        Log.d(TAG, "OnBackInvokedDispatcher: onBackPressed");
                        onBackPressed();
                    }
            );
        }
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

    @Override
    public void setToolbarColor(int color) {
        toolbar.setBackground(new ColorDrawable(color));
    }
    @Override
    public void clearToolbarColor() {
        toolbar.setBackground(toolbarBG);
    }

    @Override public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        List<Fragment> fragments = navHostFragment.getChildFragmentManager().getFragments();
        for (int i = fragments.size()-1; i >= 0; i--) {
            final Fragment currentFragment = fragments.get(i);
            Log.d(TAG, "onBackPressed: fragment " + currentFragment);
            if ((currentFragment instanceof OnBackPressedListener) && ((OnBackPressedListener) currentFragment).onBackPressed()) {
                return;
            }
        }
        if (!navController.popBackStack()) {
            super.onBackPressed();
        }
    }

    @Override
    public int submitOperation(Completable operation) {
        int key = FoodPlannerApplication.from(this)
                .getOperationManager().submitOperation(operation);
        navController.navigate(LoadingFragmentDirections.actionGlobalLoading(key));
        return key;
    }

    @Override
    public Completable retrieve(int operationKey) {
        return FoodPlannerApplication.from(this)
                .getOperationManager().retrieve(operationKey);
    }


}