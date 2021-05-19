package com.example.onlinevotingsystem.fragments.officer;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.activities.OfficerActivity;
import com.example.onlinevotingsystem.classes.Officer;
import com.example.onlinevotingsystem.viewModels.OfficerViewModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class OfficerDetailsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_officer_details, container, false);
    }

    TextView tvName, tvUsername, tvPhoneNum, tvPollNum;
    ImageView imgOfficer;

    OfficerViewModel officerViewModel;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        officerViewModel=new ViewModelProvider(requireActivity()).get(OfficerViewModel.class);

        tvName=view.findViewById(R.id.tvOfficerDetailsName);
        tvUsername=view.findViewById(R.id.tvOfficerDetailsUsername);
        tvPhoneNum=view.findViewById(R.id.tvOfficerDetailsPhoneNum);
        tvPollNum=view.findViewById(R.id.tvOfficerDetailsPollNum);
        imgOfficer=view.findViewById(R.id.imgOfficerDetails);

        officerViewModel.GetOfficerDetails().observe(getViewLifecycleOwner(),officer -> {
            if(officer!=null){
                tvName.setText(officer.getName());
                tvUsername.setText(officer.getUsername());
                tvPhoneNum.setText(officer.getPhoneNum().substring(3));
                tvPollNum.setText(String.format("%d",officer.getPollNumber()));

                if(officer.getPhotoURL()!=null){
                    Picasso
                            .get()
                            .load(Uri.parse(officer.getPhotoURL()))
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(imgOfficer);
                }
                else
                    imgOfficer.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.man2));
            }
        });
    }
}