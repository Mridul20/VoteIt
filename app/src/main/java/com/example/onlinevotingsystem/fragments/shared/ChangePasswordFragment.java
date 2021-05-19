package com.example.onlinevotingsystem.fragments.shared;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.activities.UserActivity;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ChangePasswordFragment extends Fragment implements FetchFromDatabase.FetchDbInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    String type, id;

    TextView tvHeading;
    TextInputLayout inputPassword;
    Button btnVerify;

    NavController navController;

    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChangePasswordFragmentArgs args=ChangePasswordFragmentArgs.fromBundle(getArguments());
        type=args.getType();
        id=args.getId();

        tvHeading=view.findViewById(R.id.tvChangePasswordHeading);
        inputPassword=view.findViewById(R.id.ilChangePasswordInput);
        btnVerify=view.findViewById(R.id.btnChangePasswordVerify);

        navController=Navigation.findNavController(view);

        tvHeading.setText("Password Update for "+type);

        btnVerify.setOnClickListener(v -> {
            String password=inputPassword.getEditText().getText().toString();
            if(password.isEmpty()){
                Toast.makeText(requireActivity(), "Please Enter Your Current Password", Toast.LENGTH_SHORT).show();
            }
            else {
                String fetchType;
                switch (type){
                    case "Admin":{
                        fetchType= HashMapConstants.FETCH_TYPE_LOGIN_ADMIN;
                        break;
                    }
                    case "Officer":{
                        fetchType=HashMapConstants.FETCH_TYPE_LOGIN_OFFICER;
                        break;
                    }
                    default:{
                        fetchType=HashMapConstants.FETCH_TYPE_LOGIN_USER;
                        break;
                    }
                }
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,fetchType);
                hashMap.put(HashMapConstants.FETCH_PARAM_LOGIN_USERNAME_KEY,id);
                hashMap.put(HashMapConstants.FETCH_PARAM_LOGIN_PASSWORD_KEY,password);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Verifying Current Password");
                progressIndicatorFragment.show(getParentFragmentManager(),"VerifyPasswordProcess");

                new FetchFromDatabase(this,hashMap).execute();
            }
        });

    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_LOGIN_ADMIN)){
            progressIndicatorFragment.dismiss();
            inputPassword.getEditText().setText("");

            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY)){
                    String type="ChangePassword";
                    String role="Admin";
                    String Id=id;
                    Bundle bundle=new Bundle();
                    bundle.putString("Type",type);
                    bundle.putString("Role",role);
                    bundle.putString("ID",Id);
                    navController.navigate(R.id.resetPasswordFragment3,bundle);
                }
                else {
                    Toast.makeText(requireActivity(),"Wrong Password, Please Enter the Current Password",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(),"Error in Logging! " + error + "",Toast.LENGTH_LONG).show();
            }

        }
        else if (resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_LOGIN_OFFICER)){
            progressIndicatorFragment.dismiss();

            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY)){
                    String type="ChangePassword";
                    String role="Officer";
                    String Id=id;
                    Bundle bundle=new Bundle();
                    bundle.putString("Type",type);
                    bundle.putString("Role",role);
                    bundle.putString("ID",Id);
                    navController.navigate(R.id.resetPasswordFragment2,bundle);
                }
                else {
                    Toast.makeText(requireActivity(),"Wrong Password, Please Enter the Current Password",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(),"Error in Logging! " + error + "",Toast.LENGTH_LONG).show();
            }

        }
        else if (resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_LOGIN_USER)){
            progressIndicatorFragment.dismiss();

            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY)){
                    String type="ChangePassword";
                    String role="Voter";
                    String Id=id;
                    Bundle bundle=new Bundle();
                    bundle.putString("Type",type);
                    bundle.putString("Role",role);
                    bundle.putString("ID",Id);
                    navController.navigate(R.id.resetPasswordFragment4,bundle);
                }
                else {
                    Toast.makeText(requireActivity(),"Wrong Password, Please Enter the Current Password",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(),"Error in Logging! " + error + "",Toast.LENGTH_LONG).show();
            }

        }
    }
}