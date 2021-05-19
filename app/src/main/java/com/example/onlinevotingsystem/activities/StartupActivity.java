package com.example.onlinevotingsystem.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.database.ConnectionEstablisher;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.fragments.startup.SplashScreenFragment;
import com.example.onlinevotingsystem.viewModels.CandidateListViewModel;

public class StartupActivity extends AppCompatActivity implements ConnectionEstablisher.ConnectionInterface {

    NavController navController;
    private ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_startup);

        Intent intent=getIntent();
        boolean afterLogout=intent.getBooleanExtra("AfterLogout",false);

        navController= Navigation.findNavController(this,R.id.navHostStartup);

        if(afterLogout){
            navController.navigate(R.id.action_splashScreenFragment_to_home2);
        }
        else {
            new Handler().postDelayed(() -> {
                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Establishing connection with the Server");
                progressIndicatorFragment.show(getSupportFragmentManager(),"ConnectionProgress");
                new ConnectionEstablisher(this).execute();
            },3000);
        }
    }

    @Override
    public void onBackPressed() {
        if(navController.getCurrentDestination().getId()==R.id.pollListFragment){
            navController.navigate(R.id.action_pollListFragment_to_onBoardingFragment);
        }
        else if(navController.getCurrentDestination().getId()==R.id.userLoginFragment) {
            navController.navigate(R.id.action_userLoginFragment_to_onBoardingFragment2);
        }
        else if(navController.getCurrentDestination().getId()==R.id.onBoardingFragment){
            this.finish();
        }
        else
            super.onBackPressed();
    }

    @Override
    public void onConnectionResult(boolean result, String error) {
        progressIndicatorFragment.dismiss();
        if(result){
            navController.navigate(R.id.action_splashScreenFragment_to_home2);
        }
        else {
            Toast.makeText(this,"Error in establishing connection: "+error,Toast.LENGTH_LONG).show();
            this.finish();
        }
    }
}