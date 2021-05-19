package com.example.onlinevotingsystem.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.classes.Officer;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.classes.PollAddress;
import com.example.onlinevotingsystem.classes.User;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.utils.CheckPhoneUtil;
import com.example.onlinevotingsystem.utils.DateTimeUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddUserFragment extends Fragment implements
        FetchFromDatabase.FetchDbInterface,
        DatabaseUpdater.DatabaseUpdateInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user, container, false);
    }

    TextInputLayout inputName,inputPhoneNum;
    Button btnChooseDob,btnSubmit;
    TextView tvUserDob;
    Spinner spinnerAddUserPoll;

    long dob;
    int PollNumber;

    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputName=view.findViewById(R.id.ilAddUserName);
        inputPhoneNum=view.findViewById(R.id.ilAddUserPhoneNum);
        btnChooseDob=view.findViewById(R.id.btnAddUserChooseDOB);
        btnSubmit=view.findViewById(R.id.btnAddUserSubmit);
        tvUserDob=view.findViewById(R.id.tvAddUserDOB);
        spinnerAddUserPoll=view.findViewById(R.id.spinnerAddUserPollNum);

        dob=new Date().getTime();
        tvUserDob.setText(DateTimeUtils.getDisplayDate(dob));

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_POLLS_ADDRESS);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Fetching the List of Polls");
        progressIndicatorFragment.show(getParentFragmentManager(),"FetchPollAddressProgress");

        new FetchFromDatabase(this,hashMap).execute();

        btnChooseDob.setOnClickListener(v -> {
            MaterialDatePicker datePicker=MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Set your Birthday")
                    .setSelection(dob)
                    .build();

            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    dob=(Long) selection;
                    tvUserDob.setText(DateTimeUtils.getDisplayDate(dob));
                }
            });

            datePicker.show(getParentFragmentManager(),"ChooseDob");
        });

        btnSubmit.setOnClickListener(v -> {
            String name, phoneNum;
            name=inputName.getEditText().getText().toString();
            phoneNum=inputPhoneNum.getEditText().getText().toString();

            if(name.isEmpty() || phoneNum.isEmpty() || PollNumber==-1){
                Toast.makeText(requireActivity(), "Please Fill all the Details", Toast.LENGTH_SHORT).show();
            }
            else if(!CheckPhoneUtil.IsValidPhone(phoneNum)){
                Toast.makeText(requireActivity(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
            }
            else {
                AlertDialog alertDialog=new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to add the new User")
                        .setPositiveButton("YES", (dialog, which) -> {
                            String phone="+91"+phoneNum;
                            User user=new User(name,phone,PollNumber,dob);

                            HashMap<String,Object> hashMap1=new HashMap<>();
                            hashMap1.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_ADD_USER);
                            hashMap1.put(HashMapConstants.UPDATE_PARAM_ADD_USER_KEY,user);

                            progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing With Server","Adding New User");
                            progressIndicatorFragment.show(getParentFragmentManager(),"AddUserProgress");

                            new DatabaseUpdater(hashMap1,this).execute();
                        })
                        .setNegativeButton("NO", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onDataUpdated(String type, boolean result, String error) {
        if(type.equals(HashMapConstants.UPDATE_TYPE_ADD_USER)){
            progressIndicatorFragment.dismiss();
            if(result){
                inputName.getEditText().setText("");
                inputPhoneNum.getEditText().setText("");
                PollNumber=1;
                spinnerAddUserPoll.setSelection(0);
                dob=new Date().getTime();
                Toast.makeText(requireActivity(), "User Added Successfully", Toast.LENGTH_SHORT).show();

                NavDirections action=AddUserFragmentDirections.actionAddUserFragmentToAdminHomeFragment();
                Navigation.findNavController(requireActivity(),R.id.navHostAdmin).navigate(action);
            }
            else {
                Toast.makeText(requireActivity(), "Error in Adding User: "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_POLLS_ADDRESS)){
            progressIndicatorFragment.dismiss();
            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                ArrayList<PollAddress> pollAddressArrayList=(ArrayList<PollAddress>) resultHashMap.get(HashMapConstants.FETCH_RESULT_POLLS_ADDRESS_KEY);
                UpdateInterface(pollAddressArrayList);
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(),"Error in Fetching Polls List! " + error + "",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void UpdateInterface(ArrayList<PollAddress> pollList){
        if(pollList.size()==0){
            btnSubmit.setEnabled(false);
            Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),"No Poll Available", BaseTransientBottomBar.LENGTH_INDEFINITE).show();
        }
        else {
            btnSubmit.setEnabled(true);

            ArrayList<String> addressList=new ArrayList<>();
            for(int i=0;i<pollList.size();i++)
                addressList.add(pollList.get(i).getPollAddress());

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item,addressList);
            spinnerAddUserPoll.setAdapter(arrayAdapter);

            spinnerAddUserPoll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PollNumber=position+1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    PollNumber=-1;
                }
            });
        }
    }

}