package com.example.onlinevotingsystem.fragments.admin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.adapters.OfficerListAdapter;
import com.example.onlinevotingsystem.adapters.OfficerUserListAdapter;
import com.example.onlinevotingsystem.viewModels.AdminViewModel;
import com.example.onlinevotingsystem.viewModels.OfficerViewModel;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import org.jetbrains.annotations.NotNull;

public class AdminHomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    NavController navController;

    RecyclerView rcvOfficerList, rcvUsersList;
    CircleMenu circleMenu;

    AdminViewModel adminViewModel;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminViewModel=new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        navController=Navigation.findNavController(view);

        circleMenu=view.findViewById(R.id.cmAdminHome);
        rcvOfficerList=view.findViewById(R.id.rcvAdminOfficerList);
        rcvUsersList=view.findViewById(R.id.rcvAdminUsersList);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcvOfficerList.setLayoutManager(linearLayoutManager);
        rcvOfficerList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(requireActivity());
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        rcvUsersList.setLayoutManager(linearLayoutManager1);
        rcvUsersList.setHasFixedSize(true);

        adminViewModel.GetOfficersList().observe(getViewLifecycleOwner(),officers -> {
            if(officers!=null){
                OfficerListAdapter adapter=new OfficerListAdapter(officers);
                rcvOfficerList.setAdapter(adapter);
            }
        });

        adminViewModel.GetUserList().observe(getViewLifecycleOwner(),users -> {
            if(users!=null){
                OfficerUserListAdapter adapter=new OfficerUserListAdapter(users);
                rcvUsersList.setAdapter(adapter);
            }
        });

        circleMenu.setMainMenu(Color.parseColor("#ff8a5c"),R.drawable.menu,R.drawable.cancel)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.adminhome_new_admin)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.adminhome_new_poll)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.adminhome_update_time)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.adminhome_poll_list)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.adminhome_new_officer)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.adminhome_new_voter)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.adminhome_change_password)
                .addSubMenu(Color.parseColor("#ff8a5c"),R.drawable.adminhome_update_photo)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {

                        switch (index){

                            case 0: {
                                Toast.makeText(requireActivity(), "Add a New Admin", Toast.LENGTH_SHORT).show();
                                NavDirections action = AdminHomeFragmentDirections.actionAdminHomeFragmentToAddAdminFragment();
                                navController.navigate(action);
                                break;
                            }
                            case 1: {
                                Toast.makeText(requireActivity(), "Add a New Poll", Toast.LENGTH_SHORT).show();
                                NavDirections action = AdminHomeFragmentDirections.actionAdminHomeFragmentToAddPollFragment();
                                navController.navigate(action);
                                break;
                            }
                            case 2: {
                                Toast.makeText(requireActivity(), "Update Election Time", Toast.LENGTH_SHORT).show();
                                NavDirections action=AdminHomeFragmentDirections.actionAdminHomeFragmentToUpdateElectionTimeFragment();
                                navController.navigate(action);
                                break;
                            }

                            case 3: {
                                Toast.makeText(requireActivity(), "List of Polls", Toast.LENGTH_SHORT).show();
                                NavDirections action=AdminHomeFragmentDirections.actionAdminHomeFragmentToPollListFragment();
                                navController.navigate(action);
                                break;
                            }
                            case 4: {
                                Toast.makeText(requireActivity(), "Add a New Officer", Toast.LENGTH_SHORT).show();
                                NavDirections action=AdminHomeFragmentDirections.actionAdminHomeFragmentToAddOfficerFragment();
                                navController.navigate(action);
                                break;
                            }
                            case 5: {
                                Toast.makeText(requireActivity(), "Add a New Voter", Toast.LENGTH_SHORT).show();
                                NavDirections action=AdminHomeFragmentDirections.actionAdminHomeFragmentToAddUserFragment();
                                navController.navigate(action);
                                break;
                            }
                            case 6: {
                                Toast.makeText(requireActivity(), "Change Password", Toast.LENGTH_SHORT).show();
                                NavDirections action=AdminHomeFragmentDirections.actionAdminHomeFragmentToChangePasswordFragment2("Admin",adminViewModel.GetUsername());
                                navController.navigate(action);
                                break;
                            }
                            case 7: {
                                Toast.makeText(requireActivity(), "Update Photo", Toast.LENGTH_SHORT).show();
                                String currentPhoto="null";
                                if(adminViewModel.GetPhotoUrl()!=null)
                                    currentPhoto=adminViewModel.GetPhotoUrl();
                                NavDirections action=AdminHomeFragmentDirections.actionAdminHomeFragmentToUpdatePhotoFragment("Admin",adminViewModel.GetUsername(),currentPhoto);
                                navController.navigate(action);
                                break;
                            }
                        }

                    }
                });
    }
}