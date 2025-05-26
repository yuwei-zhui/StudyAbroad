package com.example.studyabroad.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studyabroad.R;
import com.example.studyabroad.ui.auth.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button buttonNext;
    private TextView textViewSkip;
    private OnboardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Find views
        viewPager = findViewById(R.id.viewPagerOnboarding);
        tabLayout = findViewById(R.id.tabLayoutIndicator);
        buttonNext = findViewById(R.id.buttonNext);
        textViewSkip = findViewById(R.id.textViewSkip);

        // Setup onboarding pages
        setupOnboardingItems();

        // Setup listeners
        buttonNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                finishOnboarding();
            }
        });

        textViewSkip.setOnClickListener(v -> finishOnboarding());

        // ViewPager page change listener
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == adapter.getItemCount() - 1) {
                    buttonNext.setText(R.string.get_started);
                } else {
                    buttonNext.setText(R.string.next);
                }
            }
        });
    }

    private void setupOnboardingItems() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        // First page
        OnboardingItem item1 = new OnboardingItem();
        item1.setTitle(getString(R.string.onboarding_title_1));
        item1.setDescription(getString(R.string.onboarding_desc_1));
        item1.setImageResId(R.drawable.onboarding_1);

        // Second page
        OnboardingItem item2 = new OnboardingItem();
        item2.setTitle(getString(R.string.onboarding_title_2));
        item2.setDescription(getString(R.string.onboarding_desc_2));
        item2.setImageResId(R.drawable.onboarding_2);

        // Third page
        OnboardingItem item3 = new OnboardingItem();
        item3.setTitle(getString(R.string.onboarding_title_3));
        item3.setDescription(getString(R.string.onboarding_desc_3));
        item3.setImageResId(R.drawable.onboarding_3);

        onboardingItems.add(item1);
        onboardingItems.add(item2);
        onboardingItems.add(item3);

        adapter = new OnboardingAdapter(onboardingItems);
        viewPager.setAdapter(adapter);

        // Setup TabLayout with ViewPager2 for page indicators
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // We don't need to set tab text here, just the indicators
                }).attach();
    }

    private void finishOnboarding() {
        // Navigate to Login screen after onboarding
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
} 