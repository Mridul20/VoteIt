package com.example.onlinevotingsystem.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.adapters.PollListAdapter;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.viewModels.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserPollDetailsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_poll_details, container, false);
    }

    RecyclerView rcvPollList;
    UserViewModel userViewModel;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvPollList=view.findViewById(R.id.rcvUserPollList);
        userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvPollList.setLayoutManager(linearLayoutManager);
        rcvPollList.setHasFixedSize(true);

        userViewModel.GetPollDetails().observe(getViewLifecycleOwner(),poll -> {
            if(poll!=null){
                ArrayList<Poll> pollArrayList=new ArrayList<>();
                pollArrayList.add(poll);
                PollListAdapter pollListAdapter=new PollListAdapter(pollArrayList, Navigation.findNavController(view),"SinglePollUser");
                rcvPollList.setAdapter(pollListAdapter);
            }
        });
    }


}