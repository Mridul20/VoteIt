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
import com.example.onlinevotingsystem.classes.Officer;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OfficerListAdapter extends RecyclerView.Adapter<OfficerListAdapter.ViewHolder> {

    ArrayList<Officer> OfficerList;

    public OfficerListAdapter(ArrayList<Officer> officerList) {
        OfficerList = officerList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvUsername, tvPollNum, tvPhoneNum;
        ImageView imgOfficer;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvOfficerListItemName);
            tvUsername=itemView.findViewById(R.id.tvOfficerListItemUsername);
            tvPhoneNum=itemView.findViewById(R.id.tvOfficerListItemPhoneNum);
            tvPollNum=itemView.findViewById(R.id.tvOfficerListItemPollNum);
            imgOfficer=itemView.findViewById(R.id.imgOfficerListItem);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.officer_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Officer officer=OfficerList.get(position);
        holder.tvName.setText(officer.getName());
        holder.tvUsername.setText(officer.getUsername());
        holder.tvPhoneNum.setText(officer.getPhoneNum());
        holder.tvPollNum.setText(String.format("%d",officer.getPollNumber()));

        if(officer.getPhotoURL()!=null)
            Picasso.get().load(Uri.parse(officer.getPhotoURL())).into(holder.imgOfficer);
    }

    @Override
    public int getItemCount() {
        return OfficerList.size();
    }

}
