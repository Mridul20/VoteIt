package com.example.onlinevotingsystem.classes;

public class User {

    private String VoterID;
    private String Name;
    private String PhoneNumber;
    private int PollNumber;
    private long DateOfBirth;
    private boolean IsMobileRegistered;
    private long TimeOfRegistration;
    private String PhotoURL;
    private boolean HasVoted;

    public User(String voterID, String name, String phoneNumber, int pollNumber, long dateOfBirth, boolean isMobileRegistered, long timeOfRegistration, String photoURL, boolean hasVoted) {
        VoterID = voterID;
        Name = name;
        PhoneNumber = phoneNumber;
        PollNumber = pollNumber;
        DateOfBirth = dateOfBirth;
        IsMobileRegistered = isMobileRegistered;
        TimeOfRegistration = timeOfRegistration;
        PhotoURL = photoURL;
        HasVoted = hasVoted;
    }

    public User(String name, String phoneNumber, int pollNumber, long dateOfBirth) {
        Name = name;
        PhoneNumber = phoneNumber;
        PollNumber = pollNumber;
        DateOfBirth = dateOfBirth;
    }

    public User(String voterID, String name, int pollNumber, boolean isMobileRegistered, String photoURL, boolean hasVoted) {
        VoterID = voterID;
        Name = name;
        PollNumber = pollNumber;
        IsMobileRegistered = isMobileRegistered;
        PhotoURL = photoURL;
        HasVoted = hasVoted;
    }

    public User(String voterID, String name, int pollNumber, boolean isMobileRegistered,long dob, String photoURL, boolean hasVoted, String phoneNum) {
        VoterID = voterID;
        Name = name;
        PollNumber = pollNumber;
        IsMobileRegistered = isMobileRegistered;
        DateOfBirth=dob;
        PhotoURL = photoURL;
        HasVoted = hasVoted;
        PhoneNumber=phoneNum;
    }

    public String getVoterID() {
        return VoterID;
    }

    public void setVoterID(String voterID) {
        VoterID = voterID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getPollNumber() {
        return PollNumber;
    }

    public void setPollNumber(int pollNumber) {
        PollNumber = pollNumber;
    }

    public long getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public boolean isMobileRegistered() {
        return IsMobileRegistered;
    }

    public void setMobileRegistered(boolean mobileRegistered) {
        IsMobileRegistered = mobileRegistered;
    }

    public long getTimeOfRegistration() {
        return TimeOfRegistration;
    }

    public void setTimeOfRegistration(long timeOfRegistration) {
        TimeOfRegistration = timeOfRegistration;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public boolean isHasVoted() {
        return HasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        HasVoted = hasVoted;
    }
}
