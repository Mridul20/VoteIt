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

public class CandidateListAdapter extends RecyclerView.Adapter<CandidateListAdapter.ViewHolder> {

    ArrayList<Candidate> candidateList;

    public CandidateListAdapter(ArrayList<Candidate> candidateList) {
        this.candidateList = candidateList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvId, tvPollNum, tvPhoneNum, tvDob, tvSymbol;
        ImageView imgCandidate, imgSymbol;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvCandidateItemName);
            tvId=itemView.findViewById(R.id.tvCandidateItemId);
            tvPollNum=itemView.findViewById(R.id.tvCandidateItemPollNum);
            tvPhoneNum=itemView.findViewById(R.id.tvCandidateItemPhoneNum);
            tvDob=itemView.findViewById(R.id.tvCandidateItemDOB);
            tvSymbol=itemView.findViewById(R.id.tvCandidateItemSymbolName);

            imgCandidate=itemView.findViewById(R.id.imgCandidateList);
            imgSymbol=itemView.findViewById(R.id.imgCandidateItemSymbol);
        }
    }

    @NonNull
    @NotNull
    @Override
    public CandidateListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_candidate_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CandidateListAdapter.ViewHolder holder, int position) {
        Candidate candidate=candidateList.get(position);
        holder.tvName.setText(candidate.getName());
        holder.tvId.setText(candidate.getID());
        holder.tvPollNum.setText(String.format("%d",candidate.getPollNumber()));
        holder.tvPhoneNum.setText(candidate.getPhoneNumber());
        holder.tvDob.setText(DateTimeUtils.getDisplayDate(candidate.getDateOfBirth()));
        holder.tvSymbol.setText(candidate.getElectionSymbolName());

        if(candidate.getPhotoURL()!=null)
            Picasso.get().load(Uri.parse(candidate.getPhotoURL())).into(holder.imgCandidate);

        if(candidate.getElectionSymbolPhotoURL()!=null)
            Picasso.get().load(Uri.parse(candidate.getElectionSymbolPhotoURL())).into(holder.imgSymbol);
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }
}
