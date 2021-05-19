package com.example.onlinevotingsystem.classes;

public class PollAddress {

    private int PollNumber;
    private String PollAddress;

    public PollAddress(int pollNumber, String pollAddress) {
        PollNumber = pollNumber;
        PollAddress = pollAddress;
    }

    public int getPollNumber() {
        return PollNumber;
    }

    public void setPollNumber(int pollNumber) {
        PollNumber = pollNumber;
    }

    public String getPollAddress() {
        return PollAddress;
    }

    public void setPollAddress(String pollAddress) {
        PollAddress = pollAddress;
    }
}
