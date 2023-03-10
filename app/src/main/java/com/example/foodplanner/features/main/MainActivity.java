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
import com.example.foodplanner.features.common.views.NavigatorProvider;
import com.example.foodplanner.features.common.views.OnBackPressedListener;
import com.example.foodplanner.features.common.views.OperationSink;
import com.example.foodplanner.features.common.views.WindowPainter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;

@OptIn(markerClass = BuildCompat.PrereleaseSdkCheck.class)
public class MainActivity extends AppCompatActivity implements WindowPainter, OperationSink, NavigatorProvider {
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
        int[] toolbarViews = new int[] {
                R.id.home_meal_fragment,
                R.id.home_favourites_fragment,
        };
        int[] dialogsIds = new int[] {
                R.id.requiredAuth_fragment,
                R.id.loading_fragment,
        };
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(homeFragmentsIds).build();
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        if (savedInstanceState == null) {
            navController.navigate(R.id.action_global_to_launching);
        }

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            boolean changeNothing = Arrays.stream(dialogsIds).anyMatch(id -> navDestination.getId() == id);
            if (!changeNothing) {
                boolean decorShouldShow = Arrays.stream(homeFragmentsIds).anyMatch(id -> navDestination.getId() == id);
                boolean toolbarShouldShow = Arrays.stream(toolbarViews).anyMatch(id -> navDestination.getId() == id);
                setToolbarVisibility(toolbarShouldShow);
                setBottomNavVisibility(decorShouldShow);
                if (decorShouldShow) {
                    setStatusBarVisibility(true);
                    clearStatusBarColor();
                }
            }
        });

        navView.setOnItemSelectedListener(item -> {
            boolean canNavigate = item.getItemId() == R.id.home_meal_fragment || item.getItemId() == R.id.home_search_fragment;
            if (!canNavigate) {
                canNavigate = FoodPlannerApplication.from(this).getAuthenticationManager().isAuthenticated();
            }
            if (canNavigate) {
                canNavigate = NavigationUI.onNavDestinationSelected(item, navController);
            } else {
                navController.navigate(R.id.action_global_requiredAuth);
            }
            return canNavigate;
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
    public NavController getNavController() {
        return navController;
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
            if ((currentFragment instanceof OnBackPressedListener) &&
                    ((OnBackPressedListener) currentFragment).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
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