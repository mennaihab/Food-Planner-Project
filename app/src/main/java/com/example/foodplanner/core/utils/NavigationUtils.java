package com.example.foodplanner.core.utils;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.R;

import java.util.Objects;

public abstract class NavigationUtils {
    public static <T> void setResult(View view, String key, T result) {
        Objects.requireNonNull(Navigation.findNavController(view).getPreviousBackStackEntry())
                .getSavedStateHandle().set(key, result);
    }

    public static <T> void maybeSetResult(View view, String key, T result) {
        NavBackStackEntry entry = Navigation.findNavController(view).getPreviousBackStackEntry();
        if (entry != null) {
            entry.getSavedStateHandle().set(key, result);
        }
    }

    public static <T> LiveData<T> getResult(View view, String key) {
        return Objects.requireNonNull(Navigation.findNavController(view).getCurrentBackStackEntry())
                .getSavedStateHandle().getLiveData(key);
    }

    public static void navigateUp(View view) {
        Navigation.findNavController(view).navigateUp();
    }

    public static boolean onNavDestinationSelected(MenuItem item, NavController navController) {
        NavOptions.Builder builder = new NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true);
        //noinspection ConstantConditions
        NavDestination current = navController.getCurrentDestination();
        NavGraph graph = null;
        if (current != null) {
            graph = current.getParent();
        }
        if (graph == null) {
            graph = navController.getGraph();
        }
        if (graph.findNode(item.getItemId()) instanceof ActivityNavigator.Destination) {
            builder.setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim);
        } else {
            builder.setEnterAnim(androidx.navigation.ui.R.animator.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.animator.nav_default_exit_anim)
                    .setPopEnterAnim(androidx.navigation.ui.R.animator.nav_default_pop_enter_anim)
                    .setPopExitAnim(androidx.navigation.ui.R.animator.nav_default_pop_exit_anim);
        }
        if (current != null && (item.getOrder() & Menu.CATEGORY_SECONDARY) == 0) {
            builder.setPopUpTo(
                    current.getId(),
                    true,
                    true
            );
        }
        try {
            NavOptions options = builder.build();
            navController.navigate(item.getItemId(), null, options);
            return navController.getCurrentDestination().getId() == item.getItemId();
        } catch (IllegalArgumentException e) {
            Log.e("RG", e.getLocalizedMessage(), e);
            return false;
        }
    }
}
