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
import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.utils.DateTimeUtils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OfficerCandidateListAdapter extends RecyclerView.Adapter<OfficerCandidateListAdapter.ViewHolder> {

    ArrayList<Candidate> CandidateList;

    public OfficerCandidateListAdapter(ArrayList<Candidate> candidateList){
        CandidateList=candidateList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvId, tvDob, tvPhoneNum, tvSymbolName;
        ImageView imgCandidate;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvCandidateItemName);
            tvId=itemView.findViewById(R.id.tvCandidateitemNumber);
            tvDob=itemView.findViewById(R.id.tvCandidateItemDOB);
            tvPhoneNum=itemView.findViewById(R.id.tvCandidateItemPhoneNum);
            tvSymbolName=itemView.findViewById(R.id.tvCandidateItemSymbolName);
            imgCandidate=itemView.findViewById(R.id.imgCandidateItem);

        }
    }

    @NonNull
    @NotNull
    @Override
    public OfficerCandidateListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OfficerCandidateListAdapter.ViewHolder holder, int position) {
        Candidate candidate=CandidateList.get(position);
        holder.tvId.setText(candidate.getID());
        holder.tvName.setText(candidate.getName());
        holder.tvDob.setText(DateTimeUtils.getDisplayDate(candidate.getDateOfBirth()));
        holder.tvPhoneNum.setText(candidate.getPhoneNumber());
        holder.tvSymbolName.setText(candidate.getElectionSymbolName());

        if(candidate.getPhotoURL()!=null)
            Picasso.get().load(Uri.parse(candidate.getPhotoURL())).into(holder.imgCandidate);
    }

    @Override
    public int getItemCount() {
        return CandidateList.size();
    }
}
