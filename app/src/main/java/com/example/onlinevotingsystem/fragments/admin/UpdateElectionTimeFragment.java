package com.example.onlinevotingsystem.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.utils.DateTimeUtils;
import com.example.onlinevotingsystem.viewModels.CandidateListViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UpdateElectionTimeFragment extends Fragment implements DatabaseUpdater.DatabaseUpdateInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_election_time, container, false);
    }

    CandidateListViewModel viewModel;
    TextInputLayout inputPollNum;
    TextView tvStartTime, tvEndTime;
    Button btnFetchTime, btnSetStartTime, btnSetEndTime, btnSubmit;

    Boolean isPollSet;
    int PollNum;
    long startTime, endTime, originalStartTime, originalEndTime;
    ArrayList<Poll> pollList;

    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel=new ViewModelProvider(requireActivity()).get(CandidateListViewModel.class);

        inputPollNum=view.findViewById(R.id.ilUpdateElectionTimePollNum);
        tvStartTime=view.findViewById(R.id.tvUpdateElectionTimeStart);
        tvEndTime=view.findViewById(R.id.tvUpdateElectionTimeEnd);
        btnFetchTime=view.findViewById(R.id.btnUpdateElectionTimeFetch);
        btnSetStartTime=view.findViewById(R.id.btnUpdateSetElectionStartTime);
        btnSetEndTime=view.findViewById(R.id.btnUpdateSetElectionEndTime);
        btnSubmit=view.findViewById(R.id.btnUpdateElectionTimeSubmit);

        isPollSet=false;
        startTime=new Date().getTime();
        endTime=new Date().getTime();
        updateInterface();

        progressIndicatorFragment= ProgressIndicatorFragment.newInstance("Loading List","Fetching Polls Details");

        viewModel.CheckIsListLoading().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean){
                if(!progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.show(getParentFragmentManager(),"CandidateListProgress");
            }
            else {
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });

        viewModel.GetPollList().observe(getViewLifecycleOwner(),polls -> {
            if(polls!=null){
                pollList=polls;
            }
        });

        btnFetchTime.setOnClickListener(v -> {
            String pollNumStr=inputPollNum.getEditText().getText().toString();
            if(pollNumStr.isEmpty()){
                Toast.makeText(requireActivity(), "Please Enter a Poll Number", Toast.LENGTH_SHORT).show();
                isPollSet=false;
                startTime=new Date().getTime();
                endTime=new Date().getTime();
                updateInterface();
            }
            else {
                int pollNum=Integer.parseInt(pollNumStr);
                if(pollNum<=0 || pollNum>pollList.size()){
                    Toast.makeText(requireActivity(), "Invalid Poll Number!", Toast.LENGTH_SHORT).show();
                    isPollSet=false;
                    startTime=new Date().getTime();
                    endTime=new Date().getTime();
                    updateInterface();
                }
                else {
                    PollNum=pollNum;
                    startTime=pollList.get(pollNum-1).getElectionStartTime();
                    originalStartTime=startTime;
                    endTime=pollList.get(pollNum-1).getElectionEndTime();
                    originalEndTime=endTime;
                    isPollSet=true;
                    updateInterface();
                }
            }
        });

        Calendar calendar=Calendar.getInstance();

        btnSetStartTime.setOnClickListener(v -> {
            calendar.setTimeInMillis(startTime);
            MaterialDatePicker datePicker=MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Set Election Starting Date")
                    .setSelection(startTime)
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                startTime=(Long) selection;
                calendar.setTimeInMillis(startTime);
                updateInterface();

                MaterialTimePicker timePicker=new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                        .setMinute(calendar.get(Calendar.MINUTE))
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setTitleText("Choose Election Start Time")
                        .build();

                timePicker.addOnPositiveButtonClickListener(v1 -> {
                    calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                    calendar.set(Calendar.MINUTE,timePicker.getMinute());
                    calendar.set(Calendar.SECOND,0);

                    startTime=calendar.getTimeInMillis();
                    updateInterface();
                });
                timePicker.show(getParentFragmentManager(),"SelectElectionStartTime");
            });
            datePicker.show(getParentFragmentManager(),"SelectElectionStartDate");
        });

        btnSetEndTime.setOnClickListener(v -> {
            calendar.setTimeInMillis(endTime);
            MaterialDatePicker datePicker=MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Set Election Ending Date")
                    .setSelection(endTime)
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                endTime=(Long) selection;
                calendar.setTimeInMillis(endTime);
                updateInterface();

                MaterialTimePicker timePicker=new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                        .setMinute(calendar.get(Calendar.MINUTE))
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setTitleText("Choose Election End Time")
                        .build();

                timePicker.addOnPositiveButtonClickListener(v1 -> {
                    calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                    calendar.set(Calendar.MINUTE,timePicker.getMinute());
                    calendar.set(Calendar.SECOND,0);

                    endTime=calendar.getTimeInMillis();
                    updateInterface();
                });
                timePicker.show(getParentFragmentManager(),"SelectElectionEndTime");
            });

            datePicker.show(getParentFragmentManager(),"SelectElectionEndDate");
        });

        btnSubmit.setOnClickListener(v -> {
            if(isTimeValid()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(HashMapConstants.UPDATE_TYPE_KEY, HashMapConstants.UPDATE_TYPE_POLL_ELECTION_TIME);
                hashMap.put(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_TIME_POLL_NUM_KEY, PollNum);
                hashMap.put(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_TIME_START_KEY, startTime);
                hashMap.put(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_TIME_END_KEY, endTime);

                progressIndicatorFragment = ProgressIndicatorFragment.newInstance("Syncing", "Updating Election Time");
                progressIndicatorFragment.show(getParentFragmentManager(), "UpdateElectionTimeProgress");

                new DatabaseUpdater(hashMap, this).execute();
            }
        });
    }

    private boolean isTimeValid(){
        if((startTime<originalStartTime && originalStartTime<new Date().getTime()) || (startTime>originalStartTime && startTime<new Date().getTime())){
            showToast("You can only Update the Start Time to a Time in Future or leave it Unchanged");
            return false;
        }
        else if(endTime<new Date().getTime()){
            showToast("Election End Time cannot be in Past");
            return false;
        }
        else if(startTime>=endTime){
            showToast("Election End Time should be after the Start Time");
            return false;
        }
        return true;
    }

    private void showToast(String message){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateInterface(){
        if(isPollSet){
            btnSetStartTime.setEnabled(true);
            btnSetEndTime.setEnabled(true);
            btnSubmit.setEnabled(true);
        }
        else {
            btnSetStartTime.setEnabled(false);
            btnSetEndTime.setEnabled(false);
            btnSubmit.setEnabled(false);
        }
        tvStartTime.setText(getDisplayTime(startTime));
        tvEndTime.setText(getDisplayTime(endTime));
    }

    private String  getDisplayTime(long time){
        return DateTimeUtils.getDisplayDate(time)+" "+DateTimeUtils.getDisplayTime(time);
    }

    @Override
    public void onDataUpdated(String type, boolean result, String error) {
        if(type.equals(HashMapConstants.UPDATE_TYPE_POLL_ELECTION_TIME)){
            progressIndicatorFragment.dismiss();
            if(result){
                Toast.makeText(requireActivity(), "Election Time Updated Successfully", Toast.LENGTH_SHORT).show();
                NavDirections action=UpdateElectionTimeFragmentDirections.actionUpdateElectionTimeFragmentToAdminHomeFragment();
                Navigation.findNavController(requireActivity(),R.id.navHostAdmin).navigate(action);
            }
            else {
                Toast.makeText(requireActivity(), "Error in Updating Election Time! Error: "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}