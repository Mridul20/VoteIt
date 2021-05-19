package com.example.onlinevotingsystem.fragments.shared;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VerifyOtpFragment extends DialogFragment {

    public interface VerifyOtpInterface{
        void onOtpVerified(boolean result, String error);
    }

    private VerifyOtpInterface otpInterface;
    private String PhoneNum;

    private void setOtpInterface(VerifyOtpInterface otpInterface) {
        this.otpInterface = otpInterface;
    }

    private void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public VerifyOtpFragment() {
        // Required empty public constructor
    }

    public static VerifyOtpFragment newInstance(String phoneNum, VerifyOtpInterface otpInterface) {
        VerifyOtpFragment fragment = new VerifyOtpFragment();
        fragment.setPhoneNum(phoneNum);
        fragment.setOtpInterface(otpInterface);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_otp, container, false);
    }

    private TextInputLayout inputOTP;
    private String otpVerificationID;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        TextView tvPhoneNum=view.findViewById(R.id.tvVerifyOtpPhoneNum);
        Button btnVerifyOtp=view.findViewById(R.id.btnVerifyOtp);
        inputOTP=view.findViewById(R.id.ilVerifyOtp);

        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(PhoneNum)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                        otpInterface.onOtpVerified(true,null);
                        getDialog().dismiss();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                        Log.e("OtpVerify",e.getLocalizedMessage());
                        otpInterface.onOtpVerified(false,e.getLocalizedMessage());
                        getDialog().dismiss();
                    }

                    @Override
                    public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        otpVerificationID=s;
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

        tvPhoneNum.setText(PhoneNum);

        btnVerifyOtp.setOnClickListener(v -> {

            String otp=inputOTP.getEditText().getText().toString();
            if(otp.isEmpty()){
                Toast.makeText(requireActivity(),"Please Enter the OTP Received",Toast.LENGTH_SHORT).show();
            }
            else {
                if(otpVerificationID!=null){
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpVerificationID,otp);

                    ProgressIndicatorFragment progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Verifying OTP");
                    progressIndicatorFragment.show(getParentFragmentManager(),"VerifyOtpProgress");

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(task -> {
                                progressIndicatorFragment.dismiss();
                                if(task.isSuccessful()){
                                    FirebaseAuth.getInstance().signOut();
                                    getDialog().dismiss();
                                    otpInterface.onOtpVerified(true,null);
                                }
                                else {
                                    Toast.makeText(requireActivity(),"Wrong OTP",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

        });

    }
}