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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.views.OnBackPressedListener;

import java.util.Arrays;
import java.util.List;

public class LandingFragment extends Fragment implements OnBackPressedListener {

    private ViewPager2 viewPager;

    public LandingFragment() {
        super(R.layout.fragment_landing);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.pager);
        Button nextBtn = view.findViewById(R.id.next_btn);
        Button getStartedBtn  = view.findViewById(R.id.button2);
        TextView skipTV = view.findViewById(R.id.skip_tv);

        viewPager.setAdapter(new LandingPageSlideAdapter(Arrays.asList(
                new LandingPageFirstFragment(),
                new LandingPageSecondFragment(),
                new LandingPageThirdFragment()
        )));

        ImageView[] dots = new ImageView[] {view.findViewById(R.id.dot1), view.findViewById(R.id.dot2), view.findViewById(R.id.dot3)};

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                boolean lastPage = position == 3;
                for (int i = 0; i < 3; i++) {
                    if (lastPage) {
                        dots[i].setVisibility(View.GONE);
                    } else {
                        boolean selected = position == i;
                        dots[i].setVisibility(View.VISIBLE);
                        dots[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), selected ? R.drawable.tab_indicator_selected : R.drawable.tab_indicator_default));
                    }
                }

                if (lastPage) {
                    getStartedBtn.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.GONE);
                    skipTV.setVisibility(View.GONE);
                } else {
                    nextBtn.setVisibility(View.VISIBLE);
                    skipTV.setVisibility(View.VISIBLE);
                    getStartedBtn.setVisibility(View.GONE);
                }

            }
        });

        underlineSkip(skipTV);
    }

    @Override
    public boolean onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            return true;
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            return false;
        }
    }

    public void underlineSkip(TextView textView){
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
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.GRAY);
    }

    private class LandingPageSlideAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public LandingPageSlideAdapter(List<Fragment> items) {
            super(LandingFragment.this);
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

    public static class LandingPageFirstFragment extends Fragment {
        public LandingPageFirstFragment() {
            super(R.layout.fragment_landing_first);
        }
    }

    public static class LandingPageSecondFragment extends Fragment {

        public LandingPageSecondFragment() {
            super(R.layout.fragment_landing_second);
        }

    }

    public static class LandingPageThirdFragment extends Fragment {
        public LandingPageThirdFragment() {
            super(R.layout.fragment_landing_third);
        }
    }
}


