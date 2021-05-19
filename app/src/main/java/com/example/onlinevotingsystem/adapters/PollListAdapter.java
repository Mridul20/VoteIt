package com.example.onlinevotingsystem.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.fragments.officer.OfficerHomeFragmentDirections;
import com.example.onlinevotingsystem.fragments.shared.PollListFragmentDirections;
import com.example.onlinevotingsystem.fragments.user.UserPollDetailsFragmentDirections;
import com.example.onlinevotingsystem.utils.DateTimeUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class PollListAdapter extends RecyclerView.Adapter<PollListAdapter.ViewHolder> {

    ArrayList<Poll> PollList;
    NavController navController;
    String type;

    public PollListAdapter(ArrayList<Poll> pollList, NavController navController) {
        PollList = pollList;
        this.navController = navController;
        type="AllPolls";
    }

    public PollListAdapter(ArrayList<Poll> pollList, NavController navController, String type) {
        PollList = pollList;
        this.navController = navController;
        this.type = type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPollNum, tvOfficer, tvAddress, tvNumOfCandidates, tvElectionStartTime, tvElectionEndTime, tvNumOfVoters, tvNumOfCastedVotes;
        Button btnViewCandidates, btnViewResult;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvPollNum=itemView.findViewById(R.id.tvPollItemNumber);
            tvOfficer=itemView.findViewById(R.id.tvPollitemOfficer);
            tvAddress=itemView.findViewById(R.id.tvPollitemAddress);
            tvNumOfCandidates=itemView.findViewById(R.id.tvPollitemCandidateNo);
            tvElectionStartTime=itemView.findViewById(R.id.tvPollitemStartTime);
            tvElectionEndTime=itemView.findViewById(R.id.tvPollItemEndTime);
            tvNumOfVoters=itemView.findViewById(R.id.tvPollListNumOfVoters);
            tvNumOfCastedVotes=itemView.findViewById(R.id.tvPollListVotesCasted);
            btnViewCandidates=itemView.findViewById(R.id.btnPollListViewCandidates);
            btnViewResult=itemView.findViewById(R.id.btnPollListViewResult);
        }
    }

    @NonNull
    @NotNull
    @Override
    public PollListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PollListAdapter.ViewHolder holder, int position) {
        Poll poll= PollList.get(position);
        holder.tvPollNum.setText(String.format("%d",poll.getPollNumber()));

        if(poll.getOfficerUsername()!=null)
            holder.tvOfficer.setText(poll.getOfficerUsername());
        else
            holder.tvOfficer.setText("No Officer Assigned");

        holder.tvAddress.setText(poll.getAddress());
        holder.tvNumOfCandidates.setText(String.format("%d",poll.getNumberOfCandidates()));
        holder.tvElectionStartTime.setText(getDisplayTime(poll.getElectionStartTime()));
        holder.tvElectionEndTime.setText(getDisplayTime(poll.getElectionEndTime()));
        holder.tvNumOfVoters.setText(String.format("%d",poll.getNumberOfVoters()));
        holder.tvNumOfCastedVotes.setText(String.format("%d",poll.getNumberOfVotesCasted()));

        holder.btnViewResult.setEnabled(poll.getElectionEndTime() <= new Date().getTime());
        holder.btnViewCandidates.setEnabled(poll.getNumberOfCandidates() != 0);

        if(type.equals("SinglePollUser"))
            holder.btnViewResult.setVisibility(View.GONE);

        holder.btnViewResult.setOnClickListener(v -> {
            Bundle args=new Bundle();
            args.putInt("PollNum",poll.getPollNumber());
            if(type.equals("SinglePollOfficer"))
                navController.navigate(R.id.action_officerHomeFragment_to_electionResultFragment3,args);
            else if(type.equals("AllPolls"))
                navController.navigate(R.id.electionResultFragment,args);
        });

        holder.btnViewCandidates.setOnClickListener(v -> {
            NavDirections action;
            if(type.equals("AllPolls"))
                action= PollListFragmentDirections.actionPollListFragmentToCandidateListFragment(position);
            else if(type.equals("SinglePollOfficer"))
                action= OfficerHomeFragmentDirections.actionOfficerHomeFragmentToCandidateListFragment(0);
            else
                action= UserPollDetailsFragmentDirections.actionUserPollDetailsFragmentToCandidateListFragment2(0);
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return PollList.size();
    }

    private String  getDisplayTime(long time){
        return DateTimeUtils.getDisplayDate(time)+" "+DateTimeUtils.getDisplayTime(time);
    }
}
