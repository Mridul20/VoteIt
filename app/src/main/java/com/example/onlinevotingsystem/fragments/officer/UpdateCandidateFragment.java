package com.example.onlinevotingsystem.fragments.officer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.utils.CheckPhoneUtil;
import com.example.onlinevotingsystem.utils.DateTimeUtils;
import com.example.onlinevotingsystem.viewModels.OfficerViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateCandidateFragment extends Fragment implements DatabaseUpdater.DatabaseUpdateInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_candidate, container, false);
    }

    Spinner spinnerCandidateList;
    Button btnLoadDetails, btnChooseDob, btnUpdatePhoto, btnUpdateSymbolPhoto, btnSubmit;
    TextInputLayout inputName, inputPhoneNum, inputSymbol;
    TextView tvDob;

    OfficerViewModel officerViewModel;

    ArrayList<Candidate> candidateList;
    int currPosition;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerCandidateList=view.findViewById(R.id.spinnerUpdateCandidateList);
        btnLoadDetails=view.findViewById(R.id.btnUpdateCandidateLoadCandidateDetails);
        btnChooseDob=view.findViewById(R.id.btnUpdateCandidateChooseDob);
        btnUpdatePhoto=view.findViewById(R.id.btnUpdateCandidateUpdatePhoto);
        btnUpdateSymbolPhoto=view.findViewById(R.id.btnUpdateCandidateSymbolPhoto);
        btnSubmit=view.findViewById(R.id.btnUpdateCandidateSubmit);
        inputName=view.findViewById(R.id.ilUpdateCandidateName);
        inputPhoneNum=view.findViewById(R.id.ilUpdateCandidatePhoneNum);
        inputSymbol=view.findViewById(R.id.ilUpdateCandidateSymbolName);
        tvDob=view.findViewById(R.id.tvUpdateCandidateDob);

        currPosition=-1;

        officerViewModel=new ViewModelProvider(requireActivity()).get(OfficerViewModel.class);

        officerViewModel.GetPollDetails().observe(getViewLifecycleOwner(),poll -> {
            if(poll!=null){
                candidateList=poll.getCandidateList();
                updateSpinner();
            }
        });

        btnLoadDetails.setOnClickListener(v -> {
            UpdateInterface();
        });

        UpdateInterface();
    }

    private void updateSpinner(){
        ArrayList<String> candidateIdList=new ArrayList<>();
        for(int i=0;i<candidateList.size();i++)
            candidateIdList.add(candidateList.get(i).getID());

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item,candidateIdList);
        spinnerCandidateList.setAdapter(arrayAdapter);

        spinnerCandidateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currPosition=-1;
                UpdateInterface();
            }
        });
    }

    long dob;

    private void UpdateInterface(){
        if(currPosition==-1){
            btnChooseDob.setEnabled(false);
            btnUpdatePhoto.setEnabled(false);
            btnUpdateSymbolPhoto.setEnabled(false);
            btnSubmit.setEnabled(false);
            inputName.getEditText().setEnabled(false);
            inputPhoneNum.getEditText().setEnabled(false);
            inputSymbol.getEditText().setEnabled(false);
        }
        else {
            btnChooseDob.setEnabled(true);
            btnUpdatePhoto.setEnabled(true);
            btnUpdateSymbolPhoto.setEnabled(true);
            btnSubmit.setEnabled(true);
            inputName.getEditText().setEnabled(true);
            inputPhoneNum.getEditText().setEnabled(true);
            inputSymbol.getEditText().setEnabled(true);

            Candidate candidate=candidateList.get(currPosition);

            dob=candidate.getDateOfBirth();
            tvDob.setText(DateTimeUtils.getDisplayDate(candidate.getDateOfBirth()));
            inputName.getEditText().setText(candidate.getName());
            inputPhoneNum.getEditText().setText(candidate.getPhoneNumber().substring(3));
            inputSymbol.getEditText().setText(candidate.getElectionSymbolName());

            btnChooseDob.setOnClickListener(v -> {
                MaterialDatePicker datePicker=MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Set your Birthday")
                        .setSelection(dob)
                        .build();

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    dob=(Long) selection;
                    tvDob.setText(DateTimeUtils.getDisplayDate(dob));
                });

                datePicker.show(getParentFragmentManager(),"ChooseDob");
            });

            btnUpdatePhoto.setOnClickListener(v -> {
                String currentPhoto="null";
                if(candidate.getPhotoURL()!=null)
                    currentPhoto=candidate.getPhotoURL();
                NavDirections action=UpdateCandidateFragmentDirections.actionUpdateCandidateFragmentToUpdatePhotoFragment2("Candidate",candidate.getID(),currentPhoto);
                Navigation.findNavController(requireActivity(),R.id.navHostOfficer).navigate(action);
            });

            btnUpdateSymbolPhoto.setOnClickListener(v -> {
                String currentPhoto="null";
                if(candidate.getElectionSymbolPhotoURL()!=null)
                    currentPhoto=candidate.getElectionSymbolPhotoURL();
                NavDirections action=UpdateCandidateFragmentDirections.actionUpdateCandidateFragmentToUpdatePhotoFragment2("CandidateSymbol",candidate.getID(),currentPhoto);
                Navigation.findNavController(requireActivity(),R.id.navHostOfficer).navigate(action);
            });

            btnSubmit.setOnClickListener(v -> {
                String name, phoneNum, symbolName;
                name=inputName.getEditText().getText().toString();
                phoneNum=inputPhoneNum.getEditText().getText().toString();
                symbolName=inputSymbol.getEditText().getText().toString();
                if(name.isEmpty() || phoneNum.isEmpty() || symbolName.isEmpty()){
                    Toast.makeText(requireActivity(), "Please Fill all the Details", Toast.LENGTH_SHORT).show();
                }
                else if(!CheckPhoneUtil.IsValidPhone(phoneNum)){
                    Toast.makeText(requireActivity(), "Please enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog alertDialog=new MaterialAlertDialogBuilder(requireActivity())
                            .setTitle("Alert")
                            .setMessage("Are you sure you want to update the Candidate Information")
                            .setPositiveButton("YES", (dialog, which) -> {
                                String phone="+91"+phoneNum;

                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_UPDATE_CANDIDATE);
                                hashMap.put(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_ID_KEY,candidate.getID());
                                hashMap.put(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_NAME_KEY,name);
                                hashMap.put(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_PHONE_NUM_KEY,phone);
                                hashMap.put(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_DOB_KEY,dob);
                                hashMap.put(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_SYMBOL_NAME_KEY,symbolName);

                                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Updating Candidate Information");
                                progressIndicatorFragment.show(getParentFragmentManager(),"UpdateCandidateProcess");
                                new DatabaseUpdater(hashMap,this).execute();
                            })
                            .setNegativeButton("NO", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .create();
                    alertDialog.show();
                }
            });
        }
    }

    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onDataUpdated(String type, boolean result, String error) {
        if(type.equals(HashMapConstants.UPDATE_TYPE_UPDATE_CANDIDATE)){
            progressIndicatorFragment.dismiss();
            if(result){
                Toast.makeText(requireActivity(), "Candidate Updated Successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireActivity(),R.id.navHostOfficer).navigate(R.id.action_updateCandidateFragment_to_officerHomeFragment);
            }
            else {
                Toast.makeText(requireActivity(), "Error in Updating Candidate Details "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}