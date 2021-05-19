package com.example.onlinevotingsystem.fragments.startup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cuberto.liquid_swipe.LiquidPager;
import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.adapters.OnBoardingViewPagerAdapter;

public class OnBoardingFragmentHolder extends Fragment {

    LiquidPager pager;
    OnBoardingViewPagerAdapter viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding_holder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager =view.findViewById(R.id.liquidPagerOnboarding);
        viewPager = new OnBoardingViewPagerAdapter(getChildFragmentManager(),1);

        pager.setAdapter(viewPager);

    }
}