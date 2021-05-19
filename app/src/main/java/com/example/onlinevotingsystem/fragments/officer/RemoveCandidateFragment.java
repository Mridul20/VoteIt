package com.example.onlinevotingsystem.fragments.officer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.viewModels.OfficerViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoveCandidateFragment extends Fragment implements DatabaseUpdater.DatabaseUpdateInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remove_candidate, container, false);
    }

    Spinner spinnerCandidateList;
    Button btnRemoveCandidate;

    OfficerViewModel officerViewModel;

    ArrayList<Candidate> candidateList;
    int currPosition;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerCandidateList=view.findViewById(R.id.spinnerRemoveCandidateList);
        btnRemoveCandidate=view.findViewById(R.id.btnRemoveCandidateSubmit);

        currPosition=-1;

        officerViewModel=new ViewModelProvider(requireActivity()).get(OfficerViewModel.class);

        officerViewModel.GetPollDetails().observe(getViewLifecycleOwner(),poll -> {
            if(poll!=null){
                candidateList=poll.getCandidateList();
                updateSpinner();
            }
        });

        btnRemoveCandidate.setOnClickListener(v -> {
            AlertDialog alertDialog=new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Warning")
                    .setMessage("Are you sure you want to Delete the Candidate")
                    .setPositiveButton("YES", (dialog, which) -> {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_DELETE_CANDIDATE);
                        hashMap.put(HashMapConstants.UPDATE_PARAM_DELETE_CANDIDATE_ID_KEY,candidateList.get(currPosition).getID());
                        hashMap.put(HashMapConstants.UPDATE_PARAM_DELETE_CANDIDATE_POLL_NUM_KEY,candidateList.get(currPosition).getPollNumber());

                        progressIndicatorFragment= ProgressIndicatorFragment.newInstance("Syncing","Deleting Candidate");
                        progressIndicatorFragment.show(getParentFragmentManager(),"DeleteCandidateProcess");
                        new DatabaseUpdater(hashMap,this).execute();
                    })
                    .setNegativeButton("NO", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create();
            alertDialog.show();
        });
    }

    ProgressIndicatorFragment progressIndicatorFragment;

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
                btnRemoveCandidate.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currPosition=-1;
                btnRemoveCandidate.setEnabled(false);
            }
        });
    }

    @Override
    public void onDataUpdated(String type, boolean result, String error) {
        if(type.equals(HashMapConstants.UPDATE_TYPE_DELETE_CANDIDATE)){
            progressIndicatorFragment.dismiss();
            if(result){
                Toast.makeText(requireActivity(), "Candidate Deleted Successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireActivity(),R.id.navHostOfficer).navigate(R.id.action_removeCandidateFragment_to_officerHomeFragment);
            }
            else {
                Toast.makeText(requireActivity(),"Error in Deleting Candidate "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}