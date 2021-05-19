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
import com.example.onlinevotingsystem.activities.UserActivity;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

public class UserLoginFragment extends Fragment implements FetchFromDatabase.FetchDbInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_login, container, false);
    }

    private TextInputLayout inputLayoutVoterID, inputLayoutPassword;
    private ProgressIndicatorFragment progressIndicatorFragment;

    private String voterID;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputLayoutVoterID=view.findViewById(R.id.ilUserVoterID);
        inputLayoutPassword=view.findViewById(R.id.ilUserPassword);

        Button btnSubmit = view.findViewById(R.id.btnUserLoginSubmit);
        Button btnForgotPassword = view.findViewById(R.id.btnUserForgotPassword);
        Button btnOpenRegister = view.findViewById(R.id.btnLoginToRegister);
        Button btnOpenAdminLogin = view.findViewById(R.id.btnLoginToAdminOfficerLogin);
        Button btnViewVoterList=view.findViewById(R.id.btnViewPublicVoterList);

        btnSubmit.setOnClickListener(v -> {
            voterID=inputLayoutVoterID.getEditText().getText().toString();
            String password=inputLayoutPassword.getEditText().getText().toString();

            if(voterID.isEmpty() || password.isEmpty()){
                Toast.makeText(requireActivity(),"Username or Password cannot be Empty!",Toast.LENGTH_SHORT).show();
            }
            else {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_LOGIN_USER);
                hashMap.put(HashMapConstants.FETCH_PARAM_LOGIN_USERNAME_KEY,voterID);
                hashMap.put(HashMapConstants.FETCH_PARAM_LOGIN_PASSWORD_KEY,password);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Authenticating","Signing in with the Given Credentials");
                progressIndicatorFragment.show(getParentFragmentManager(),"UserLoginProgress");
                new FetchFromDatabase(this,hashMap).execute();
            }

        });

        btnForgotPassword.setOnClickListener(v -> Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(R.id.forgotPasswordFragment));

        btnOpenRegister.setOnClickListener(v -> Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(R.id.registerFragment));

        btnOpenAdminLogin.setOnClickListener(v -> Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(R.id.adminOfficerLoginFragment));

        btnViewVoterList.setOnClickListener(v -> Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(R.id.publicVoterListFragment));
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_LOGIN_USER)){

            progressIndicatorFragment.dismiss();
            inputLayoutVoterID.getEditText().setText("");
            inputLayoutPassword.getEditText().setText("");

            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY)){
                    Toast.makeText(requireActivity(),"Login Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(requireActivity(), UserActivity.class);
                    intent.putExtra("UserVoterID",voterID);
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