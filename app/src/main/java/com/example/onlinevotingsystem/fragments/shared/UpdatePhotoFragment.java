package com.example.onlinevotingsystem.fragments.shared;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.viewModels.AdminViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UpdatePhotoFragment extends Fragment implements DatabaseUpdater.DatabaseUpdateInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_photo, container, false);
    }

    private String type, id;
    private ProgressIndicatorFragment progressIndicatorFragment;

    TextView tvHeading;
    ImageView imgPhoto;
    Button btnChoosePhoto, btnRemovePhoto, btnSubmit;

    private final int RC_IMAGE_REQUEST=1001;
    private Uri filePath;

    String defaultPhotoUrl, currentPhotoUrl;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvHeading=view.findViewById(R.id.tvUpdatePhotoHeading);
        imgPhoto=view.findViewById(R.id.imgUpdatePhoto);
        btnChoosePhoto=view.findViewById(R.id.btnUpdatePhotoChoose);
        btnRemovePhoto=view.findViewById(R.id.btnUpdatePhotoRemove);
        btnSubmit=view.findViewById(R.id.btnUpdatePhotoUpload);

        UpdatePhotoFragmentArgs args=UpdatePhotoFragmentArgs.fromBundle(getArguments());
        type=args.getType();
        id=args.getId();
        defaultPhotoUrl=args.getPhotoUrl();
        if(defaultPhotoUrl.equals("null"))
            defaultPhotoUrl=null;
        currentPhotoUrl=defaultPhotoUrl;

        tvHeading.setText("Update Photo for "+type);
        UpdateInterface();

        btnChoosePhoto.setOnClickListener(v -> {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Image"),RC_IMAGE_REQUEST);
        });

        btnRemovePhoto.setOnClickListener(v -> {
            currentPhotoUrl=null;
            UpdateInterface();
        });

        btnSubmit.setOnClickListener(v -> {
            FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
            StorageReference storageReference=firebaseStorage.getReference().child(type+"/"+id);
            if(currentPhotoUrl!=null){
                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Uploading","Uploading Photo");
                progressIndicatorFragment.show(getParentFragmentManager(),"UploadPhotoProcess");
                storageReference
                        .putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            progressIndicatorFragment.dismiss();
                            GetDownloadUri();
                        })
                        .addOnFailureListener(e -> {
                            progressIndicatorFragment.dismiss();
                            Toast.makeText(requireActivity(), "Error in Uploading Photo", Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(snapshot -> {
                            long progress=(snapshot.getBytesTransferred()*100)/snapshot.getTotalByteCount();
                            int progressInt=(int)(long)progress;
                            progressIndicatorFragment.SetProgress(progressInt,100);
                        });
            }
            else {
                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Updating","Removing Photo");
                progressIndicatorFragment.show(getParentFragmentManager(),"RemovePhotoProcess");
                storageReference
                        .delete()
                        .addOnCompleteListener(task -> {
                            progressIndicatorFragment.dismiss();
                            if(task.isSuccessful())
                                RemovePhotoFromDatabase();
                            else {
                                Toast.makeText(requireActivity(), "Error in Removing Photo", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void RemovePhotoFromDatabase(){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_REMOVE_PHOTO);
        hashMap.put(HashMapConstants.UPDATE_PARAM_REMOVE_PHOTO_ID_KEY,id);
        hashMap.put(HashMapConstants.UPDATE_PARAM_REMOVE_PHOTO_ROLE_KEY,type);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Removing Photo");
        progressIndicatorFragment.show(getParentFragmentManager(),"PhotoRemove");
        new DatabaseUpdater(hashMap,this).execute();
    }

    private void GetDownloadUri(){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Updating Database");
        progressIndicatorFragment.show(getParentFragmentManager(),"UpdatePhotoDbProcess");
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReference().child(type+"/"+id);
        storageReference.getDownloadUrl().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                progressIndicatorFragment.dismiss();
                currentPhotoUrl=task.getResult().toString();
                UpdateInterface();
                Log.d("UploadPhoto","Photo Url "+currentPhotoUrl);
                updatePhoto();
            }
            else {
                progressIndicatorFragment.dismiss();
                Toast.makeText(requireActivity(), "Error in Getting Download Url", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateInterface(){
        if(currentPhotoUrl==null){
            btnRemovePhoto.setEnabled(false);
            imgPhoto.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.new5));
        }
        else {
            btnRemovePhoto.setEnabled(true);
            Picasso
                    .get()
                    .load(Uri.parse(currentPhotoUrl))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imgPhoto);
        }
        if(currentPhotoUrl==null && defaultPhotoUrl!=null)
            btnSubmit.setEnabled(true);
        else if(currentPhotoUrl!=null && defaultPhotoUrl==null)
            btnSubmit.setEnabled(true);
        else if(currentPhotoUrl == null && defaultPhotoUrl==null)
            btnSubmit.setEnabled(false);
        else if(!currentPhotoUrl.equals(defaultPhotoUrl))
            btnSubmit.setEnabled(true);
        else
            btnSubmit.setEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_IMAGE_REQUEST){
            if(data!=null && data.getData()!=null){
                filePath=data.getData();
                currentPhotoUrl=filePath.toString();
                UpdateInterface();
            }
        }
    }

    private void updatePhoto(){
        HashMap<String,Object> hashMap=new HashMap<>();
        switch (type){
            case "Admin":{
                hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_ADMIN_PHOTO);
                hashMap.put(HashMapConstants.UPDATE_PARAM_ADMIN_PHOTO_USERNAME_KEY,id);
                hashMap.put(HashMapConstants.UPDATE_PARAM_ADMIN_PHOTO_KEY,currentPhotoUrl);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Updating Admin Photo");
                progressIndicatorFragment.show(getParentFragmentManager(),"AdminPhoto");
                new DatabaseUpdater(hashMap,this).execute();
                break;
            }
            case "Officer":{
                hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_OFFICER_PHOTO);
                hashMap.put(HashMapConstants.UPDATE_PARAM_OFFICER_PHOTO_USERNAME_KEY,id);
                hashMap.put(HashMapConstants.UPDATE_PARAM_OFFICER_PHOTO_KEY,currentPhotoUrl);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Updating Officer Photo");
                progressIndicatorFragment.show(getParentFragmentManager(),"OfficerPhoto");
                new DatabaseUpdater(hashMap,this).execute();
                break;
            }
            case "Voter":{
                hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_VOTER_PHOTO);
                hashMap.put(HashMapConstants.UPDATE_PARAM_VOTER_PHOTO_ID_KEY,id);
                hashMap.put(HashMapConstants.UPDATE_PARAM_VOTER_PHOTO_KEY,currentPhotoUrl);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Updating Voter Photo");
                progressIndicatorFragment.show(getParentFragmentManager(),"VoterPhoto");
                new DatabaseUpdater(hashMap,this).execute();
                break;
            }
            case "Candidate":{
                hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_CANDIDATE_PHOTO);
                hashMap.put(HashMapConstants.UPDATE_PARAM_CANDIDATE_PHOTO_ID_KEY,id);
                hashMap.put(HashMapConstants.UPDATE_PARAM_CANDIDATE_PHOTO_URL_KEY,currentPhotoUrl);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Updating Candidate Photo");
                progressIndicatorFragment.show(getParentFragmentManager(),"CandidatePhoto");
                new DatabaseUpdater(hashMap,this).execute();
                break;
            }
            case "CandidateSymbol":{
                hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_CANDIDATE_SYMBOL_PHOTO);
                hashMap.put(HashMapConstants.UPDATE_PARAM_CANDIDATE_SYMBOL_PHOTO_ID_KEY,id);
                hashMap.put(HashMapConstants.UPDATE_PARAM_CANDIDATE_SYMBOL_PHOTO_URL_KEY,currentPhotoUrl);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Updating Election Symbol Photo");
                progressIndicatorFragment.show(getParentFragmentManager(),"ElectionSymbolPhoto");
                new DatabaseUpdater(hashMap,this).execute();
                break;
            }
        }
    }

    @Override
    public void onDataUpdated(String Type, boolean result, String error) {
        switch (Type){
            case HashMapConstants.UPDATE_TYPE_ADMIN_PHOTO:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"Photo Updated Successfully for Admin",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireActivity(),R.id.navHostAdmin).popBackStack(R.id.adminHomeFragment,false);
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Updating Photo",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case HashMapConstants.UPDATE_TYPE_OFFICER_PHOTO:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"Photo Updated Successfully for Officer",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireActivity(),R.id.navHostOfficer).popBackStack(R.id.officerHomeFragment,false);
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Updating Photo",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case HashMapConstants.UPDATE_TYPE_VOTER_PHOTO:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"Photo Updated Successfully for Voter",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireActivity(),R.id.navHostUser).navigate(R.id.action_updatePhotoFragment3_to_userHomeFragment);
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Updating Photo",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case HashMapConstants.UPDATE_TYPE_CANDIDATE_PHOTO:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"Photo Updated Successfully for Candidate",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireActivity(),R.id.navHostOfficer).navigate(R.id.action_updatePhotoFragment2_to_officerHomeFragment);
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Updating Photo",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case HashMapConstants.UPDATE_TYPE_CANDIDATE_SYMBOL_PHOTO:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(),"Election Symbol Photo Updated Successfully Candidate",Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireActivity(),R.id.navHostOfficer).navigate(R.id.action_updatePhotoFragment2_to_officerHomeFragment);
                }
                else {
                    Toast.makeText(requireActivity(),"Error in Updating Photo",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case HashMapConstants.UPDATE_TYPE_REMOVE_PHOTO:{
                progressIndicatorFragment.dismiss();
                if(result){
                    Toast.makeText(requireActivity(), "Photo Removed Successfully", Toast.LENGTH_SHORT).show();
                    switch (type){
                        case "Admin":{
                            Toast.makeText(requireActivity(), "Photo Removed Successfully", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(requireActivity(),R.id.navHostAdmin).popBackStack(R.id.adminHomeFragment,false);
                            break;
                        }
                        case "Officer":{
                            Toast.makeText(requireActivity(), "Photo Removed Successfully", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(requireActivity(),R.id.navHostOfficer).popBackStack(R.id.officerHomeFragment,false);
                            break;
                        }
                        case "Voter":{
                            Toast.makeText(requireActivity(),"Photo Removed Successfully",Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(requireActivity(),R.id.navHostUser).navigate(R.id.action_updatePhotoFragment3_to_userHomeFragment);
                            break;
                        }
                        case "Candidate":
                        case "CandidateSymbol": {
                            Navigation.findNavController(requireActivity(),R.id.navHostOfficer).navigate(R.id.action_updatePhotoFragment2_to_officerHomeFragment);
                            break;
                        }
                    }
                }
                else {
                    Toast.makeText(requireActivity(), "Error in Removing Photo "+error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}