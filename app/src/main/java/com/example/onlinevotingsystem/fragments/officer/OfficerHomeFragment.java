package com.example.onlinevotingsystem.fragments.officer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.adapters.OfficerCandidateListAdapter;
import com.example.onlinevotingsystem.adapters.OfficerUserListAdapter;
import com.example.onlinevotingsystem.adapters.PollListAdapter;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.viewModels.CandidateListViewModel;
import com.example.onlinevotingsystem.viewModels.OfficerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OfficerHomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_officer_home, container, false);
    }

    private OfficerViewModel officerViewModel;
    private CandidateListViewModel candidateListViewModel;
    private RelativeLayout rlAddCandidate, rlEditCandidate, rlRemoveCandidate, rlUpdateProfile;
    private NavController navController;

    RecyclerView rcvCandidateList;
    RecyclerView rcvUserList;
    RecyclerView rcvPollList;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        officerViewModel=new ViewModelProvider(requireActivity()).get(OfficerViewModel.class);
        candidateListViewModel=new ViewModelProvider(requireActivity()).get(CandidateListViewModel.class);

        rlAddCandidate=view.findViewById(R.id.rlBtnAddCandidate);
        rlEditCandidate=view.findViewById(R.id.rlBtnEditCandidate);
        rlRemoveCandidate=view.findViewById(R.id.rlBtnRemoveCandidate);
        rlUpdateProfile=view.findViewById(R.id.rlBtnUpdateProfile);
        rcvCandidateList=view.findViewById(R.id.rcvAdminCandidateList);
        rcvUserList=view.findViewById(R.id.rcvOfficerUserList);
        rcvPollList=view.findViewById(R.id.rcvOfficerPollList);

        navController= Navigation.findNavController(view);

        rlAddCandidate.setOnClickListener(v -> {
            navController.navigate(R.id.addCandidateFragment);
        });

        rlUpdateProfile.setOnClickListener(v -> {
            navController.navigate(R.id.updateOfficerProfileFragment);
        });

        rlEditCandidate.setOnClickListener(v -> {
            navController.navigate(R.id.updateCandidateFragment);
        });

        rlRemoveCandidate.setOnClickListener(v -> {
            navController.navigate(R.id.removeCandidateFragment);
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcvCandidateList.setLayoutManager(linearLayoutManager);
        rcvCandidateList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(requireActivity());
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        rcvUserList.setLayoutManager(linearLayoutManager1);
        rcvUserList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(requireActivity());
        linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        rcvPollList.setLayoutManager(linearLayoutManager2);
        rcvPollList.setHasFixedSize(true);

        officerViewModel.GetPollDetails().observe(getViewLifecycleOwner(),poll -> {
            if(poll!=null){
                OfficerCandidateListAdapter adapter=new OfficerCandidateListAdapter(poll.getCandidateList());
                rcvCandidateList.setAdapter(adapter);

                candidateListViewModel.SetSinglePollDetails(poll);

                ArrayList<Poll> pollArrayList=new ArrayList<>();
                pollArrayList.add(poll);
                PollListAdapter pollListAdapter=new PollListAdapter(pollArrayList,navController,"SinglePollOfficer");
                rcvPollList.setAdapter(pollListAdapter);
            }
        });

        officerViewModel.GetUserList().observe(getViewLifecycleOwner(),users -> {
            if(users!=null && users.size()!=0){
                OfficerUserListAdapter adapter=new OfficerUserListAdapter(users);
                rcvUserList.setAdapter(adapter);
            }
        });
    }
}