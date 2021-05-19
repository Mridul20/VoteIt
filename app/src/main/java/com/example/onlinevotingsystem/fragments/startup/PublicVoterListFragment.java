package com.example.onlinevotingsystem.fragments.startup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.adapters.PublicVoterListAdapter;
import com.example.onlinevotingsystem.classes.User;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class PublicVoterListFragment extends Fragment implements FetchFromDatabase.FetchDbInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public_voter_list, container, false);
    }

    RecyclerView rcvUserList;
    MaterialCardView mcvUserList;
    TextView tvNoDisplay;

    ArrayList<User> userList;

    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNoDisplay=view.findViewById(R.id.tvPublicVoterListNotFound);
        mcvUserList=view.findViewById(R.id.mcvPublicVoterList);
        rcvUserList=view.findViewById(R.id.rcvPublicVoterList);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvUserList.setLayoutManager(linearLayoutManager);
        rcvUserList.setHasFixedSize(true);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_ALL_VOTERS_LIST);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Loading Users List");
        progressIndicatorFragment.show(getParentFragmentManager(),"PublicVoterListProcess");
        new FetchFromDatabase(this,hashMap).execute();
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_ALL_VOTERS_LIST)) {
            progressIndicatorFragment.dismiss();
            if ((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)) {
                userList = (ArrayList<User>) resultHashMap.get(HashMapConstants.FETCH_RESULT_ALL_VOTERS_LIST_KEY);
                updateRcv();
            } else {
                String error = resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Toast.makeText(requireActivity(), "Error in Loading Voters List: "+error, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void updateRcv(){
        PublicVoterListAdapter listAdapter=new PublicVoterListAdapter(userList);
        rcvUserList.setAdapter(listAdapter);
    }
}