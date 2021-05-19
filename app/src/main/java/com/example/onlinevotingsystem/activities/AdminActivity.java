package com.example.onlinevotingsystem.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.fragments.admin.AdminDetailsFragment;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.viewModels.AdminViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

public class AdminActivity extends AppCompatActivity {

    ProgressIndicatorFragment progressIndicatorFragment;
    MaterialToolbar toolbar;

    NavController navController;

    AdminDetailsFragment detailsFragment;
    boolean isProgressShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin);

        Intent startIntent=getIntent();
        String username=startIntent.getStringExtra("username");

        AdminViewModel adminViewModel=new ViewModelProvider(this).get(AdminViewModel.class);
        adminViewModel.SetUsername(username);

        toolbar=findViewById(R.id.toolbarAdmin);
        navController= Navigation.findNavController(this,R.id.navHostAdmin);

        detailsFragment=(AdminDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAdminDetails);
        isProgressShow=false;

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Updating the Database");

        adminViewModel.CheckIsProcessPerformed().observe(this,aBoolean -> {
            if(aBoolean!=null && aBoolean){
                if(!isProgressShow){
                    isProgressShow=true;
                    progressIndicatorFragment.show(getSupportFragmentManager(),"DbProcess");
                }
            }
            else if(aBoolean != null){
                if(isProgressShow){
                    isProgressShow=false;
                    progressIndicatorFragment.dismiss();
                }
            }
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()==R.id.adminHomeFragment){
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(detailsFragment)
                        .commit();
                adminViewModel.reloadData();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(detailsFragment)
                        .commit();
            }
        });

        toolbar.setOnMenuItemClickListener(item -> {

            if(item==toolbar.getMenu().findItem(R.id.menuAdminLogout)){
                Intent intent=new Intent(this,StartupActivity.class);
                intent.putExtra("AfterLogout",true);
                startActivity(intent);
                this.finish();
            }

            return false;
        });
    }
}