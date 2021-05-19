package com.example.onlinevotingsystem.fragments.shared;

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
import android.widget.TextView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.adapters.PollListAdapter;
import com.example.onlinevotingsystem.viewModels.CandidateListViewModel;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class PollListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poll_list, container, false);
    }

    private ProgressIndicatorFragment progressIndicatorFragment;

    TextView tvNoPollFound;
    MaterialCardView mcvPollList;
    RecyclerView rcvPollList;

    NavController navController;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CandidateListViewModel listViewModel=new ViewModelProvider(requireActivity()).get(CandidateListViewModel.class);

        tvNoPollFound=view.findViewById(R.id.tvPollListNotFound);
        mcvPollList=view.findViewById(R.id.mcvPollList);
        rcvPollList=view.findViewById(R.id.rcvPollList);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvPollList.setLayoutManager(linearLayoutManager);
        rcvPollList.setHasFixedSize(true);

        navController= Navigation.findNavController(view);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Loading List","Fetching List of Candidates from Server");

        listViewModel.CheckIsListLoading().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean){
                if(!progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.show(getParentFragmentManager(),"CandidateListProgress");
            }
            else {
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });

        listViewModel.GetPollList().observe(getViewLifecycleOwner(),polls -> {
            if(polls!=null){
                if(polls.size()==0){
                    tvNoPollFound.setVisibility(View.VISIBLE);
                    mcvPollList.setVisibility(View.GONE);
                    rcvPollList.setVisibility(View.GONE);
                }
                else {
                    tvNoPollFound.setVisibility(View.GONE);
                    mcvPollList.setVisibility(View.VISIBLE);
                    rcvPollList.setVisibility(View.VISIBLE);

                    PollListAdapter pollListAdapter=new PollListAdapter(polls,navController);
                    rcvPollList.setAdapter(pollListAdapter);
                }
            }
        });
    }
}