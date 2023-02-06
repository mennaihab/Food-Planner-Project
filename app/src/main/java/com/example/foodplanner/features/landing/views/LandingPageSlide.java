package com.example.foodplanner.features.landing.views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodplanner.R;

import java.util.ArrayList;

public class LandingPageSlide extends FragmentActivity {

    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    Button nextBtn;
    Button getStartedBtn;
    TextView skipTV;
    LinearLayout linearLayout;
    private ViewPager2 viewPager;
    private LandingPageSlideAdapter fragmentStateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page_slide);
        viewPager = findViewById(R.id.pager);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        nextBtn = findViewById(R.id.next_btn);
        getStartedBtn  = findViewById(R.id.button2);
        skipTV = findViewById(R.id.skip_tv);
        linearLayout = findViewById(R.id.linear);
        fragmentStateAdapter = new LandingPageSlideAdapter(this);
        fragmentStateAdapter.addFragment(new LandingPageFirstFragment());
        fragmentStateAdapter.addFragment(new LandingPageSecondFragment());
        fragmentStateAdapter.addFragment(new LandingPageThirdFragment());
        viewPager.setAdapter(fragmentStateAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == 0) {
                    dot1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_selected));
                    dot2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_default));
                    dot3.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_default));
                }
                if (position == 1) {

                   /* View inflatedView = getLayoutInflater().inflate(R.layout.fragment_landing_page_third, null);
                    Button getStartedBtn  = inflatedView.findViewById(R.id.button2);
                    ViewGroup parent = (ViewGroup) linearLayout.getParent();
                    ViewGroup parent2 = (ViewGroup) getStartedBtn.getParent();
                    int index = parent.indexOfChild(linearLayout);
                    int index2 = parent.indexOfChild(getStartedBtn);
                    if(parent2!=null&&parent!=null) {
                    parent.removeView(linearLayout);
                    parent2.removeView(getStartedBtn);
                    parent.addView(linearLayout, index);
                    //}
                    */
                    dot1.setVisibility(View.VISIBLE);
                    dot2.setVisibility(View.VISIBLE);
                    dot3.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    skipTV.setVisibility(View.VISIBLE);
                    getStartedBtn.setVisibility(View.GONE);
                    dot2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_selected));
                    dot1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_default));
                    dot3.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_default));
                }

                if (position == 2) {
                    getStartedBtn.setVisibility(View.VISIBLE);
                    dot1.setVisibility(View.GONE);
                    dot2.setVisibility(View.GONE);
                    dot3.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.GONE);
                    skipTV.setVisibility(View.GONE);

                }
            }
        });

        underlineSkip();

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();

        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void underlineSkip(){
        SpannableString spannableString = new SpannableString("SKIP");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //startActivity(new Intent(getContext(), MainActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        spannableString.setSpan(clickableSpan, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        TextView textView = findViewById(R.id.skip_tv);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.GRAY);
    }

    private class LandingPageSlideAdapter extends FragmentStateAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public LandingPageSlideAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public Fragment createFragment(int position) {

            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}


