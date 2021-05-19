package com.example.onlinevotingsystem.fragments.startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.activities.AdminActivity;
import com.example.onlinevotingsystem.activities.OfficerActivity;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AdminOfficerLoginFragment extends Fragment implements FetchFromDatabase.FetchDbInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_officer_login, container, false);
    }

    private TextInputLayout inputUsername, inputPassword;

    private String username;
    private ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUsername=view.findViewById(R.id.ilAdminOfficerUsername);
        inputPassword=view.findViewById(R.id.ilAdminOfficerPassword);

        Button btnLoginAsOfficer = view.findViewById(R.id.btnOfficerLoginSubmit);
        Button btnLoginAsAdmin = view.findViewById(R.id.btnAdminLoginSubmit);
        Button btnForgetPassword = view.findViewById(R.id.btnAdminOfficerForgotPassword);

        btnForgetPassword.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.forgotPasswordFragment));

        btnLoginAsAdmin.setOnClickListener(v -> {
            String password;

            username=inputUsername.getEditText().getText().toString();
            password=inputPassword.getEditText().getText().toString();

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(requireActivity(),"Please enter the Credentials",Toast.LENGTH_SHORT).show();
            }
            else {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_LOGIN_ADMIN);
                hashMap.put(HashMapConstants.FETCH_PARAM_LOGIN_USERNAME_KEY,username);
                hashMap.put(HashMapConstants.FETCH_PARAM_LOGIN_PASSWORD_KEY,password);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Authenticating Admin","Signing in with the Given Credentials");
                progressIndicatorFragment.show(getParentFragmentManager(),"AdminLoginProgress");

                new FetchFromDatabase(this,hashMap).execute();
            }
        });

        btnLoginAsOfficer.setOnClickListener(v -> {
            String password;

            username=inputUsername.getEditText().getText().toString();
            password=inputPassword.getEditText().getText().toString();

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(requireActivity(),"Please enter the Credentials",Toast.LENGTH_SHORT).show();
            }
            else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY, HashMapConstants.FETCH_TYPE_LOGIN_OFFICER);
                hashMap.put(HashMapConstants.FETCH_PARAM_LOGIN_USERNAME_KEY, username);
                hashMap.put(HashMapConstants.FETCH_PARAM_LOGIN_PASSWORD_KEY, password);

                progressIndicatorFragment = ProgressIndicatorFragment.newInstance("Authenticating Officer", "Signing in with the Given Credentials");
                progressIndicatorFragment.show(getParentFragmentManager(), "OfficerLoginProgress");

                new FetchFromDatabase(this, hashMap).execute();
            }
        });

    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        String fetchType=resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).toString();
        if(fetchType.equals(HashMapConstants.FETCH_TYPE_LOGIN_ADMIN) || fetchType.equals(HashMapConstants.FETCH_TYPE_LOGIN_OFFICER)){
            progressIndicatorFragment.dismiss();
            inputUsername.getEditText().setText("");
            inputPassword.getEditText().setText("");

            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY)){
                    Toast.makeText(requireActivity(),"Login Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent;

                    if(fetchType.equals(HashMapConstants.FETCH_TYPE_LOGIN_ADMIN)){
                        intent=new Intent(requireActivity(), AdminActivity.class);
                    }
                    else
                        intent=new Intent(requireActivity(), OfficerActivity.class);

                    intent.putExtra("username",username);
                    startActivity(intent);
                    requireActivity().finish();
                }
                else {
                    Toast.makeText(requireActivity(),"Invalid Login Credentials",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(),"Error in Logging! " + error + "",Toast.LENGTH_LONG).show();
            }

        }
    }
}