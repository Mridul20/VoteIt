package com.example.onlinevotingsystem.fragments.shared;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.adapters.CandidateListAdapter;
import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.viewModels.CandidateListViewModel;
import com.example.onlinevotingsystem.viewModels.UserViewModel;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CandidateListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_candidate_list, container, false);
    }

    ArrayList<Candidate> candidateList;

    TextView tvNoCandidateFound;
    MaterialCardView mcvCandidateList;
    RecyclerView rcvCandidateList;

    CandidateListAdapter candidateListAdapter;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CandidateListFragmentArgs args=CandidateListFragmentArgs.fromBundle(getArguments());
        int position=args.getPosition();

        CandidateListViewModel listViewModel=new ViewModelProvider(requireActivity()).get(CandidateListViewModel.class);
        if(listViewModel.IsViewModelUsed())
            candidateList=listViewModel.GetPollCandidateList(position);
        else {
            UserViewModel viewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);
            candidateList=viewModel.GetCandidateList();
        }

        tvNoCandidateFound=view.findViewById(R.id.tvCandidateListNotFound);
        mcvCandidateList=view.findViewById(R.id.mcvCandidateList);
        rcvCandidateList=view.findViewById(R.id.rcvCandidateList);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvCandidateList.setLayoutManager(linearLayoutManager);
        rcvCandidateList.setHasFixedSize(true);

        if(candidateList.size()==0){
            tvNoCandidateFound.setVisibility(View.VISIBLE);
            mcvCandidateList.setVisibility(View.GONE);
            rcvCandidateList.setVisibility(View.GONE);
        }
        else {
            tvNoCandidateFound.setVisibility(View.GONE);
            mcvCandidateList.setVisibility(View.VISIBLE);
            rcvCandidateList.setVisibility(View.VISIBLE);

            candidateListAdapter=new CandidateListAdapter(candidateList);
            rcvCandidateList.setAdapter(candidateListAdapter);
        }

    }
}