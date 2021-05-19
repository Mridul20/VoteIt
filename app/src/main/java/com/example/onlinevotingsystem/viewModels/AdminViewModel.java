package com.example.onlinevotingsystem.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlinevotingsystem.classes.Admin;
import com.example.onlinevotingsystem.classes.Officer;
import com.example.onlinevotingsystem.classes.User;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminViewModel extends ViewModel implements FetchFromDatabase.FetchDbInterface {

    public AdminViewModel() {
        super();
        IsDatabaseProcessPerformed=new MutableLiveData<>();
    }

    private MutableLiveData<Boolean> IsDatabaseProcessPerformed;

    public LiveData<Boolean> CheckIsProcessPerformed(){
        if(IsDatabaseProcessPerformed==null){
            IsDatabaseProcessPerformed=new MutableLiveData<>();
            IsDatabaseProcessPerformed.setValue(false);
        }
        return IsDatabaseProcessPerformed;
    }

    private MutableLiveData<ArrayList<Officer>> OfficerList;

    public LiveData<ArrayList<Officer>> GetOfficersList(){
        if(OfficerList==null)
            OfficerList=new MutableLiveData<>();
        return OfficerList;
    }

    private MutableLiveData<ArrayList<User>> UserList;

    public LiveData<ArrayList<User>> GetUserList(){
        if(UserList==null)
            UserList=new MutableLiveData<>();
        return UserList;
    }

    private MutableLiveData<Admin> AdminDetails;

    public LiveData<Admin> GetAdminDetails(){
        if(AdminDetails==null){
            AdminDetails=new MutableLiveData<>();
            FetchData();
        }
        return AdminDetails;
    }

    private MutableLiveData<String> Error;

    private String adminUsername;

    public void SetUsername(String username){
        adminUsername=username;
        FetchData();
    }

    public String GetUsername(){
        return adminUsername;
    }

    public String GetPhotoUrl(){
        return AdminDetails.getValue().getPhotoURL();
    }

    public void reloadData(){
        if(AdminDetails!=null && AdminDetails.getValue()!=null){
            FetchData();
        }
    }

    public void FetchData(){
        IsDatabaseProcessPerformed.setValue(true);

        if(AdminDetails==null)
            AdminDetails=new MutableLiveData<>();
        if(OfficerList==null)
            OfficerList=new MutableLiveData<>();
        if(UserList==null)
            UserList=new MutableLiveData<>();
        if(Error==null)
            Error=new MutableLiveData<>();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_ADMIN_DETAILS);
        hashMap.put(HashMapConstants.FETCH_PARAM_ADMIN_DETAILS_USERNAME_KEY,adminUsername);

        new FetchFromDatabase(this,hashMap).execute();
    }

    public void SetDatabaseProcess(boolean process) {
        if (IsDatabaseProcessPerformed == null)
            IsDatabaseProcessPerformed = new MutableLiveData<>();
        IsDatabaseProcessPerformed.setValue(process);
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_ADMIN_DETAILS)){
            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                Admin admin=(Admin)resultHashMap.get(HashMapConstants.FETCH_RESULT_ADMIN_DETAILS_KEY);
                AdminDetails.setValue(admin);

                FetchOfficersList();
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Error.setValue(error);
            }
        }
        else if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_OFFICER_LIST)){
            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                ArrayList<Officer> officerList=(ArrayList<Officer>) resultHashMap.get(HashMapConstants.FETCH_RESULT_OFFICERS_LIST_KEY);
                OfficerList.setValue(officerList);

                FetchUserList();
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Error.setValue(error);
            }
        }
        else if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_ALL_VOTERS_LIST)){
            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                ArrayList<User> userList=(ArrayList<User>) resultHashMap.get(HashMapConstants.FETCH_RESULT_ALL_VOTERS_LIST_KEY);
                UserList.setValue(userList);

                IsDatabaseProcessPerformed.setValue(false);
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                Error.setValue(error);
            }
        }
    }

    private void FetchOfficersList(){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_OFFICER_LIST);

        new FetchFromDatabase(this,hashMap).execute();
    }

    private void FetchUserList(){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_ALL_VOTERS_LIST);

        new FetchFromDatabase(this,hashMap).execute();
    }
}
