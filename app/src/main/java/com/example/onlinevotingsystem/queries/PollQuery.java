package com.example.onlinevotingsystem.queries;

import com.example.onlinevotingsystem.constants.TableKeys;

public class PollQuery {

    public static String getCreateQuery(){
        return "CREATE TABLE  IF NOT EXISTS "+ TableKeys.TABLE_NAME_POLL+
                "(`"+TableKeys.KEY_POLL_NUMBER+"` int NOT NULL," +
                "`"+TableKeys.KEY_POLL_NO_CANDIDATES+"` int DEFAULT 0," +
                "`"+TableKeys.KEY_POLL_NO_VOTERS+"` int DEFAULT 0," +
                "`"+TableKeys.KEY_POLL_ELEC_START_TIME+"` varchar(50) NOT NULL," +
                "`"+TableKeys.KEY_POLL_ELEC_END_TIME+"` varchar(50) NOT NULL," +
                "`"+TableKeys.KEY_POLL_NO_VOTES_CASTED+"` int DEFAULT 0," +
                "PRIMARY KEY (`"+TableKeys.KEY_POLL_NUMBER+"`))";
    }

    public static String GetInsertQuery(int Pollnum, long elecendtime , long elecstarttime){
        return "INSERT INTO `" + TableKeys.TABLE_NAME_POLL + "`  ( " + TableKeys.KEY_POLL_NUMBER +","  + TableKeys.KEY_POLL_ELEC_START_TIME +"," + TableKeys.KEY_POLL_ELEC_END_TIME +" ) VALUES ('" + Pollnum + "','"  + elecstarttime  + "','" + elecendtime + "')";
    }

    public static String GetPollDetailsQuery(int pollno) {
        return " SELECT *  FROM " + TableKeys.TABLE_NAME_POLL + " WHERE " + TableKeys.KEY_POLL_NUMBER + " = " + pollno;
    }

    public static String GetPollListQuery(){
        return " SELECT *  FROM " + TableKeys.TABLE_NAME_POLL;
    }

    public static String GetUpdateElecTimeQuery(int pollno , long elecendtime ,  long elecstarttime){
        return " UPDATE " + TableKeys.TABLE_NAME_POLL + " SET " + TableKeys.KEY_POLL_ELEC_END_TIME  + " = '" + elecendtime + "', " + TableKeys.KEY_POLL_ELEC_START_TIME  + " = '" + elecstarttime+  "' WHERE "+ TableKeys.KEY_POLL_NUMBER + " = " + pollno ;
    }

    public static String GetIncrementNoOfVotersQuery(int pollno) {
        return " UPDATE " + TableKeys.TABLE_NAME_POLL + " SET " + TableKeys.KEY_POLL_NO_VOTERS  + " = " + TableKeys.KEY_POLL_NO_VOTERS  +  " + 1  WHERE "+ TableKeys.KEY_POLL_NUMBER + " = " + pollno ;
    }

    public static String GetIncrementNoOfVotesCastedQuery(int pollno) {
        return " UPDATE " + TableKeys.TABLE_NAME_POLL + " SET " + TableKeys.KEY_POLL_NO_VOTES_CASTED  + " = " + TableKeys.KEY_POLL_NO_VOTES_CASTED  +  " + 1  WHERE "+ TableKeys.KEY_POLL_NUMBER + " = " + pollno ;
    }

    public static String GetIncrementNoOfCandidatesQuery(int pollno) {
        return " UPDATE " + TableKeys.TABLE_NAME_POLL + " SET " + TableKeys.KEY_POLL_NO_CANDIDATES  + " = " + TableKeys.KEY_POLL_NO_CANDIDATES  +  " + 1  WHERE "+ TableKeys.KEY_POLL_NUMBER + " = " + pollno ;
    }

    public static String GetDecrementNoOfCandidatesQuery(int pollno) {
        return " UPDATE " + TableKeys.TABLE_NAME_POLL + " SET " + TableKeys.KEY_POLL_NO_CANDIDATES  + " = " + TableKeys.KEY_POLL_NO_CANDIDATES  +  " - 1  WHERE "+ TableKeys.KEY_POLL_NUMBER + " = " + pollno ;
    }

}
