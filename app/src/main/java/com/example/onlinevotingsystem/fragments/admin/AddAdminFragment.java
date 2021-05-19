package com.example.onlinevotingsystem.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.classes.Admin;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.utils.CheckPhoneUtil;
import com.example.onlinevotingsystem.viewModels.AdminViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AddAdminFragment extends Fragment implements DatabaseUpdater.DatabaseUpdateInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_admin, container, false);
    }

    TextInputLayout inputUsername, inputName, inputPhoneNum;
    Button btnSubmit;
    AdminViewModel adminViewModel;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUsername=view.findViewById(R.id.ilAddAdminUsername);
        inputName=view.findViewById(R.id.ilAddAdminName);
        inputPhoneNum=view.findViewById(R.id.ilAddAdminPhoneNum);

        btnSubmit=view.findViewById(R.id.btnAddAdmin);

        adminViewModel=new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        btnSubmit.setOnClickListener(v -> {

            String username, name, phoneNum;
            username=inputUsername.getEditText().getText().toString();
            name=inputName.getEditText().getText().toString();
            phoneNum=inputPhoneNum.getEditText().getText().toString();

            if(username.isEmpty() || name.isEmpty() || phoneNum.isEmpty()){
                Toast.makeText(requireActivity(),"Please Enter all the Details", Toast.LENGTH_SHORT).show();
            }
            else if(!CheckPhoneUtil.IsValidPhone(phoneNum)){
                Toast.makeText(requireActivity(),"Please Enter a Valid Phone Number",Toast.LENGTH_SHORT).show();
            }
            else {
                AlertDialog alertDialog=new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Alert")
                        .setMessage("Are You sure you want to add this admin")
                        .setPositiveButton("YES", (dialog, which) -> {
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_ADD_ADMIN);
                            String phone="+91"+phoneNum;
                            Admin admin=new Admin(username,name,phone);
                            hashMap.put(HashMapConstants.UPDATE_PARAM_ADMIN_KEY,admin);

                            adminViewModel.SetDatabaseProcess(true);
                            new DatabaseUpdater(hashMap,this).execute();
                        })
                        .setNegativeButton("NO", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onDataUpdated(String type, boolean result, String error) {
        if(type.equals(HashMapConstants.UPDATE_TYPE_ADD_ADMIN)){
            adminViewModel.SetDatabaseProcess(false);
            if(result){
                inputUsername.getEditText().setText("");
                inputName.getEditText().setText("");
                inputPhoneNum.getEditText().setText("");
                Toast.makeText(requireActivity(),"Admin Added Successfully",Toast.LENGTH_SHORT).show();

                NavDirections action=AddAdminFragmentDirections.actionAddAdminFragmentToAdminHomeFragment();
                Navigation.findNavController(requireActivity(),R.id.navHostAdmin).navigate(action);
            }
            else {
                Toast.makeText(requireActivity(),"Error In Adding Admin "+error,Toast.LENGTH_LONG).show();
            }
        }
    }
}