package com.example.onlinevotingsystem.fragments.user;

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
import com.example.onlinevotingsystem.viewModels.UserViewModel;

import org.jetbrains.annotations.NotNull;

public class UserUpdateProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_update_profile, container, false);
    }

    UserViewModel userViewModel;
    Button btnChangePassword, btnUpdatePhoto;
    NavController navController;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        btnChangePassword=view.findViewById(R.id.btnUserUpdateProfileChangePassword);
        btnUpdatePhoto=view.findViewById(R.id.btnUserUpdateProfileUpdatePhoto);

        navController= Navigation.findNavController(view);

        btnChangePassword.setOnClickListener(v -> {
            NavDirections action=UserUpdateProfileFragmentDirections.actionUserUpdateProfileFragmentToChangePasswordFragment3("Voter",userViewModel.GetVoterId());
            navController.navigate(action);
        });

        btnUpdatePhoto.setOnClickListener(v -> {
            String currentPhoto="null";
            if(userViewModel.GetUser().getPhotoURL()!=null)
                currentPhoto=userViewModel.GetUser().getPhotoURL();
            NavDirections action=UserUpdateProfileFragmentDirections.actionUserUpdateProfileFragmentToUpdatePhotoFragment3("Voter",userViewModel.GetVoterId(),currentPhoto);
            navController.navigate(action);
        });
    }
}