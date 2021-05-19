package com.example.onlinevotingsystem.classes;

public class Candidate {

    private String ID;
    private String Name;
    private long DateOfBirth;
    private String PhotoURL;
    private String PhoneNumber;
    private String ElectionSymbolName;
    private String ElectionSymbolPhotoURL;
    private int PollNumber;
    private int NumberOfVotesReceived;

    public Candidate(String ID, String name, long dateOfBirth, String photoURL, String phoneNumber, String electionSymbolName, String electionSymbolPhotoURL, int pollNumber, int numberOfVotesReceived) {
        this.ID = ID;
        Name = name;
        DateOfBirth = dateOfBirth;
        PhotoURL = photoURL;
        PhoneNumber = phoneNumber;
        ElectionSymbolName = electionSymbolName;
        ElectionSymbolPhotoURL = electionSymbolPhotoURL;
        PollNumber = pollNumber;
        NumberOfVotesReceived = numberOfVotesReceived;
    }

    public Candidate(String name, long dateOfBirth, String phoneNumber, String electionSymbolName, int pollNumber) {
        Name = name;
        DateOfBirth = dateOfBirth;
        PhoneNumber = phoneNumber;
        ElectionSymbolName = electionSymbolName;
        PollNumber = pollNumber;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getElectionSymbolName() {
        return ElectionSymbolName;
    }

    public void setElectionSymbolName(String electionSymbolName) {
        ElectionSymbolName = electionSymbolName;
    }

    public String getElectionSymbolPhotoURL() {
        return ElectionSymbolPhotoURL;
    }

    public void setElectionSymbolPhotoURL(String electionSymbolPhotoURL) {
        ElectionSymbolPhotoURL = electionSymbolPhotoURL;
    }

    public int getPollNumber() {
        return PollNumber;
    }

    public void setPollNumber(int pollNumber) {
        PollNumber = pollNumber;
    }

    public int getNumberOfVotesReceived() {
        return NumberOfVotesReceived;
    }

    public void setNumberOfVotesReceived(int numberOfVotesReceived) {
        NumberOfVotesReceived = numberOfVotesReceived;
    }
}
