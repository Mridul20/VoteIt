package com.example.onlinevotingsystem.fragments.shared;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.adapters.ResultListAdapter;
import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ElectionResultFragment extends Fragment implements FetchFromDatabase.FetchDbInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_election_result, container, false);
    }

    ArrayList<Candidate> candidateResultList;
    TextView tvNotFound;
    MaterialCardView mcvResultList;
    RecyclerView rcvResultList;

    ProgressIndicatorFragment progressIndicatorFragment;

    int PollNum;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PollNum=ElectionResultFragmentArgs.fromBundle(getArguments()).getPollNum();

        tvNotFound=view.findViewById(R.id.tvElectionResultNotFound);
        mcvResultList=view.findViewById(R.id.mcvResultList);
        rcvResultList=view.findViewById(R.id.rcvElectionResult);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvResultList.setLayoutManager(linearLayoutManager);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_POLL_RESULT);
        hashMap.put(HashMapConstants.FETCH_PARAM_POLL_RESULT_POLL_NUM_KEY,PollNum);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Loading","Fetching Election Result");
        progressIndicatorFragment.show(getParentFragmentManager(),"ElectionResultProcess");

        new FetchFromDatabase(this,hashMap).execute();
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_POLL_RESULT)){
            progressIndicatorFragment.dismiss();
            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                candidateResultList=(ArrayList<Candidate>)resultHashMap.get(HashMapConstants.FETCH_RESULT_POLL_RESULT_CANDIDATE_LIST_KEY);

                ResultListAdapter adapter=new ResultListAdapter(candidateResultList);
                rcvResultList.setAdapter(adapter);
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(),"Error in Logging! " + error + "",Toast.LENGTH_LONG).show();
            }
        }
    }
}