package com.example.onlinevotingsystem.fragments.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.activities.StartupActivity;
import com.example.onlinevotingsystem.activities.UserActivity;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.fragments.shared.VerifyOtpFragment;
import com.example.onlinevotingsystem.viewModels.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import org.jetbrains.annotations.NotNull;

public class UserHomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    CircleMenu circleMenu;
    NavController navController;

    UserViewModel userViewModel;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circleMenu=view.findViewById(R.id.cmUserHome);
        navController= Navigation.findNavController(view);

        userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.menu,R.drawable.cancel)
                .addSubMenu(Color.parseColor("#88bef5"),R.drawable.vote1)
                .addSubMenu(Color.parseColor("#83e85a"),R.drawable.update_profile1)
                .addSubMenu(Color.parseColor("#FF4B32"),R.drawable.result1)
                .addSubMenu(Color.parseColor("#ba53de"),R.drawable.candidates1)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.complain1)
                .setOnMenuSelectedListener(index -> {
                    switch (index){
                        case 0: {
                            if(userViewModel.hasUserVoted()){
                                Toast.makeText(requireActivity(), "You have Already Voted! You cannot give another vote.", Toast.LENGTH_SHORT).show();
                            }
                            else if(userViewModel.hasElectionEnd()){
                                Toast.makeText(requireActivity(), "Election has Already Ended!", Toast.LENGTH_SHORT).show();
                            }
                            else if(!userViewModel.hasElectionStarted()){
                                Toast.makeText(requireActivity(), "Election has not yet started", Toast.LENGTH_SHORT).show();
                            }
                            else
                                navController.navigate(R.id.voteFragment);
                            break;
                        }
                        case 1: {
                            navController.navigate(R.id.action_userHomeFragment_to_userUpdateProfileFragment);
                            break;
                        }
                        case 2: {
                            if(userViewModel.hasElectionEnd()){
                                NavDirections action=UserHomeFragmentDirections.actionUserHomeFragmentToElectionResultFragment2(userViewModel.GetUser().getPollNumber());
                                navController.navigate(action);
                            }
                            else {
                                Toast.makeText(requireActivity(), "Election has not yet Ended", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case 3: {
                            navController.navigate(R.id.action_userHomeFragment_to_userPollDetailsFragment);
                            break;
                        }
                        case 4: {
                            AlertDialog alertDialog=new MaterialAlertDialogBuilder(requireActivity())
                                    .setTitle("Warning")
                                    .setMessage("Do you want to Logout?")
                                    .setPositiveButton("YES", (dialog, which) -> {
                                        Intent intent=new Intent(requireActivity(), StartupActivity.class);
                                        intent.putExtra("AfterLogout",true);
                                        startActivity(intent);
                                        requireActivity().finish();
                                    })
                                    .setNegativeButton("NO", (dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .create();
                            alertDialog.show();
                            break;
                        }
                    }

                });
    }
}