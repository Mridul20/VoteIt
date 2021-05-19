package com.example.onlinevotingsystem.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.classes.User;
import com.example.onlinevotingsystem.utils.DateTimeUtils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OfficerUserListAdapter extends RecyclerView.Adapter<OfficerUserListAdapter.ViewHolder> {

    ArrayList<User> UserList;

    public OfficerUserListAdapter(ArrayList<User> userList) {
        UserList=userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvVoterName, tvVoterID, tvVoterDob, tvVoterPoll, tvHasVoted, tvIsMobileReg;
        ImageView imgUser;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvVoterName=itemView.findViewById(R.id.tvVoterItemName);
            tvVoterID=itemView.findViewById(R.id.tvVoterItemId);
            tvVoterDob=itemView.findViewById(R.id.tvVoterItemDOB);
            tvVoterPoll=itemView.findViewById(R.id.tvVoterItemPollNum);
            tvHasVoted=itemView.findViewById(R.id.tvVoterItemHasVoted);
            tvIsMobileReg=itemView.findViewById(R.id.tvVoterItemIsMobileReg);
            imgUser=itemView.findViewById(R.id.imgVoterItem);

        }
    }

    @NonNull
    @NotNull
    @Override
    public OfficerUserListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.voter_list_card_design,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OfficerUserListAdapter.ViewHolder holder, int position) {
        User user=UserList.get(position);
        holder.tvVoterName.setText(user.getName());
        holder.tvVoterID.setText(user.getVoterID());
        holder.tvVoterDob.setText(DateTimeUtils.getDisplayDate(user.getDateOfBirth()));
        holder.tvVoterPoll.setText(String.format("Poll Number: %d",user.getPollNumber()));
        holder.tvHasVoted.setText("Voted: "+user.isHasVoted());
        holder.tvIsMobileReg.setText("Is Mobile Registered: "+user.isMobileRegistered());

        if(user.getPhotoURL()!=null)
            Picasso.get().load(Uri.parse(user.getPhotoURL())).into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }
}
