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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.fragments.shared.VerifyOtpFragment;
import com.example.onlinevotingsystem.utils.CheckPhoneUtil;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ForgotPasswordFragment extends Fragment implements
        FetchFromDatabase.FetchDbInterface,
        VerifyOtpFragment.VerifyOtpInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    TextInputLayout inputId, inputPhoneNum;
    Spinner spinnerRole;
    Button btnSendOtp;

    String id, roll, phoneNum;

    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputId=view.findViewById(R.id.ilForgetPasswordID);
        inputPhoneNum=view.findViewById(R.id.ilForgetPasswordPhoneNum);
        spinnerRole=view.findViewById(R.id.spinnerForgetPasswordSelectRole);
        btnSendOtp=view.findViewById(R.id.btnForgetPasswordSendOtp);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Verifying Phone Number");

        ArrayList<String> roleList=new ArrayList<>();
        roleList.add("Voter");
        roleList.add("Officer");
        roleList.add("Admin");

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, roleList);
        spinnerRole.setAdapter(arrayAdapter);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roll=roleList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                roll=null;
            }
        });

        btnSendOtp.setOnClickListener(v -> {
            id=inputId.getEditText().getText().toString();
            phoneNum=inputPhoneNum.getEditText().getText().toString();

            if(id.isEmpty() || phoneNum.isEmpty() || roll==null){
                Toast.makeText(requireActivity(), "Please Enter all the Fields", Toast.LENGTH_SHORT).show();
            }
            else if(!CheckPhoneUtil.IsValidPhone(phoneNum)){
                Toast.makeText(requireActivity(), "Please Enter a Valid Phone Number (10 Digits onlt)", Toast.LENGTH_SHORT).show();
            }
            else {
                String phone="+91"+phoneNum;

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_VERIFY_PHONE_NUM);
                hashMap.put(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_VOTER_ID_KEY,id);
                hashMap.put(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_NUMBER_KEY,phone);

                String inputRoll=roll;
                if(roll.equals("Voter"))
                    inputRoll="VoterF";

                hashMap.put(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_ROLE_KEY,inputRoll);

                progressIndicatorFragment.show(getParentFragmentManager(),"VerifyPhoneProcess");
                new FetchFromDatabase(this,hashMap).execute();
            }

        });
    }

    @Override
    public void onOtpVerified(boolean result,String error) {
        if(result){
            Toast.makeText(requireActivity(),"OTP Verified",Toast.LENGTH_SHORT).show();
            NavDirections action=ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToResetPasswordFragment("Forget",roll,id);
            Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(action);
        }
        else {
            Toast.makeText(requireActivity(), "Error in Verifying OTP! "+error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_VERIFY_PHONE_NUM)){

            progressIndicatorFragment.dismiss();

            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_VERIFY_PHONE_NUM_KEY)){
                    String phone="+91"+phoneNum;
                    VerifyOtpFragment verifyOtpFragment=VerifyOtpFragment.newInstance(phone,ForgotPasswordFragment.this);
                    verifyOtpFragment.show(getParentFragmentManager(),"OtpFragment");
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
}