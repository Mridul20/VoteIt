package com.example.onlinevotingsystem.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlinevotingsystem.classes.Officer;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.classes.User;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class OfficerViewModel extends ViewModel implements FetchFromDatabase.FetchDbInterface {

    public OfficerViewModel() {
        super();
        IsDataLoading=new MutableLiveData<>();
    }

    private MutableLiveData<Boolean> IsDataLoading;

    public LiveData<Boolean> CheckIsListLoading(){
        if(IsDataLoading==null){
            IsDataLoading=new MutableLiveData<>();
            IsDataLoading.setValue(false);
        }
        return IsDataLoading;
    }

    private MutableLiveData<Poll> OfficerPoll;
    private MutableLiveData<Officer> OfficerDetails;

    private MutableLiveData<ArrayList<User>> PollUsersList;

    private MutableLiveData<String> Error;

    public LiveData<Poll> GetPollDetails(){
        if(OfficerPoll==null)
            OfficerPoll=new MutableLiveData<>();
        return OfficerPoll;
    }

    public LiveData<Officer> GetOfficerDetails(){
        if(OfficerDetails==null)
            OfficerDetails=new MutableLiveData<>();
        return OfficerDetails;
    }

    public LiveData<ArrayList<User>> GetUserList(){
        if(PollUsersList==null)
            PollUsersList=new MutableLiveData<>();
        return PollUsersList;
    }

    public String GetOfficerUsername(){
        return OfficerDetails.getValue().getUsername();
    }

    public String GetPhotoUrl(){
        return OfficerDetails.getValue().getPhotoURL();
    }

    public void FetchDetails(String username){
        IsDataLoading.setValue(true);

        if(OfficerPoll==null)
            OfficerPoll=new MutableLiveData<>();
        if(OfficerDetails==null)
            OfficerDetails=new MutableLiveData<>();
        if(PollUsersList==null)
            PollUsersList=new MutableLiveData<>();
        if(Error==null)
            Error=new MutableLiveData<>();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_OFFICER_DETAILS);
        hashMap.put(HashMapConstants.FETCH_PARAM_OFFICER_DETAILS_USERNAME_KEY,username);

        new FetchFromDatabase(this,hashMap).execute();
    }

    public void reloadData(){
        if(OfficerDetails!=null && OfficerDetails.getValue()!=null){
            String username=OfficerDetails.getValue().getUsername();
            FetchDetails(username);
        }
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {

        switch ((String) resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY)){
            case HashMapConstants.FETCH_TYPE_OFFICER_DETAILS:{
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                    Officer officer=(Officer)resultHashMap.get(HashMapConstants.FETCH_RESULT_OFFICER_DETAILS_KEY);
                    OfficerDetails.setValue(officer);

                    FetchPollDetails(officer.getPollNumber());
                }
                else {
                    String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                    Error.setValue(error);
                }
                break;
            }
            case HashMapConstants.FETCH_TYPE_POLL_DETAILS:{
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                    Poll poll=(Poll)resultHashMap.get(HashMapConstants.FETCH_RESULT_POLL_DETAILS_KEY);
                    OfficerPoll.setValue(poll);

                    FetchUsersList(poll.getPollNumber());
                }
                else {
                    String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                    Error.setValue(error);
                }
                break;
            }
            case HashMapConstants.FETCH_TYPE_POLL_VOTERS_LIST:{
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                    ArrayList<User> userList=(ArrayList<User>) resultHashMap.get(HashMapConstants.FETCH_RESULT_POLL_VOTERS_LIST_KEY);
                    PollUsersList.setValue(userList);

                    IsDataLoading.setValue(false);
                }
                else {
                    String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                    Error.setValue(error);
                }
                break;
            }
        }

    }

    private void FetchPollDetails(int PollNum){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_POLL_DETAILS);
        hashMap.put(HashMapConstants.FETCH_PARAM_POLL_DETAILS_POLL_NUM_KEY,PollNum);

        new FetchFromDatabase(this,hashMap).execute();
    }

    private void FetchUsersList(int pollNum){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_POLL_VOTERS_LIST);
        hashMap.put(HashMapConstants.FETCH_PARAM_POLL_VOTERS_LIST_POLL_NUM_KEY,pollNum);

        new FetchFromDatabase(this,hashMap).execute();
    }
}
