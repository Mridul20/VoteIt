package com.example.onlinevotingsystem.viewModels;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.FetchFromDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CandidateListViewModel extends ViewModel implements FetchFromDatabase.FetchDbInterface {

    private MutableLiveData<ArrayList<Poll>> PollList;
    private MutableLiveData<Boolean> IsListLoading;
    private MutableLiveData<Pair<String,String>> ExceptionError;

    private boolean IsSinglePoll;

    public boolean CheckIsSinglePoll(){
        return IsSinglePoll;
    }

    public void SetSinglePollDetails(Poll poll){
        IsSinglePoll=true;
        PollList=new MutableLiveData<>();
        ArrayList<Poll> arrayList=new ArrayList<>();
        arrayList.add(poll);
        PollList.setValue(arrayList);
    }

    public LiveData<ArrayList<Poll>> GetPollList(){
        IsSinglePoll=false;
        if(PollList==null){
            PollList=new MutableLiveData<>();
        }
        fetchPollList();
        return PollList;
    }

    public LiveData<Boolean> CheckIsListLoading(){
        if(IsListLoading==null){
            IsListLoading=new MutableLiveData<>();
            IsListLoading.setValue(false);
        }
        return IsListLoading;
    }

    private LiveData<Pair<String,String>> GetExceptionError(){
        if(ExceptionError==null){
            ExceptionError=new MutableLiveData<>();
            ExceptionError.setValue(new Pair<>("TYPE","ERROR"));
        }
        return ExceptionError;
    }

    private void fetchPollList(){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(HashMapConstants.FETCH_PARAM_TYPE_KEY,HashMapConstants.FETCH_TYPE_POLL_LIST);
        IsListLoading.setValue(true);
        new FetchFromDatabase(this,hashMap).execute();
    }

    @Override
    public void onFetchCompleted(HashMap<String, Object> resultHashMap) {
        if(resultHashMap.get(HashMapConstants.FETCH_RESULT_TYPE_KEY).equals(HashMapConstants.FETCH_TYPE_POLL_LIST)){
            IsListLoading.setValue(false);
            if((Boolean) resultHashMap.get(HashMapConstants.FETCH_RESULT_SUCCESS_KEY)){
                ArrayList<Poll> fetchedPollList=(ArrayList<Poll>) resultHashMap.get(HashMapConstants.FETCH_RESULT_POLL_LIST_KEY);
                PollList.setValue(fetchedPollList);
            }
            else {
                String error=resultHashMap.get(HashMapConstants.FETCH_RESULT_ERROR_KEY).toString();
                if(ExceptionError==null)
                    ExceptionError=new MutableLiveData<>();
                ExceptionError.setValue(new Pair<>(HashMapConstants.FETCH_TYPE_POLL_LIST,error));
            }
        }
    }

    public boolean IsViewModelUsed(){
        return PollList != null;
    }

    public ArrayList<Candidate> GetPollCandidateList(int position){
        return PollList.getValue().get(position).getCandidateList();
    }
}
