package com.example.onlinevotingsystem.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.classes.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PublicVoterListAdapter extends RecyclerView.Adapter<PublicVoterListAdapter.ViewHolder> {

    ArrayList<User> userList;

    public PublicVoterListAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPollNum, tvName, tvId, tvPhoneNum;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvPollNum=itemView.findViewById(R.id.tvPublicUserListPollNum);
            tvName=itemView.findViewById(R.id.tvPublicUserListName);
            tvId=itemView.findViewById(R.id.tvPublicUserListId);
            tvPhoneNum=itemView.findViewById(R.id.tvPublicVoterListPhoneNumber);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.public_voter_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        User user=userList.get(position);
        holder.tvId.setText(user.getVoterID());
        holder.tvName.setText(user.getName());
        holder.tvPollNum.setText(String.format("%d",user.getPollNumber()));

        String phoneNum=user.getPhoneNumber();
        String displayNum=phoneNum.substring(0,4)+"XXXXXXX"+phoneNum.substring(11);
        holder.tvPhoneNum.setText(displayNum);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
