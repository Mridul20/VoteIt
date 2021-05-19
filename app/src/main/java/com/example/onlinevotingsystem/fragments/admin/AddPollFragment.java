package com.example.onlinevotingsystem.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.database.DatabaseUpdater;
import com.example.onlinevotingsystem.fragments.shared.ProgressIndicatorFragment;
import com.example.onlinevotingsystem.utils.DateTimeUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddPollFragment extends Fragment implements DatabaseUpdater.DatabaseUpdateInterface {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_poll, container, false);
    }

    TextInputLayout ilAddress;
    TextView tvElectionStartTime, tvElectionEndTime;
    Button btnSetElectionStartTime, btnSetElectionEndTime, btnAddPoll;

    long electionStartTime, electionEndTime;

    private ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ilAddress=view.findViewById(R.id.ilAddPollAddress);
        tvElectionStartTime=view.findViewById(R.id.tvAddPollElectionStartTime);
        tvElectionEndTime=view.findViewById(R.id.tvAddPollElectionEndTime);

        btnSetElectionStartTime=view.findViewById(R.id.btnSetElectionStartTime);
        btnSetElectionEndTime=view.findViewById(R.id.btnSetElectionEndTime);
        btnAddPoll=view.findViewById(R.id.btnAddPollSubmit);

        electionStartTime=new Date().getTime();
        electionEndTime=new Date().getTime();

        tvElectionStartTime.setText(getDisplayTime(electionStartTime));
        tvElectionEndTime.setText(getDisplayTime(electionEndTime));

        Calendar calendar=Calendar.getInstance();

        btnSetElectionStartTime.setOnClickListener(v -> {
            calendar.setTimeInMillis(electionStartTime);
            MaterialDatePicker datePicker=MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Set Election Starting Date")
                    .setSelection(electionStartTime)
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                electionStartTime=(Long) selection;
                calendar.setTimeInMillis(electionStartTime);
                tvElectionStartTime.setText(getDisplayTime(electionStartTime));

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

                    electionStartTime=calendar.getTimeInMillis();
                    tvElectionStartTime.setText(getDisplayTime(electionStartTime));
                });
                timePicker.show(getParentFragmentManager(),"SelectElectionStartTime");
            });
            datePicker.show(getParentFragmentManager(),"SelectElectionStartDate");
        });

        btnSetElectionEndTime.setOnClickListener(v -> {
            calendar.setTimeInMillis(electionEndTime);
            MaterialDatePicker datePicker=MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Set Election Ending Date")
                    .setSelection(electionEndTime)
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                electionEndTime=(Long) selection;
                calendar.setTimeInMillis(electionEndTime);
                tvElectionEndTime.setText(getDisplayTime(electionEndTime));

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

                    electionEndTime=calendar.getTimeInMillis();
                    tvElectionEndTime.setText(getDisplayTime(electionEndTime));
                });
                timePicker.show(getParentFragmentManager(),"SelectElectionEndTime");
            });
            datePicker.show(getParentFragmentManager(),"SelectElectionEndDate");
        });

        btnAddPoll.setOnClickListener(v -> {
            String address=ilAddress.getEditText().getText().toString();
            if(address.isEmpty()){
                Toast.makeText(requireActivity(),"Address Cannot be Empty",Toast.LENGTH_SHORT).show();
            }
            else if(electionStartTime<new Date().getTime()){
                Toast.makeText(requireActivity(), "Starting Time cannot be in Past", Toast.LENGTH_SHORT).show();
            }
            else if(electionStartTime>=electionEndTime){
                Toast.makeText(requireActivity(), "Starting Time should be before the Ending Time", Toast.LENGTH_SHORT).show();
            }
            else {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(HashMapConstants.UPDATE_TYPE_KEY,HashMapConstants.UPDATE_TYPE_ADD_POLL);
                hashMap.put(HashMapConstants.UPDATE_PARAM_POLL_ADDRESS_KEY,address);
                hashMap.put(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_START_TIME_KEY,electionStartTime);
                hashMap.put(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_END_TIME_KEY,electionEndTime);

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Adding New Poll");
                progressIndicatorFragment.show(getParentFragmentManager(),"NewPollProcess");
                new DatabaseUpdater(hashMap,this).execute();
            }
        });

    }

    private String  getDisplayTime(long time){
        return DateTimeUtils.getDisplayDate(time)+" "+DateTimeUtils.getDisplayTime(time);
    }

    @Override
    public void onDataUpdated(String type, boolean result, String error) {
        if(type.equals(HashMapConstants.UPDATE_TYPE_ADD_POLL)){
            progressIndicatorFragment.dismiss();
            if(result){
                Toast.makeText(requireActivity(), "Poll Added Successfully", Toast.LENGTH_SHORT).show();
                ilAddress.getEditText().setText("");

                NavDirections action=AddPollFragmentDirections.actionAddPollFragmentToAdminHomeFragment();
                Navigation.findNavController(requireActivity(),R.id.navHostAdmin).navigate(action);
            }
            else {
                Toast.makeText(requireActivity(), "Error in Adding Poll: "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}