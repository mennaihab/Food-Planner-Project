package com.example.foodplanner.features.launching.views;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodplanner.R;
import com.example.foodplanner.features.authenticatuin.views.SignUpFragment;
import com.example.foodplanner.features.landing.views.LandingPageSlide;

public class MainActivity extends AppCompatActivity {
  TextView tv;
  Button n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.app_name_tv);
     AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
     AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;

        tv.startAnimation(fadeIn);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

                tv.startAnimation(fadeIn);
                fadeOut.setDuration(2000);
                fadeOut.setFillAfter(true);
                fadeOut.setStartOffset(4200+fadeIn.getStartOffset());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(getApplicationContext(), LandingPageSlide.class);
                //Intent i = new Intent(getApplicationContext(), SignUpFragment.class);
                startActivity(i);
                finish();
            }
        }, 5000);



}}