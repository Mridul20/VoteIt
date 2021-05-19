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
import com.example.onlinevotingsystem.fragments.officer.OfficerDetailsFragment;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.viewModels.OfficerViewModel;
import com.google.android.material.appbar.MaterialToolbar;

public class OfficerActivity extends AppCompatActivity {

    ProgressIndicatorFragment progressIndicatorFragment;
    OfficerViewModel officerViewModel;

    NavController navController;
    
    MaterialToolbar toolbar;

    OfficerDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_officer);

        Intent intent=getIntent();
        String username=intent.getStringExtra("username");

        officerViewModel=new ViewModelProvider(this).get(OfficerViewModel.class);
        officerViewModel.FetchDetails(username);

        detailsFragment=(OfficerDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentOfficerDetails);

        toolbar=findViewById(R.id.toolbarOfficer);

        toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.menuAdminLogout){
                Intent intent1=new Intent(this,StartupActivity.class);
                intent1.putExtra("AfterLogout",true);
                startActivity(intent1);
                this.finish();
            }
            return false;
        });

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Loading Data");

        officerViewModel.CheckIsListLoading().observe(this,aBoolean -> {
            if(aBoolean){
                if(!progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.show(getSupportFragmentManager(),"OfficerDetailsProcess");
            }
            else {
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });

        navController=Navigation.findNavController(this,R.id.navHostOfficer);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()==R.id.officerHomeFragment){
                officerViewModel.reloadData();
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