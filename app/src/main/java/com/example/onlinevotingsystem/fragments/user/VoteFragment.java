package com.example.onlinevotingsystem.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
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
import com.example.onlinevotingsystem.fragments.shared.VerifyOtpFragment;
import com.example.onlinevotingsystem.viewModels.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class VoteFragment extends Fragment implements DatabaseUpdater.DatabaseUpdateInterface, VerifyOtpFragment.VerifyOtpInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vote, container, false);
    }

    Spinner spinnerCandidateList;
    Button btnCastVote;

    UserViewModel userViewModel;

    ArrayList<Candidate> candidateList;
    int currPosition;

    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerCandidateList = view.findViewById(R.id.spinnerCandidateListVoter);
        btnCastVote=view.findViewById(R.id.btnUserCastVote);

        userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.GetPollDetails().observe(getViewLifecycleOwner(),poll -> {
            if(poll!=null){
                candidateList=poll.getCandidateList();
                updateSpinner();
            }
        });

        btnCastVote.setOnClickListener(v -> {
            AlertDialog alertDialog=new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Warning")
                    .setMessage("Are you sure you want to Give Your Vote to "+candidateList.get(currPosition).getName())
                    .setPositiveButton("YES", (dialog, which) -> {
                        VerifyOtpFragment verifyOtpFragment=VerifyOtpFragment.newInstance(userViewModel.GetUser().getPhoneNumber(),this);
                        verifyOtpFragment.show(getParentFragmentManager(),"VerifyOtp");
                    })
                    .setNegativeButton("NO", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create();
            alertDialog.show();
        });
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
                btnCastVote.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currPosition=-1;
                btnCastVote.setEnabled(false);
            }
        });
    }

    private void castVote(){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_CAST_VOTE);
        hashMap.put(HashMapConstants.UPDATE_PARAM_CAST_VOTE_VOTER_ID_KEY,userViewModel.GetVoterId());
        hashMap.put(HashMapConstants.UPDATE_PARAM_CAST_VOTE_CANDIDATE_ID_KEY,candidateList.get(currPosition).getID());
        hashMap.put(HashMapConstants.UPDATE_PARAM_CAST_VOTE_POLL_NUM_KEY,candidateList.get(currPosition).getPollNumber());

        progressIndicatorFragment= ProgressIndicatorFragment.newInstance("Syncing","Casting your Vote");
        progressIndicatorFragment.show(getParentFragmentManager(),"CastVoteProcess");
        new DatabaseUpdater(hashMap,this).execute();
    }

    @Override
    public void onDataUpdated(String type, boolean result, String error) {
        if(type.equals(HashMapConstants.UPDATE_TYPE_CAST_VOTE)){
            progressIndicatorFragment.dismiss();
            if(result){
                Toast.makeText(requireActivity(), "Vote Casted Successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireActivity(),R.id.navHostUser).navigate(R.id.action_voteFragment_to_userHomeFragment);
            }
            else {
                Toast.makeText(requireActivity(), "Error in Casting Vote "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onOtpVerified(boolean result,String error) {
        if(result)
            castVote();
        else {
            Toast.makeText(requireActivity(), "Error in Verifying OTP "+error, Toast.LENGTH_SHORT).show();
        }
    }
}