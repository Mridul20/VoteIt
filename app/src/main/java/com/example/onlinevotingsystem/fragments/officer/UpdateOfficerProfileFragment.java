package com.example.onlinevotingsystem.fragments.officer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.viewModels.OfficerViewModel;

import org.jetbrains.annotations.NotNull;

public class UpdateOfficerProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_officer_profile, container, false);
    }

    Button btnUpdatePhoto,btnChangePassword;
    NavController navController;

    OfficerViewModel officerViewModel;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnChangePassword=view.findViewById(R.id.btnOfficerChangePassword);
        btnUpdatePhoto=view.findViewById(R.id.btnOfficerUpdatePhoto);

        officerViewModel=new ViewModelProvider(requireActivity()).get(OfficerViewModel.class);

        navController= Navigation.findNavController(view);

        btnChangePassword.setOnClickListener(v -> {
            NavDirections action=UpdateOfficerProfileFragmentDirections.actionUpdateOfficerProfileFragmentToChangePasswordFragment("Officer",officerViewModel.GetOfficerUsername());
            navController.navigate(action);
        });

        btnUpdatePhoto.setOnClickListener(v -> {
            String currentPhoto="null";
            if(officerViewModel.GetPhotoUrl()!=null)
                currentPhoto=officerViewModel.GetPhotoUrl();
            NavDirections action=UpdateOfficerProfileFragmentDirections.actionUpdateOfficerProfileFragmentToUpdatePhotoFragment2("Officer",officerViewModel.GetOfficerUsername(),currentPhoto);
            navController.navigate(action);
        });
    }
}