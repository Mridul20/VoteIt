package com.example.onlinevotingsystem.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.fragments.user.UserDetailsFragment;
import com.example.onlinevotingsystem.viewModels.UserViewModel;

public class UserActivity extends AppCompatActivity {

    ProgressIndicatorFragment progressIndicatorFragment;
    UserViewModel userViewModel;

    NavController navController;

    UserDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user);

        Intent intent=getIntent();
        String voterID=intent.getStringExtra("UserVoterID");

        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.FetchDetails(voterID);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Loading Data");

        userViewModel.CheckIsDataLoading().observe(this,aBoolean -> {
            if(aBoolean){
                if(!progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.show(getSupportFragmentManager(),"OfficerDetailsProcess");
            }
            else {
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });

        navController=Navigation.findNavController(this,R.id.navHostUser);

        detailsFragment=(UserDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentUserDetails);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()==R.id.userHomeFragment){
                userViewModel.reloadData();
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(detailsFragment)
                        .commit();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(detailsFragment)
                        .commit();
            }
        });

    }
}