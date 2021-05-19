package com.example.onlinevotingsystem.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.onlinevotingsystem.classes.Admin;
import com.example.onlinevotingsystem.classes.Candidate;
import com.example.onlinevotingsystem.classes.Officer;
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
import java.util.Date;
import java.util.HashMap;

public class DatabaseUpdater extends AsyncTask<Void,Void,Boolean> {

    public interface DatabaseUpdateInterface{
        void onDataUpdated(String type, boolean result, String error);
    }

    private final HashMap<String,Object> inputHashMap;
    private final DatabaseUpdateInterface updateInterface;
    private String error, UpdateType;

    public DatabaseUpdater(HashMap<String, Object> inputHashMap, DatabaseUpdateInterface updateInterface) {
        super();
        this.inputHashMap = inputHashMap;
        this.updateInterface = updateInterface;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String TAG = "DatabaseUpdater";
        UpdateType=(String) inputHashMap.get(HashMapConstants.UPDATE_TYPE_KEY);
        try {
            Class.forName(ConnectionConstants.JDBC_CLASS_NAME).newInstance();
            Log.d(TAG,"Class Loaded");

            Connection connection=DriverManager.getConnection(ConnectionConstants.SERVER_URL,ConnectionConstants.USERNAME,ConnectionConstants.PASSWORD);
            Log.d(TAG,"Connection Successful");

            Statement statement=connection.createStatement();

            switch (UpdateType){
                //Implemented
                case HashMapConstants.UPDATE_TYPE_ADD_ADMIN:{
                    Admin inputAdmin=(Admin) inputHashMap.get(HashMapConstants.UPDATE_PARAM_ADMIN_KEY);
                    if (inputAdmin != null) {
                        String username=inputAdmin.getUsername();
                        String name=inputAdmin.getName();
                        String phoneNum=inputAdmin.getPhoneNum();
                        String password=phoneNum.substring(phoneNum.length()-4)+name.substring(0,4);

                        Log.d(TAG,"Adding New Admin");
                        ResultSet resultSet= statement.executeQuery(AdminQuery.GetCheckUserNameExistsQuery(username));
                        if(!resultSet.first()){
                            statement.execute(AdminQuery.GetAddAdminQuery(username,password,name,phoneNum));
                            Log.d(TAG,"Admin Added Successfully!");
                        }
                        else {
                            error="Username Already Exists";
                            return false;
                        }

                    }
                    else {
                        error="Invalid Admin Input";
                        return false;
                    }
                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_ADMIN_PASSWORD:{
                    String username=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_ADMIN_USERNAME_KEY);
                    String password=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_ADMIN_PASSWORD_KEY);

                    Log.d(TAG,"Updating Password for "+username);
                    statement.execute(AdminQuery.GetUpdatePasswordQuery(username,password));
                    Log.d(TAG,"Password Updated Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_ADMIN_PHOTO:{
                    String username=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_ADMIN_PHOTO_USERNAME_KEY);
                    String photo=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_ADMIN_PHOTO_KEY);

                    Log.d(TAG,"Updating Photo for "+username);
                    statement.execute(AdminQuery.GetUpdatePhotoUrlQuery(username,photo));
                    Log.d(TAG,"Photo Updated Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_ADD_OFFICER:{
                    Officer officer=(Officer)inputHashMap.get(HashMapConstants.UPDATE_PARAM_OFFICER_KEY);
                    if(officer != null){
                        String username=officer.getUsername();
                        String name=officer.getName();
                        String phoneNum=officer.getPhoneNum();
                        String password=phoneNum.substring(phoneNum.length()-4)+name.substring(0,4);
                        int pollNum=officer.getPollNumber();

                        Log.d(TAG,"Adding New Officer");
                        statement.execute(OfficerQuery.GetInsertQuery(username,password,name,phoneNum));
                        statement.execute(OfficerPollNumQuery.GetAddOfficerToPollQuery(username,pollNum));
                        Log.d(TAG,"New Officer Added Successfully");
                    }
                    else {
                        error="Invalid Officer Input";
                        return false;
                    }
                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_OFFICER_PASSWORD:{
                    String username=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_OFFICER_USERNAME_KEY);
                    String password=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_OFFICER_PASSWORD_KEY);

                    Log.d(TAG,"Updating Password for "+username);
                    statement.execute(OfficerQuery.GetUpdatePasswordQuery(username,password));
                    Log.d(TAG,"Password Updated Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_OFFICER_PHOTO:{
                    String username=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_OFFICER_PHOTO_USERNAME_KEY);
                    String photo=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_OFFICER_PHOTO_KEY);

                    Log.d(TAG,"Updating Photo for "+username);
                    statement.execute(OfficerQuery.GetUpdatePhotoUrlQuery(username,photo));
                    Log.d(TAG,"Photo Updated Successfully");

                    break;
                }

                case HashMapConstants.UPDATE_TYPE_OFFICER_POLL:{
                    String username=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_OFFICER_POLL_USERNAME_KEY);
                    Integer pollNum=(Integer) inputHashMap.get(HashMapConstants.UPDATE_PARAM_OFFICER_POLL_KEY);
                    if (pollNum!=null) {
                        Log.d(TAG,"Updating Poll Number of Officer "+username);
                        statement.execute(OfficerPollNumQuery.GetUpdatePollNoQuery(username,pollNum));
                        Log.d(TAG,"Poll Number Updated Successfully");
                    }
                    else {
                        error="Invalid Input";
                        return false;
                    }
                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_ADD_POLL:{
                    String pollAddress=(String)inputHashMap.get(HashMapConstants.UPDATE_PARAM_POLL_ADDRESS_KEY);
                    Long startTime=(Long)inputHashMap.get(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_START_TIME_KEY);
                    Long endTime=(Long)inputHashMap.get(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_END_TIME_KEY);

                    Log.d(TAG,"Adding New Poll");

                    ResultSet resultSet=statement.executeQuery(PollQuery.GetPollListQuery());
                    int pollNum=1;
                    while (resultSet.next())
                        pollNum++;

                    if(endTime!=null && startTime!=null){
                        statement.execute(PollQuery.GetInsertQuery(pollNum,endTime,startTime));
                    }
                    else {
                        error="Invalid Input";
                        return false;
                    }

                    statement.execute(PollAddressQuery.GetInsertQuery(pollNum,pollAddress));
                    statement.execute(OfficerPollNumQuery.GetInsertQuery(pollNum));

                    Log.d(TAG,"New Poll Added Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_POLL_ELECTION_TIME:{
                    Integer pollNum=(Integer)inputHashMap.get(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_TIME_POLL_NUM_KEY);
                    Long startTime=(Long)inputHashMap.get(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_TIME_START_KEY);
                    Long endTime=(Long)inputHashMap.get(HashMapConstants.UPDATE_PARAM_POLL_ELECTION_TIME_END_KEY);
                    if(pollNum!=null && endTime!=null && startTime!=null){
                        Log.d(TAG,"Updating Election Time of Poll "+pollNum);
                        String query=PollQuery.GetUpdateElecTimeQuery(pollNum,endTime,startTime);
                        statement.execute(query);
                        Log.d(TAG,"Election Time updated Successfully");
                    }
                    else {
                        error="Invalid Input";
                        return false;
                    }
                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_ADD_USER:{
                    User user=(User)inputHashMap.get(HashMapConstants.UPDATE_PARAM_ADD_USER_KEY);
                    if(user!=null){
                        int pollNum=user.getPollNumber();

                        Log.d(TAG,"Adding New User");

                        ResultSet resultSet=statement.executeQuery(VotersQuery.GetVoterListAccToPollNoQuery(pollNum));

                        int currSize=1;
                        while (resultSet.next())
                            currSize++;

                        String voterId=generateVoterId(pollNum,currSize);

                        statement.execute(VotersQuery.GetInsertQuery(user.getName(),voterId,user.getPhoneNumber(),user.getDateOfBirth(),pollNum));
                        statement.execute(PollQuery.GetIncrementNoOfVotersQuery(user.getPollNumber()));
                        Log.d(TAG,"User Added Successfully");
                    }
                    else {
                        error="Invalid Input";
                        return false;
                    }
                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_REGISTER_USER:{
                    String voterID=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_REGISTER_USER_VOTER_ID);
                    String password=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_REGISTER_USER_PASSWORD);
                    String regTime=Long.toString(new Date().getTime());

                    Log.d(TAG,"Registering User "+voterID);
                    statement.execute(VotersQuery.GetUpdateMobileRegisteredQuery(voterID,password,regTime,"NULL"));
                    Log.d(TAG,"User " + voterID + " Registered Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_VOTER_PASSWORD:{
                    String voterId=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_VOTER_ID_KEY);
                    String password=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_VOTER_PASSWORD_KEY);

                    Log.d(TAG,"Updating Password for "+voterId);
                    statement.execute(VotersQuery.GetUpdatePasswordQuery(voterId,password));
                    Log.d(TAG,"Password Updated Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_VOTER_PHOTO:{
                    String voterId=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_VOTER_PHOTO_ID_KEY);
                    String photo=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_VOTER_PHOTO_KEY);

                    Log.d(TAG,"Updating Photo for "+voterId);
                    statement.execute(VotersQuery.GetUpdatePhotoUrlQuery(voterId,photo));
                    Log.d(TAG,"Photo Updated Successfully");

                    break;
                }

                case HashMapConstants.UPDATE_TYPE_CAST_VOTE:{
                    String voterId=(String)inputHashMap.get(HashMapConstants.UPDATE_PARAM_CAST_VOTE_VOTER_ID_KEY);
                    String candidateId=(String)inputHashMap.get(HashMapConstants.UPDATE_PARAM_CAST_VOTE_CANDIDATE_ID_KEY);
                    Integer pollNum=(Integer)inputHashMap.get(HashMapConstants.UPDATE_PARAM_CAST_VOTE_POLL_NUM_KEY);

                    if(pollNum!=null){
                        Log.d(TAG,"Casting Vote for "+voterId);
                        statement.execute(VotersQuery.GetUpdateHasVotedQuery(voterId));
                        statement.execute(CandidateQuery.GetIncreaseCandidateVoteQuery(candidateId));
                        statement.execute(PollQuery.GetIncrementNoOfVotesCastedQuery(pollNum));
                        Log.d(TAG,"Voting Successful");

                    }
                    else {
                        error="Invalid Input";
                        return false;
                    }

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_ADD_CANDIDATE:{
                    Candidate candidate=(Candidate)inputHashMap.get(HashMapConstants.UPDATE_PARAM_CANDIDATE_KEY);
                    if(candidate!=null){
                        int PollNum=candidate.getPollNumber();

                        ResultSet resultSet=statement.executeQuery(CandidateQuery.GetPollWiseCandidateQuery(PollNum));

                        int currPos=1;
                        if(!resultSet.first()){
                            currPos=0;
                        }
                        while (resultSet.next()){
                            currPos=Integer.parseInt(resultSet.getString(TableKeys.KEY_CANDIDATE_CAND_ID).substring(1,3));
                        }
                        currPos++;

                        String id=generateCandidateId(PollNum,currPos);

                        Log.d(TAG,"Adding New Candidate");
                        statement.execute(CandidateQuery.GetAddCandidateQuery(
                                candidate.getName(), id,
                                candidate.getPhoneNumber(),
                                candidate.getDateOfBirth(),
                                candidate.getElectionSymbolName(), PollNum));

                        statement.execute(PollQuery.GetIncrementNoOfCandidatesQuery(candidate.getPollNumber()));
                        Log.d(TAG,"Candidate Added Successfully");
                    }
                    else {
                        error="Invalid Input";
                        return false;
                    }
                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_UPDATE_CANDIDATE:{
                    String id=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_ID_KEY);
                    String name=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_NAME_KEY);
                    String phoneNum=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_PHONE_NUM_KEY);
                    Long dob=(Long) inputHashMap.get(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_DOB_KEY);
                    String symbolName=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_UPDATE_CANDIDATE_SYMBOL_NAME_KEY);

                    Log.d(TAG,"Updating Candidate Information");
                    statement.execute(CandidateQuery.GetUpdateCandidateQuery(id,name,phoneNum,dob,symbolName));
                    Log.d(TAG,"Candidate Information Updated Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_CANDIDATE_PHOTO:{
                    String id=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_CANDIDATE_PHOTO_ID_KEY);
                    String photo=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_CANDIDATE_PHOTO_URL_KEY);

                    Log.d(TAG,"Updating Photo for "+id);
                    statement.execute(CandidateQuery.GetUpdateCandidatePhotoQuery(id,photo));
                    Log.d(TAG,"Photo Updated Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_CANDIDATE_SYMBOL_PHOTO:{
                    String id=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_CANDIDATE_SYMBOL_PHOTO_ID_KEY);
                    String photo=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_CANDIDATE_SYMBOL_PHOTO_URL_KEY);

                    Log.d(TAG,"Updating Symbol Photo for "+id);
                    statement.execute(CandidateQuery.GetUpdateCandidateSymbolPhotoQuery(id,photo));
                    Log.d(TAG,"Photo Updated Successfully");

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_DELETE_CANDIDATE:{
                    String candidateId=(String)inputHashMap.get(HashMapConstants.UPDATE_PARAM_DELETE_CANDIDATE_ID_KEY);
                    Integer pollNum=(Integer)inputHashMap.get(HashMapConstants.UPDATE_PARAM_DELETE_CANDIDATE_POLL_NUM_KEY);

                    if(pollNum!=null){
                        Log.d(TAG,"Deleting Candidate "+candidateId);
                        statement.execute(CandidateQuery.GetDeleteCandidateQuery(candidateId));
                        statement.execute(PollQuery.GetDecrementNoOfCandidatesQuery(pollNum));
                        Log.d(TAG,"Candidate Deleted Successfully");
                    }
                    else {
                        error="Invalid Input";
                        return false;
                    }

                    break;
                }

                //Implemented
                case HashMapConstants.UPDATE_TYPE_REMOVE_PHOTO:{
                    String role=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_REMOVE_PHOTO_ROLE_KEY);
                    String id=(String) inputHashMap.get(HashMapConstants.UPDATE_PARAM_REMOVE_PHOTO_ID_KEY);

                    String query="";

                    switch (role){
                        case "Admin":{
                            query=AdminQuery.GetRemovePhotoQuery(id);
                            break;
                        }
                        case "Officer":{
                            query=OfficerQuery.GetRemovePhotoQuery(id);
                            break;
                        }
                        case "Voter":{
                            query=VotersQuery.GetRemovePhotoQuery(id);
                            break;
                        }
                        case "Candidate":{
                            query=CandidateQuery.GetRemoveCandidatePhotoQuery(id);
                            break;
                        }
                        case "CandidateSymbol":{
                            query=CandidateQuery.GetRemoveCandidateSymbolPhotoQuery(id);
                            break;
                        }
                    }

                    Log.d(TAG,"Removing Photo for "+id);
                    statement.execute(query);
                    Log.d(TAG,"Photo Removed Successfully");

                    break;
                }
            }
            return true;
        }
        catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            error=e.getLocalizedMessage();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
            updateInterface.onDataUpdated(UpdateType,true,null);
        else
            updateInterface.onDataUpdated(UpdateType,false,error);
    }

    private String generateVoterId(int pollNum, int position){
        return "P" + String.format("%02d",pollNum) + "_V" + String.format("%04d",position);
    }

    private String generateCandidateId(int pollNum,int position){
        return "C" + String.format("%02d",position) + "_P" + String.format("%02d",pollNum);
    }
}
