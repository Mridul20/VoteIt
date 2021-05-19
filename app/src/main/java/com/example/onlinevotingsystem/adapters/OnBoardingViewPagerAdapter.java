package com.example.onlinevotingsystem.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.onlinevotingsystem.fragments.startup.OnBoardingFragment1;
import com.example.onlinevotingsystem.fragments.startup.OnBoardingFragment2;
import com.example.onlinevotingsystem.fragments.startup.OnBoardingFragment3;

import org.jetbrains.annotations.NotNull;

public class OnBoardingViewPagerAdapter extends FragmentStatePagerAdapter {

    public OnBoardingViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 1: return new OnBoardingFragment2();
            case 2: return new OnBoardingFragment3();
            default: return new OnBoardingFragment1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
