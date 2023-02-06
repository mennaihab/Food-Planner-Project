package com.example.foodplanner.features.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.views.OnBackPressedListener;

public class MainActivity extends AppCompatActivity {

    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
    }

    @Override public void onBackPressed() {
        final Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        final NavController controller = Navigation.findNavController(this, R.id.main_nav_host);
        if (!(currentFragment instanceof OnBackPressedListener) ||
                !((OnBackPressedListener) currentFragment).onBackPressed() ||
                !controller.popBackStack()) {
            super.onBackPressed();
        }
    }
}