package com.example.onlinevotingsystem.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.utils.DateTimeUtils;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ViewHolder> {

    ArrayList<Candidate> candidateList;

    public ResultListAdapter(ArrayList<Candidate> candidateList) {
        this.candidateList = candidateList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView mcvResult;
        TextView tvName, tvId, tvPollNum, tvPhoneNum, tvDob, tvSymbol, tvVotes;
        ImageView imgCandidate, imgSymbol;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mcvResult=itemView.findViewById(R.id.mcvResultList);

            tvName=itemView.findViewById(R.id.tvResultItemName);
            tvId=itemView.findViewById(R.id.tvResultItemId);
            tvPollNum=itemView.findViewById(R.id.tvResultItemPollNum);
            tvPhoneNum=itemView.findViewById(R.id.tvResultItemPhoneNum);
            tvDob=itemView.findViewById(R.id.tvResultItemDOB);
            tvSymbol=itemView.findViewById(R.id.tvResultItemSymbolName);
            tvVotes=itemView.findViewById(R.id.tvResultListNumOfVotes);

            imgCandidate=itemView.findViewById(R.id.imgResultList);
            imgSymbol=itemView.findViewById(R.id.imgResultItemSymbol);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Candidate candidate=candidateList.get(position);
        holder.tvName.setText(candidate.getName());
        holder.tvId.setText(candidate.getID());
        holder.tvPollNum.setText(String.format("%d",candidate.getPollNumber()));
        holder.tvPhoneNum.setText(candidate.getPhoneNumber());
        holder.tvDob.setText(DateTimeUtils.getDisplayDate(candidate.getDateOfBirth()));
        holder.tvSymbol.setText(candidate.getElectionSymbolName());
        holder.tvVotes.setText(String.format("%d",candidate.getNumberOfVotesReceived()));

        if(candidate.getPhotoURL()!=null)
            Picasso.get().load(Uri.parse(candidate.getPhotoURL())).into(holder.imgCandidate);

        if(candidate.getElectionSymbolPhotoURL()!=null)
            Picasso.get().load(Uri.parse(candidate.getElectionSymbolPhotoURL())).into(holder.imgSymbol);

        if(position==0)
            holder.mcvResult.setCardBackgroundColor(ContextCompat.getColor(holder.mcvResult.getContext(), R.color.resultGold));
        else if(position==1)
            holder.mcvResult.setCardBackgroundColor(ContextCompat.getColor(holder.mcvResult.getContext(), R.color.resultSilver));
        else if(position==2)
            holder.mcvResult.setCardBackgroundColor(ContextCompat.getColor(holder.mcvResult.getContext(), R.color.resultBronze));
        else
            holder.mcvResult.setCardBackgroundColor(ContextCompat.getColor(holder.mcvResult.getContext(), R.color.resultRest));
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

}
