package com.example.onlinevotingsystem.fragments.admin;

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
import com.example.onlinevotingsystem.viewModels.AdminViewModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class AdminDetailsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    AdminViewModel adminViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_details, container, false);
    }

    TextView tvName, tvUsername, tvPhoneNum;
    ImageView imgAdmin;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName=view.findViewById(R.id.tvAdminDetailsName);
        tvUsername=view.findViewById(R.id.tvAdminDetailsUsername);
        tvPhoneNum=view.findViewById(R.id.tvAdminDetailsPhoneNum);
        imgAdmin=view.findViewById(R.id.imgAdminDetails);

        adminViewModel=new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        adminViewModel.GetAdminDetails().observe(getViewLifecycleOwner(),admin -> {
            if(admin!=null){
                tvName.setText(admin.getName());
                tvUsername.setText(admin.getUsername());
                tvPhoneNum.setText(admin.getPhoneNum().substring(3));
                if(admin.getPhotoURL()!=null){
                    Picasso
                            .get()
                            .load(Uri.parse(admin.getPhotoURL()))
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(imgAdmin);
                }
                else{
                    imgAdmin.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.man2));
                }
            }
        });
    }
}