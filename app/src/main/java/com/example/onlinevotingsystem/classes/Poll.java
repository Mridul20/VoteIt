package com.example.onlinevotingsystem.classes;

import java.util.ArrayList;

public class Poll {

    private int PollNumber;
    private String Address;
    private String OfficerUsername;
    private int NumberOfCandidates;
    private int NumberOfVoters;
    private long ElectionStartTime;
    private long ElectionEndTime;
    private int NumberOfVotesCasted;

    private ArrayList<Candidate> candidateList;

    public Poll(int pollNumber, int numberOfCandidates, int numberOfVoters, long electionStartTime, long electionEndTime, int numberOfVotesCasted) {
        PollNumber = pollNumber;
        NumberOfCandidates = numberOfCandidates;
        NumberOfVoters = numberOfVoters;
        ElectionStartTime = electionStartTime;
        ElectionEndTime = electionEndTime;
        NumberOfVotesCasted = numberOfVotesCasted;
    }

    public Poll(int pollNumber, String address, String officerUsername, int numberOfCandidates, int numberOfVoters, long electionStartTime, long electionEndTime, int numberOfVotesCasted) {
        PollNumber = pollNumber;
        Address = address;
        OfficerUsername = officerUsername;
        NumberOfCandidates = numberOfCandidates;
        NumberOfVoters = numberOfVoters;
        ElectionStartTime = electionStartTime;
        ElectionEndTime = electionEndTime;
        NumberOfVotesCasted = numberOfVotesCasted;
    }

    public int getPollNumber() {
        return PollNumber;
    }

    public void setPollNumber(int pollNumber) {
        PollNumber = pollNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOfficerUsername() {
        return OfficerUsername;
    }

    public void setOfficerUsername(String officerUsername) {
        OfficerUsername = officerUsername;
    }

    public int getNumberOfCandidates() {
        return NumberOfCandidates;
    }

    public void setNumberOfCandidates(int numberOfCandidates) {
        NumberOfCandidates = numberOfCandidates;
    }

    public int getNumberOfVoters() {
        return NumberOfVoters;
    }

    public void setNumberOfVoters(int numberOfVoters) {
        NumberOfVoters = numberOfVoters;
    }

    public long getElectionStartTime() {
        return ElectionStartTime;
    }

    public void setElectionStartTime(long electionStartTime) {
        ElectionStartTime = electionStartTime;
    }

    public long getElectionEndTime() {
        return ElectionEndTime;
    }

    public void setElectionEndTime(long electionEndTime) {
        ElectionEndTime = electionEndTime;
    }

    public int getNumberOfVotesCasted() {
        return NumberOfVotesCasted;
    }

    public void setNumberOfVotesCasted(int numberOfVotesCasted) {
        NumberOfVotesCasted = numberOfVotesCasted;
    }

    public ArrayList<Candidate> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(ArrayList<Candidate> candidateList) {
        this.candidateList = candidateList;
    }
}
