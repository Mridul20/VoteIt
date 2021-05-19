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
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.classes.Officer;
import com.example.onlinevotingsystem.classes.PollAddress;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.utils.CheckPhoneUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class AddOfficerFragment extends Fragment implements
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
        return inflater.inflate(R.layout.fragment_add_officer, container, false);
    }

    TextInputLayout inputUsername, inputName, inputPhoneNum;
    Spinner spinnerChoosePoll;
    Button btnSubmit;

    private ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUsername=view.findViewById(R.id.ilAddOfficerUsername);
        inputName=view.findViewById(R.id.ilAddOfficerName);
        inputPhoneNum=view.findViewById(R.id.ilAddOfficerPhoneNum);
        spinnerChoosePoll=view.findViewById(R.id.spinnerAddOfficerPollNum);
        btnSubmit=view.findViewById(R.id.btnAddOfficerSubmit);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_UNASSIGNED_POLLS);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Fetching the List of Unassigned Polls");
        progressIndicatorFragment.show(getParentFragmentManager(),"FetchUnassignedPolls");

        new FetchFromDatabase(this,hashMap).execute();

        btnSubmit.setOnClickListener(v -> {
            String username, name, phoneNum;
            username=inputUsername.getEditText().getText().toString();
            name=inputName.getEditText().getText().toString();
            phoneNum=inputPhoneNum.getEditText().getText().toString();

            if(username.isEmpty() || name.isEmpty() || phoneNum.isEmpty() || PollNumber==-1){
                Toast.makeText(requireActivity(), "Please Fill all the Details", Toast.LENGTH_SHORT).show();
            }
            else if(!CheckPhoneUtil.IsValidPhone(phoneNum)){
                Toast.makeText(requireActivity(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
            }
            else {
                AlertDialog alertDialog=new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to add this Officer")
                        .setPositiveButton("YES", (dialog, which) -> {
                            String phone="+91"+phoneNum;
                            Officer officer=new Officer(username,name,phone,PollNumber);

                            HashMap<String,Object> hashMap1=new HashMap<>();
                            hashMap1.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_ADD_OFFICER);
                            hashMap1.put(HashMapConstants.UPDATE_PARAM_OFFICER_KEY,officer);

                            progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing With Server","Adding New Officer");
                            progressIndicatorFragment.show(getParentFragmentManager(),"AddOfficerProgress");

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
        if(type.equals(HashMapConstants.UPDATE_TYPE_ADD_OFFICER)){
            progressIndicatorFragment.dismiss();
            if(result){
                inputUsername.getEditText().setText("");
                inputName.getEditText().setText("");
                inputPhoneNum.getEditText().setText("");
                PollNumber=-1;
                Toast.makeText(requireActivity(), "Officer Added Successfully", Toast.LENGTH_SHORT).show();

                NavDirections action=AddOfficerFragmentDirections.actionAddOfficerFragmentToAdminHomeFragment();
                Navigation.findNavController(requireActivity(),R.id.navHostAdmin).navigate(action);
            }
            else {
                Toast.makeText(requireActivity(), "Error in Adding Officer: "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_UNASSIGNED_POLLS)){
            progressIndicatorFragment.dismiss();
            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                ArrayList<PollAddress> pollAddressArrayList=(ArrayList<PollAddress>) resultHashMap.get(HashMapConstants.FETCH_RESULT_UNASSIGNED_POLLS_KEY);
                UpdateInterface(pollAddressArrayList);
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(),"Error in Fetching Unassigned Polls! " + error + "",Toast.LENGTH_LONG).show();
            }
        }
    }

    private int PollNumber;

    private void UpdateInterface(ArrayList<PollAddress> pollList){
        if(pollList.size()==0){
            btnSubmit.setEnabled(false);
            Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),"No Unassigned Poll Available",4000).show();
        }
        else {
            btnSubmit.setEnabled(true);

            ArrayList<String> addressList=new ArrayList<>();
            for(int i=0;i<pollList.size();i++)
                addressList.add(pollList.get(i).getPollAddress());

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item,addressList);
            spinnerChoosePoll.setAdapter(arrayAdapter);

            spinnerChoosePoll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String address=addressList.get(position);
                    for(int i=0;i<pollList.size();i++){
                        if(pollList.get(i).getPollAddress().equals(address)){
                            PollNumber=pollList.get(i).getPollNumber();
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    PollNumber=-1;
                }
            });

        }
    }
}