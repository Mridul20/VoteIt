package com.example.onlinevotingsystem.fragments.shared;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgs;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.activities.AdminActivity;
import com.example.onlinevotingsystem.activities.OfficerActivity;
import com.example.onlinevotingsystem.activities.UserActivity;
import com.example.onlinevotingsystem.classes.Officer;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.fragments.startup.RegisterFragmentDirections;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ResetPasswordFragment extends Fragment implements DatabaseUpdater.DatabaseUpdateInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    private String type,role,id;
    TextInputLayout inputPassword, inputPasswordConfirm;
    Button btnSubmit;

    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ResetPasswordFragmentArgs args=ResetPasswordFragmentArgs.fromBundle(getArguments());
        type=args.getType();
        role=args.getRole();
        id=args.getID();

        inputPassword=view.findViewById(R.id.ilResetPassword);
        inputPasswordConfirm=view.findViewById(R.id.ilResetPasswordConfirm);

        btnSubmit=view.findViewById(R.id.btnSetPassword);

        btnSubmit.setOnClickListener(v -> {
            String password, passwordConfirm;

            password=inputPassword.getEditText().getText().toString();
            passwordConfirm=inputPasswordConfirm.getEditText().getText().toString();

            if(password.isEmpty() || passwordConfirm.isEmpty()){
                Toast.makeText(requireActivity(),"Please Fill all the Fields",Toast.LENGTH_SHORT).show();
            }
            else if(!password.equals(passwordConfirm)){
                Toast.makeText(requireActivity(),"Passwords Do not Match",Toast.LENGTH_SHORT).show();
            }
            else {
                HashMap<String,Object> hashMap=new HashMap<>();
                if(type.equals("RegisterUser") && role.equals("Voter")){
                    hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_REGISTER_USER);
                    hashMap.put(HashMapConstants.UPDATE_PARAM_REGISTER_USER_VOTER_ID,id);
                    hashMap.put(HashMapConstants.UPDATE_PARAM_REGISTER_USER_PASSWORD,password);

                    progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Registering User");
                    progressIndicatorFragment.show(getParentFragmentManager(),"RegisterUser");
                    new DatabaseUpdater(hashMap,this).execute();
                }
                else if(type.equals("ChangePassword") || type.equals("Forget")){
                    switch (role) {
                        case "Admin":
                            hashMap.put(HashMapConstants.UPDATE_TYPE_KEY, HashMapConstants.UPDATE_TYPE_ADMIN_PASSWORD);
                            hashMap.put(HashMapConstants.UPDATE_PARAM_ADMIN_USERNAME_KEY, id);
                            hashMap.put(HashMapConstants.UPDATE_PARAM_ADMIN_PASSWORD_KEY, password);

                            progressIndicatorFragment = ProgressIndicatorFragment.newInstance("Syncing with Server", "Updating Password");
                            progressIndicatorFragment.show(getParentFragmentManager(), "UpdateAdminPassword");
                            new DatabaseUpdater(hashMap, this).execute();
                            break;
                        case "Officer":
                            hashMap.put(HashMapConstants.UPDATE_TYPE_KEY, HashMapConstants.UPDATE_TYPE_OFFICER_PASSWORD);
                            hashMap.put(HashMapConstants.UPDATE_PARAM_OFFICER_USERNAME_KEY, id);
                            hashMap.put(HashMapConstants.UPDATE_PARAM_OFFICER_PASSWORD_KEY, password);

                            progressIndicatorFragment = ProgressIndicatorFragment.newInstance("Syncing with Server", "Updating Password");
                            progressIndicatorFragment.show(getParentFragmentManager(), "UpdateOfficerPassword");
                            new DatabaseUpdater(hashMap, this).execute();
                            break;
                        case "Voter":
                            hashMap.put(HashMapConstants.UPDATE_TYPE_KEY, HashMapConstants.UPDATE_TYPE_VOTER_PASSWORD);
                            hashMap.put(HashMapConstants.UPDATE_PARAM_VOTER_ID_KEY, id);
                            hashMap.put(HashMapConstants.UPDATE_PARAM_VOTER_PASSWORD_KEY, password);

                            progressIndicatorFragment = ProgressIndicatorFragment.newInstance("Syncing with Server", "Updating Password");
                            progressIndicatorFragment.show(getParentFragmentManager(), "UpdateVoterPassword");
                            new DatabaseUpdater(hashMap, this).execute();
                            break;
                    }
                }
            }

        });

    }

    @Override
    public void onDataUpdated(String Type, boolean result, String error) {
        switch (Type){
            case HashMapConstants.UPDATE_TYPE_REGISTER_USER:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"User Registered Successfully, Now signing in",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(requireActivity(), UserActivity.class);
                    intent.putExtra("UserVoterID",id);
                    startActivity(intent);
                    requireActivity().finish();
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Registering User: "+error,Toast.LENGTH_LONG).show();
                }
                break;
            }
            case HashMapConstants.UPDATE_TYPE_ADMIN_PASSWORD:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"Password Changed Successfully for Admin",Toast.LENGTH_SHORT).show();
                    if(type.equals("Forget")){
                        Intent intent=new Intent(requireActivity(), AdminActivity.class);
                        intent.putExtra("username",id);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                    else {
                        Navigation.findNavController(requireActivity(),R.id.navHostAdmin).navigate(R.id.action_resetPasswordFragment3_to_adminHomeFragment);
                    }
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Updating Password",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case HashMapConstants.UPDATE_TYPE_OFFICER_PASSWORD:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"Password Changed Successfully for Officer",Toast.LENGTH_SHORT).show();
                    if(type.equals("Forget")){
                        Intent intent=new Intent(requireActivity(), OfficerActivity.class);
                        intent.putExtra("username",id);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                    else {
                        Navigation.findNavController(requireActivity(),R.id.navHostOfficer).navigate(R.id.action_resetPasswordFragment2_to_updateOfficerProfileFragment);
                    }
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Updating Password",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case HashMapConstants.UPDATE_TYPE_VOTER_PASSWORD:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"Password Changed Successfully for Voter",Toast.LENGTH_SHORT).show();
                    if(type.equals("Forget")){
                        Intent intent=new Intent(requireActivity(), UserActivity.class);
                        intent.putExtra("UserVoterID",id);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                    else {
                        Navigation.findNavController(requireActivity(),R.id.navHostUser).navigate(R.id.action_resetPasswordFragment4_to_userHomeFragment);
                    }
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Updating Password",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}