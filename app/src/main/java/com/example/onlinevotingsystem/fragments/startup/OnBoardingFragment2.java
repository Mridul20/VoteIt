package com.example.onlinevotingsystem.fragments.startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.onlinevotingsystem.R;

import org.jetbrains.annotations.NotNull;

public class OnBoardingFragment2 extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_on_boarding2,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnViewCandidates=view.findViewById(R.id.btnOnboarding2ViewCandidates);

        btnViewCandidates.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(R.id.action_onBoardingFragment_to_pollListFragment);
        });

    }
}
