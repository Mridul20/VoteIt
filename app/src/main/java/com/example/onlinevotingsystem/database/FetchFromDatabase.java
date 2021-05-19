package com.example.onlinevotingsystem.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.onlinevotingsystem.classes.Admin;
import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.classes.Officer;
import com.example.onlinevotingsystem.classes.Poll;
import com.example.onlinevotingsystem.classes.PollAddress;
import com.example.onlinevotingsystem.classes.User;
import com.example.onlinevotingsystem.constants.ConnectionConstants;
import com.example.onlinevotingsystem.constants.HashMapConstants;
import com.example.onlinevotingsystem.constants.TableKeys;
import com.example.onlinevotingsystem.queries.AdminQuery;
import com.example.onlinevotingsystem.queries.CandidateQuery;
import com.example.onlinevotingsystem.queries.OfficerPollNumQuery;
import com.example.onlinevotingsystem.queries.OfficerQuery;
import com.example.onlinevotingsystem.queries.PollAddressQuery;
import com.example.onlinevotingsystem.queries.PollQuery;
import com.example.onlinevotingsystem.queries.VotersQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class FetchFromDatabase extends AsyncTask<Void,Void, HashMap<String,Object>> {

    public interface FetchDbInterface{
        void onFetchCompleted(HashMap<String,Object> resultHashMap);
    }

    private final FetchDbInterface fetchDbInterface;
    private final HashMap<String,Object> inputHashMap;

    public FetchFromDatabase(FetchDbInterface fetchDbInterface, HashMap<String, Object> inputHashMap) {
        super();
        this.fetchDbInterface = fetchDbInterface;
        this.inputHashMap = inputHashMap;
    }

    @Override
    protected HashMap<String, Object> doInBackground(Void... voids) {
        HashMap<String,Object> resultHashMap=new HashMap<>();
        resultHashMap.put(HashMapConstants.FETCH_RESULT_TYPE_KEY,inputHashMap.get(HashMapConstants.FETCH_PARAM_TYPE_KEY));
        try {
            Class.forName(ConnectionConstants.JDBC_CLASS_NAME).newInstance();
            String TAG = "MySqlDataFetch";
            Log.d(TAG,"Class Loaded");

            Connection connection= DriverManager.getConnection(ConnectionConstants.SERVER_URL,ConnectionConstants.USERNAME,ConnectionConstants.PASSWORD);
            Log.d(TAG,"Connection Successful");

            Statement statement=connection.createStatement();

            String fetchType=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_TYPE_KEY);
            Log.d(TAG,"Fetch Type - "+fetchType);

            if (fetchType != null) {
                switch (fetchType){
                    case HashMapConstants.FETCH_TYPE_LOGIN_USER:{
                        String voterId,password;

                        voterId=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_LOGIN_USERNAME_KEY);
                        password=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_LOGIN_PASSWORD_KEY);

                        Log.d(TAG,"Authenticating User with Voter ID: "+voterId);

                        ResultSet resultSet=statement.executeQuery(VotersQuery.GetAuthenticateQuery(voterId,password));

                        Log.d(TAG,"Authentication Status for "+voterId+" - "+resultSet.first());

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY,resultSet.first());
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_LOGIN_ADMIN:{
                        String username, password;

                        username=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_LOGIN_USERNAME_KEY);
                        password=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_LOGIN_PASSWORD_KEY);

                        Log.d(TAG,"Authenticating Admin with Username: "+username);

                        ResultSet resultSet=statement.executeQuery(AdminQuery.GetAuthenticateQuery(username,password));

                        Log.d(TAG,"Authentication Status for "+username+" - "+resultSet.first());

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY,resultSet.first());
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_LOGIN_OFFICER:{
                        String username, password;

                        username=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_LOGIN_USERNAME_KEY);
                        password=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_LOGIN_PASSWORD_KEY);

                        Log.d(TAG,"Authenticating Officer with Username: "+username);

                        ResultSet resultSet=statement.executeQuery(OfficerQuery.GetAuthenticateQuery(username,password));

                        Log.d(TAG,"Authentication Status for "+username+" - "+resultSet.first());

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_LOGIN_IS_SUCCESSFUL_KEY,resultSet.first());
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_VERIFY_PHONE_NUM:{
                        String voterId, phoneNum, type;

                        voterId=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_VOTER_ID_KEY);
                        phoneNum=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_NUMBER_KEY);
                        type=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_VERIFY_PHONE_NUM_ROLE_KEY);

                        Log.d(TAG,"Verifying Phone Number for "+voterId+" with Number "+phoneNum);

                        String verifyQuery;
                        if(type.equals("VoterR") || type.equals("VoterF")){
                            verifyQuery=VotersQuery.GetVerifyPhoneNumQuery(voterId,phoneNum);
                        }
                        else if(type.equals("Officer")){
                            verifyQuery=OfficerQuery.GetVerifyPhoneNumQuery(voterId,phoneNum);
                        }
                        else {
                            verifyQuery=AdminQuery.GetVerifyPhoneNumQuery(voterId,phoneNum);
                        }

                        ResultSet resultSet=statement.executeQuery(verifyQuery);

                        Log.d(TAG,"Phone Verification Status for "+voterId+" - "+resultSet.first());

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_VERIFY_PHONE_NUM_KEY,resultSet.first());

                        if(resultSet.first() && type.equals("VoterR")){
                            boolean isRegistered= resultSet.getInt(TableKeys.KEY_VOTERS_IS_MOBILE_REGISTERED) == 1;
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_VERIFY_PHONE_NUM_IS_REG_KEY,isRegistered);
                        }
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_CHECK_VOTER_ID:{
                        String voterId=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_CHECK_VOTER_ID_KEY);

                        Log.d(TAG,"Checking If Voter ID Exists or not for id "+voterId);

                        ResultSet resultSet=statement.executeQuery(VotersQuery.GetCheckVoterIdQuery(voterId));

                        Log.d(TAG,"Verification Completed, Status - "+resultSet.first());

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_CHECK_VOTER_ID_EXISTS_KEY,resultSet.first());
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_EXISTING_DATA_FROM_ID:{
                        String voterId=(String) inputHashMap.get(HashMapConstants.FETCH_PARAM_EXISTING_DATA_FROM_ID_KEY);

                        Log.d(TAG,"Fetching Data of User with Voter ID "+voterId);

                        ResultSet resultSet=statement.executeQuery(VotersQuery.GetCheckVoterIdQuery(voterId));

                        Log.d(TAG,"Data Fetch Completed for "+voterId);

                        if(resultSet.first()){
                            String name=resultSet.getString(TableKeys.KEY_VOTERS_NAME);
                            String phoneNum=resultSet.getString(TableKeys.KEY_VOTERS_PHONE_NUM);
                            long dob=Long.parseLong(resultSet.getString(TableKeys.KEY_VOTERS_DOB));
                            int pollNum=resultSet.getInt(TableKeys.KEY_VOTERS_POLL_NUM);
                            boolean isMobileRegistered=resultSet.getInt(TableKeys.KEY_VOTERS_IS_MOBILE_REGISTERED)==1;
                            long regTime=Long.parseLong(resultSet.getString(TableKeys.KEY_VOTERS_REG_TIME));
                            String photoUrl=resultSet.getString(TableKeys.KEY_VOTERS_PHOTO_URL);
                            boolean hasVoted=resultSet.getInt(TableKeys.KEY_VOTERS_HAS_VOTED)==1;

                            User user=new User(voterId,name,phoneNum,pollNum,dob,isMobileRegistered,regTime,photoUrl,hasVoted);
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_EXISTING_DATA_FROM_ID_USER_KEY,user);
                        }
                        else {
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                        }

                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_ADMIN_CHECK_UNIQUE_USERNAME:{
                        String username=(String)inputHashMap.get(HashMapConstants.FETCH_PARAM_CHECK_UNIQUE_USERNAME_KEY);

                        Log.d(TAG,"Checking If Admin Username is unique : "+username);
                        ResultSet resultSet=statement.executeQuery(AdminQuery.GetCheckUserNameExistsQuery(username));
                        Log.d(TAG,"Verification Completed, Status - "+resultSet.first());

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_CHECK_UNIQUE_USERNAME_KEY,!resultSet.first());
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_USER_CHECK_UNIQUE_USERNAME:{
                        String username=(String)inputHashMap.get(HashMapConstants.FETCH_PARAM_CHECK_UNIQUE_USERNAME_KEY);

                        Log.d(TAG,"Checking If User Username is unique : "+username);
                        ResultSet resultSet=statement.executeQuery(VotersQuery.GetCheckVoterIdQuery(username));
                        Log.d(TAG,"Verification Completed, Status - "+resultSet.first());

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_CHECK_UNIQUE_USERNAME_KEY,!resultSet.first());
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_OFFICER_CHECK_UNIQUE_USERNAME:{
                        String username=(String)inputHashMap.get(HashMapConstants.FETCH_PARAM_CHECK_UNIQUE_USERNAME_KEY);

                        Log.d(TAG,"Checking If Officer Username is unique : "+username);
                        ResultSet resultSet=statement.executeQuery(OfficerQuery.GetOfficerDataQuery(username));
                        Log.d(TAG,"Verification Completed, Status - "+resultSet.first());

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_CHECK_UNIQUE_USERNAME_KEY,!resultSet.first());
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_ADMIN_DETAILS:{
                        String username=(String)inputHashMap.get(HashMapConstants.FETCH_PARAM_ADMIN_DETAILS_USERNAME_KEY);

                        Log.d(TAG,"Fetching Details of Admin "+username);
                        ResultSet resultSet=statement.executeQuery(AdminQuery.GetCheckUserNameExistsQuery(username));
                        Log.d(TAG,"Fetch Completed for "+username);

                        if(resultSet.first()){
                            String name=resultSet.getString(TableKeys.KEY_ADMIN_NAME);
                            String phoneNum=resultSet.getString(TableKeys.KEY_ADMIN_PHONE_NO);
                            String photoUrl=resultSet.getString(TableKeys.KEY_ADMIN_PHOTO_URL);

                            Admin admin=new Admin(username,name,photoUrl,phoneNum);

                            resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_ADMIN_DETAILS_KEY,admin);
                        }
                        else {
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                        }
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_OFFICER_DETAILS:{
                        String username=(String)inputHashMap.get(HashMapConstants.FETCH_PARAM_OFFICER_DETAILS_USERNAME_KEY);

                        Log.d(TAG,"Fetching Information of Officer "+username);
                        ResultSet resultSet=statement.executeQuery(OfficerQuery.GetOfficerDataQuery(username));
                        Log.d(TAG,"Information Fetch completed for "+username);

                        if(resultSet.first()){
                            String name=resultSet.getString(TableKeys.KEY_OFFICER_NAME);
                            String photoUrl=resultSet.getString(TableKeys.KEY_OFFICER_PHOTO_URL);
                            String phoneNum=resultSet.getString(TableKeys.KEY_OFFICER_PHONE_NO);

                            Log.d(TAG,"Getting Poll Number for Officer "+username);
                            ResultSet pollResult=statement.executeQuery(OfficerPollNumQuery.GetUserNamePollNoQuery(username));
                            Log.d(TAG,"Poll Number Fetch Completed for "+username);

                            if(pollResult.first()){
                                int pollNum=pollResult.getInt(TableKeys.KEY_OFFICER_POLL_NO_POLL_NO);
                                Officer officer=new Officer(username,name,photoUrl,phoneNum,pollNum);

                                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_OFFICER_DETAILS_KEY,officer);
                            }
                            else{
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                            }
                        }
                        else {
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                            resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                        }

                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_OFFICER_LIST:{

                        Log.d(TAG,"Getting list of officers");
                        ResultSet resultSet=statement.executeQuery(OfficerQuery.GetAllOfficerDataQuery());
                        Log.d(TAG,"Fetch Completed");

                        ArrayList<Officer> officerArrayList=new ArrayList<>();

                        while (resultSet.next()) {

                            String username = resultSet.getString(TableKeys.KEY_OFFICER_USERNAME);
                            String name = resultSet.getString(TableKeys.KEY_OFFICER_NAME);
                            String phoneNum = resultSet.getString(TableKeys.KEY_OFFICER_PHONE_NO);
                            String photoUrl = resultSet.getString(TableKeys.KEY_OFFICER_PHOTO_URL);

                            Statement statement1=connection.createStatement();
                            ResultSet pollResult = statement1.executeQuery(OfficerPollNumQuery.GetUserNamePollNoQuery(username));

                            if (pollResult.first()){
                                int pollNum = pollResult.getInt(TableKeys.KEY_OFFICER_POLL_NO_POLL_NO);
                                Officer officer = new Officer(username, name, photoUrl, phoneNum, pollNum);
                                officerArrayList.add(officer);
                            }
                            else {
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                                return resultHashMap;
                            }
                        }
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_OFFICERS_LIST_KEY,officerArrayList);
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_POLL_LIST:{

                        Log.d(TAG,"Fetching the Details of Each Poll");
                        ResultSet resultSet=statement.executeQuery(PollQuery.GetPollListQuery());
                        Log.d(TAG,"List Fetched Successfully");

                        ArrayList<Poll> pollArrayList=new ArrayList<>();

                        while (resultSet.next()){
                            int pollNum=resultSet.getInt(TableKeys.KEY_POLL_NUMBER);
                            int numOfCandidates=resultSet.getInt(TableKeys.KEY_POLL_NO_CANDIDATES);
                            int numOfVoters=resultSet.getInt(TableKeys.KEY_POLL_NO_VOTERS);
                            long electionStartTime=Long.parseLong(resultSet.getString(TableKeys.KEY_POLL_ELEC_START_TIME));
                            long electionEndTime=Long.parseLong(resultSet.getString(TableKeys.KEY_POLL_ELEC_END_TIME));
                            int numOfCastedVotes=resultSet.getInt(TableKeys.KEY_POLL_NO_VOTES_CASTED);

                            Poll poll=new Poll(pollNum,numOfCandidates,numOfVoters,electionStartTime,electionEndTime,numOfCastedVotes);

                            Statement statement1=connection.createStatement();

                            ResultSet addressResult=statement1.executeQuery(PollAddressQuery.GetPollDetailsQuery(pollNum));

                            if(addressResult.first())
                                poll.setAddress(addressResult.getString(TableKeys.KEY_POLL_ADDRESS_ADDRESS));
                            else {
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                                return resultHashMap;
                            }

                            ResultSet officerResult=statement1.executeQuery(OfficerPollNumQuery.GetPollDetailsQuery(pollNum));

                            if(officerResult.first())
                                poll.setOfficerUsername(officerResult.getString(TableKeys.KEY_OFFICER_POLL_NO_USERNAME));
                            else {
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                                return resultHashMap;
                            }

                            ResultSet candidateListResult=statement1.executeQuery(CandidateQuery.GetPollWiseCandidateQuery(pollNum));

                            ArrayList<Candidate> candidateArrayList=new ArrayList<>();

                            while (candidateListResult.next()){
                                String name=candidateListResult.getString(TableKeys.KEY_CANDIDATE_NAME);
                                String id=candidateListResult.getString(TableKeys.KEY_CANDIDATE_CAND_ID);
                                String phoneNum=candidateListResult.getString(TableKeys.KEY_CANDIDATE_PHONE_NO);
                                String dob=candidateListResult.getString(TableKeys.KEY_CANDIDATE_DOB);
                                String photoUrl=candidateListResult.getString(TableKeys.KEY_CANDIDATE_PHOTO_URL);
                                String symbolName=candidateListResult.getString(TableKeys.KEY_CANDIDATE_ELEC_SYMBOL_NAME);
                                String symbolPhoto=candidateListResult.getString(TableKeys.KEY_CANDIDATE_ELEC_SYMBOL_PHOTO);
                                int numOfVotes=candidateListResult.getInt(TableKeys.KEY_CANDIDATE_NO_VOTES);

                                Candidate candidate=new Candidate(id,name,Long.parseLong(dob),photoUrl,phoneNum,symbolName,symbolPhoto,pollNum,numOfVotes);
                                candidateArrayList.add(candidate);
                            }

                            poll.setCandidateList(candidateArrayList);
                            pollArrayList.add(poll);
                        }

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_POLL_LIST_KEY,pollArrayList);

                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_POLL_DETAILS: {
                        Integer pollNo = (Integer) inputHashMap.get(HashMapConstants.FETCH_PARAM_POLL_DETAILS_POLL_NUM_KEY);
                            Log.d(TAG,"Fetching the Details of Poll" + pollNo);
                            ResultSet resultSet=statement.executeQuery(PollQuery.GetPollDetailsQuery(pollNo));
                            Log.d(TAG,"Fetch Completed for Poll "+pollNo);

                            if(resultSet.first()){
                                int numOfCandidates=resultSet.getInt(TableKeys.KEY_POLL_NO_CANDIDATES);
                                int numOfVoters=resultSet.getInt(TableKeys.KEY_POLL_NO_VOTERS);
                                long electionStartTime=Long.parseLong(resultSet.getString(TableKeys.KEY_POLL_ELEC_START_TIME));
                                long electionEndTime=Long.parseLong(resultSet.getString(TableKeys.KEY_POLL_ELEC_END_TIME));
                                int numOfCastedVotes=resultSet.getInt(TableKeys.KEY_POLL_NO_VOTES_CASTED);

                                Poll poll=new Poll(pollNo,numOfCandidates,numOfVoters,electionStartTime,electionEndTime,numOfCastedVotes);

                                ResultSet addressResult=statement.executeQuery(PollAddressQuery.GetPollDetailsQuery(pollNo));

                                if(addressResult.first())
                                    poll.setAddress(addressResult.getString(TableKeys.KEY_POLL_ADDRESS_ADDRESS));
                                else {
                                    resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                                    resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                                    return resultHashMap;
                                }

                                ResultSet officerResult=statement.executeQuery(OfficerPollNumQuery.GetPollDetailsQuery(pollNo));

                                if(officerResult.first())
                                    poll.setOfficerUsername(officerResult.getString(TableKeys.KEY_OFFICER_POLL_NO_USERNAME));
                                else {
                                    resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                                    resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                                    return resultHashMap;
                                }

                                ResultSet candidateListResult=statement.executeQuery(CandidateQuery.GetPollWiseCandidateQuery(pollNo));

                                ArrayList<Candidate> candidateArrayList=new ArrayList<>();

                                while (candidateListResult.next()){
                                    String name=candidateListResult.getString(TableKeys.KEY_CANDIDATE_NAME);
                                    String id=candidateListResult.getString(TableKeys.KEY_CANDIDATE_CAND_ID);
                                    String phoneNum=candidateListResult.getString(TableKeys.KEY_CANDIDATE_PHONE_NO);
                                    String dob=candidateListResult.getString(TableKeys.KEY_CANDIDATE_DOB);
                                    String photoUrl=candidateListResult.getString(TableKeys.KEY_CANDIDATE_PHOTO_URL);
                                    String symbolName=candidateListResult.getString(TableKeys.KEY_CANDIDATE_ELEC_SYMBOL_NAME);
                                    String symbolPhoto=candidateListResult.getString(TableKeys.KEY_CANDIDATE_ELEC_SYMBOL_PHOTO);
                                    int numOfVotes=candidateListResult.getInt(TableKeys.KEY_CANDIDATE_NO_VOTES);

                                    Candidate candidate=new Candidate(id,name,Long.parseLong(dob),photoUrl,phoneNum,symbolName,symbolPhoto,pollNo,numOfVotes);
                                    candidateArrayList.add(candidate);
                                }

                                poll.setCandidateList(candidateArrayList);

                                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_POLL_DETAILS_KEY,poll);
                            }
                            else {
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY, false);
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY, "Data Does not Exists");
                                return resultHashMap;
                            }
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_POLL_RESULT:{
                        Integer pollNum=(Integer)inputHashMap.get(HashMapConstants.FETCH_PARAM_POLL_RESULT_POLL_NUM_KEY);

                        Log.d(TAG,"Getting Election Results of Poll "+pollNum);
                        String query=CandidateQuery.GetPollWiseResultQuery(pollNum);
                        Log.d(TAG,"Query: "+query);
                        ResultSet candidateListResult=statement.executeQuery(query);
                        Log.d(TAG,"Fetch Completed");

                        ArrayList<Candidate> candidateArrayList=new ArrayList<>();

                        while (candidateListResult.next()){
                            String name=candidateListResult.getString(TableKeys.KEY_CANDIDATE_NAME);
                            String id=candidateListResult.getString(TableKeys.KEY_CANDIDATE_CAND_ID);
                            String phoneNum=candidateListResult.getString(TableKeys.KEY_CANDIDATE_PHONE_NO);
                            String dob=candidateListResult.getString(TableKeys.KEY_CANDIDATE_DOB);
                            String photoUrl=candidateListResult.getString(TableKeys.KEY_CANDIDATE_PHOTO_URL);
                            String symbolName=candidateListResult.getString(TableKeys.KEY_CANDIDATE_ELEC_SYMBOL_NAME);
                            String symbolPhoto=candidateListResult.getString(TableKeys.KEY_CANDIDATE_ELEC_SYMBOL_PHOTO);
                            int numOfVotes=candidateListResult.getInt(TableKeys.KEY_CANDIDATE_NO_VOTES);

                            Candidate candidate=new Candidate(id,name,Long.parseLong(dob),photoUrl,phoneNum,symbolName,symbolPhoto,pollNum,numOfVotes);
                            candidateArrayList.add(candidate);
                        }

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_POLL_RESULT_CANDIDATE_LIST_KEY,candidateArrayList);
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_UNASSIGNED_POLLS:{
                        Log.d(TAG,"Fetching the List of Unassigned Polls");
                        ResultSet resultSet= statement.executeQuery(OfficerPollNumQuery.GetUnassignedPolls());
                        Log.d(TAG,"Fetch Completed");

                        ArrayList<PollAddress> pollList=new ArrayList<>();

                        while (resultSet.next()){
                            int PollNum=resultSet.getInt(TableKeys.KEY_OFFICER_POLL_NO_POLL_NO);

                            Statement statement1=connection.createStatement();
                            ResultSet resultSet1=statement1.executeQuery(PollAddressQuery.GetPollDetailsQuery(PollNum));

                            if(resultSet1.first()){
                                String address=resultSet1.getString(TableKeys.KEY_POLL_ADDRESS_ADDRESS);
                                pollList.add(new PollAddress(PollNum,address));
                            }
                            else {
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                                resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Data Does not Exists");
                                return resultHashMap;
                            }
                        }

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_UNASSIGNED_POLLS_KEY,pollList);
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_POLLS_ADDRESS:{
                        Log.d(TAG,"Fetching the Address of All the Polls");
                        ResultSet resultSet= statement.executeQuery(PollAddressQuery.GetAllPollAddress());
                        Log.d(TAG,"Fetch Completed");

                        ArrayList<PollAddress> pollList=new ArrayList<>();

                        while (resultSet.next()){
                            int PollNum=resultSet.getInt(TableKeys.KEY_POLL_ADDRESS_NUMBER);
                            String PollAddress=resultSet.getString(TableKeys.KEY_POLL_ADDRESS_ADDRESS);

                            pollList.add(new PollAddress(PollNum,PollAddress));
                        }

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_POLLS_ADDRESS_KEY,pollList);
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_POLL_VOTERS_LIST:{
                        Integer pollNum=(Integer)inputHashMap.get(HashMapConstants.FETCH_PARAM_POLL_VOTERS_LIST_POLL_NUM_KEY);

                        Log.d(TAG,"Fetching List of User for Poll Number "+pollNum);
                        ResultSet resultSet= statement.executeQuery(VotersQuery.GetVoterListAccToPollNoQuery(pollNum));
                        Log.d(TAG,"Fetch Completed for Poll "+pollNum);

                        ArrayList<User> userList=new ArrayList<>();

                        while (resultSet.next()){
                            String voterId=resultSet.getString(TableKeys.KEY_VOTERS_ID);
                            String name=resultSet.getString(TableKeys.KEY_VOTERS_NAME);
                            boolean isMobileReg=resultSet.getInt(TableKeys.KEY_VOTERS_IS_MOBILE_REGISTERED)==1;
                            String photoUrl=resultSet.getString(TableKeys.KEY_VOTERS_PHOTO_URL);
                            String phoneNum=resultSet.getString(TableKeys.KEY_VOTERS_PHONE_NUM);
                            long dob=Long.parseLong(resultSet.getString(TableKeys.KEY_VOTERS_DOB));
                            boolean hasVoted=resultSet.getInt(TableKeys.KEY_VOTERS_HAS_VOTED)==1;

                            userList.add(new User(voterId,name,pollNum,isMobileReg,dob,photoUrl,hasVoted,phoneNum));
                        }

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_POLL_VOTERS_LIST_KEY,userList);
                        break;
                    }
                    case HashMapConstants.FETCH_TYPE_ALL_VOTERS_LIST:{
                        Log.d(TAG,"Fetching List of All Users=");
                        ResultSet resultSet= statement.executeQuery(VotersQuery.GetAllVotersQuery());
                        Log.d(TAG,"Fetch of All VOters Completed");

                        ArrayList<User> userList=new ArrayList<>();

                        while (resultSet.next()){
                            String voterId=resultSet.getString(TableKeys.KEY_VOTERS_ID);
                            String name=resultSet.getString(TableKeys.KEY_VOTERS_NAME);
                            int pollNum=resultSet.getInt(TableKeys.KEY_VOTERS_POLL_NUM);
                            boolean isMobileReg=resultSet.getInt(TableKeys.KEY_VOTERS_IS_MOBILE_REGISTERED)==1;
                            String photoUrl=resultSet.getString(TableKeys.KEY_VOTERS_PHOTO_URL);
                            String phoneNum=resultSet.getString(TableKeys.KEY_VOTERS_PHONE_NUM);
                            long dob=Long.parseLong(resultSet.getString(TableKeys.KEY_VOTERS_DOB));
                            boolean hasVoted=resultSet.getInt(TableKeys.KEY_VOTERS_HAS_VOTED)==1;

                            userList.add(new User(voterId,name,pollNum,isMobileReg,dob,photoUrl,hasVoted,phoneNum));
                        }

                        resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,true);
                        resultHashMap.put(HashMapConstants.FETCH_RESULT_ALL_VOTERS_LIST_KEY,userList);
                        break;
                    }
                }
            }
            else {
                resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
                resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,"Invalid Input");
                return resultHashMap;
            }

            return resultHashMap;

        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resultHashMap.put(HashMapConstants.FETCH_RESULT_SUCCESS_KEY,false);
            resultHashMap.put(HashMapConstants.FETCH_RESULT_ERROR_KEY,e.getLocalizedMessage());
            return resultHashMap;
        }
    }

    @Override
    protected void onPostExecute(HashMap<String, Object> hashMap) {
        super.onPostExecute(hashMap);
        fetchDbInterface.onFetchCompleted(hashMap);
    }
}