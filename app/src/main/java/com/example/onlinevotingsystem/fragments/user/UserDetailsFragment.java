package com.example.onlinevotingsystem.fragments.user;

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
import com.example.onlinevotingsystem.viewModels.UserViewModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class UserDetailsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    ImageView imgUser;
    TextView tvName, tvVoterId;
    UserViewModel userViewModel;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgUser=view.findViewById(R.id.imgUserDetailsPhoto);
        tvName=view.findViewById(R.id.tvUserDetailsName);
        tvVoterId=view.findViewById(R.id.tvUserDetailsVoterID);

        userViewModel=new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.GetUserDetails().observe(getViewLifecycleOwner(),user -> {
            if(user!=null){
                tvName.setText(user.getName());
                tvVoterId.setText(user.getVoterID());

                if(user.getPhotoURL()!=null)
                    Picasso
                            .get()
                            .load(Uri.parse(user.getPhotoURL()))
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(imgUser);
                else
                    imgUser.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.user2));
            }
        });

    }


}