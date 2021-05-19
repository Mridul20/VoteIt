package com.example.onlinevotingsystem.fragments.startup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.fragments.shared.VerifyOtpFragment;
import com.example.onlinevotingsystem.utils.CheckPhoneUtil;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RegisterFragment extends Fragment implements FetchFromDatabase.FetchDbInterface, VerifyOtpFragment.VerifyOtpInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    TextInputLayout inputVoterID, inputPhoneNum;
    Button btnVerifyPhoneNum;
    ProgressIndicatorFragment progressIndicatorFragment;

    String voterID,phoneNum;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputVoterID=view.findViewById(R.id.ilRegisterVoterID);
        inputPhoneNum=view.findViewById(R.id.ilRegisterPhoneNum);

        btnVerifyPhoneNum=view.findViewById(R.id.btnRegisterSendOTP);

        btnVerifyPhoneNum.setOnClickListener(v -> {
            voterID=inputVoterID.getEditText().getText().toString();
            phoneNum=inputPhoneNum.getEditText().getText().toString();

            if(voterID.isEmpty() || phoneNum.isEmpty()){
                Toast.makeText(requireActivity(),"Voter ID or Phone Number cannot be Empty!",Toast.LENGTH_SHORT).show();
            }
            else if(!CheckPhoneUtil.IsValidPhone(phoneNum)){
                Toast.makeText(requireActivity(),"Please Enter a Valid Phone Number (10 Digits Only)",Toast.LENGTH_SHORT).show();
            }
            else {
                String phone="+91"+phoneNum;

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_VERIFY_PHONE_NUM);
                hashMap.put(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_VOTER_ID_KEY,voterID);
                hashMap.put(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_NUMBER_KEY,phone);
                hashMap.put(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_ROLE_KEY,"VoterR");

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Verifying Given Credentials");
                progressIndicatorFragment.show(getParentFragmentManager(),"VerifyPhoneProgress");
                new FetchFromDatabase(this,hashMap).execute();
            }
        });

    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_VERIFY_PHONE_NUM)){

            progressIndicatorFragment.dismiss();

            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_VERIFY_PHONE_NUM_KEY)){
                    if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_VERIFY_PHONE_NUM_IS_REG_KEY)){
                        Toast.makeText(requireActivity(),"User Already Registered, Please Login to Continue!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        String phone="+91"+phoneNum;
                        VerifyOtpFragment verifyOtpFragment=VerifyOtpFragment.newInstance(phone,RegisterFragment.this);
                        verifyOtpFragment.show(getParentFragmentManager(),"OtpFragment");
                    }
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Verifying Credentials",Toast.LENGTH_LONG).show();
                }
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(),"Error in Logging! " + error + "",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onOtpVerified(boolean result, String error) {
        if(result){
            Toast.makeText(requireActivity(),"OTP Verified",Toast.LENGTH_SHORT).show();
            NavDirections action=RegisterFragmentDirections.actionRegisterSetPassword("RegisterUser","Voter",voterID);
            Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(action);
        }
        else {
            Toast.makeText(requireActivity(), "Error in Verifying OTP: "+error, Toast.LENGTH_SHORT).show();
        }
    }
}