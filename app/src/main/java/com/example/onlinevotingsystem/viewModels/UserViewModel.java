package com.example.onlinevotingsystem.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.classes.User;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserViewModel extends ViewModel implements FetchFromDatabase.FetchDbInterface {

    public UserViewModel() {
        super();
        IsDataLoading=new MutableLiveData<>();
    }

    private MutableLiveData<Boolean> IsDataLoading;

    public LiveData<Boolean> CheckIsDataLoading(){
        if(IsDataLoading==null)
            IsDataLoading=new MutableLiveData<>();
        return IsDataLoading;
    }

    private MutableLiveData<User> UserDetails;

    public LiveData<User> GetUserDetails(){
        if(UserDetails==null)
            UserDetails=new MutableLiveData<>();
        return UserDetails;
    }

    private MutableLiveData<Poll> PollDetails;

    public LiveData<Poll> GetPollDetails(){
        if(PollDetails==null)
            PollDetails=new MutableLiveData<>();
        return PollDetails;
    }

    public ArrayList<Candidate> GetCandidateList(){
        return PollDetails.getValue().getCandidateList();
    }

    public String GetVoterId(){
        return UserDetails.getValue().getVoterID();
    }

    public boolean hasElectionEnd(){
        return PollDetails.getValue().getElectionEndTime() < new Date().getTime();
    }

    public boolean hasElectionStarted(){
        return PollDetails.getValue().getElectionStartTime() < new Date().getTime();
    }

    public boolean hasUserVoted(){
        return UserDetails.getValue().isHasVoted();
    }

    public User GetUser(){
        return UserDetails.getValue();
    }

    private MutableLiveData<String> Error;

    public void FetchDetails(String voterId){
        IsDataLoading.setValue(true);

        if(UserDetails==null)
            UserDetails=new MutableLiveData<>();

        if(PollDetails==null)
            PollDetails=new MutableLiveData<>();

        if(Error==null)
            Error=new MutableLiveData<>();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_EXISTING_DATA_FROM_ID);
        hashMap.put(HashMapConstants.FETCH_PARAM_EXISTING_DATA_FROM_ID_KEY,voterId);

        new FetchFromDatabase(this,hashMap).execute();
    }

    public void reloadData(){
        if(UserDetails!=null && UserDetails.getValue()!=null){
            String voterId=UserDetails.getValue().getVoterID();
            FetchDetails(voterId);
        }
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        switch ((String) resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY)){
            case HashMapConstants.FETCH_TYPE_POLL_DETAILS:{
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                    Poll poll=(Poll)resultHashMap.get(HashMapConstants.FETCH_RESULT_POLL_DETAILS_KEY);
                    PollDetails.setValue(poll);

                    IsDataLoading.setValue(false);
                }
                else {
                    String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                    Error.setValue(error);
                }
                break;
            }
            case HashMapConstants.FETCH_TYPE_EXISTING_DATA_FROM_ID:{
                if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                    User user=(User)resultHashMap.get(HashMapConstants.FETCH_RESULT_EXISTING_DATA_FROM_ID_USER_KEY);
                    UserDetails.setValue(user);

                    FetchPollDetails(user.getPollNumber());
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
}
