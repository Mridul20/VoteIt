package com.example.onlinevotingsystem.queries;

import com.example.onlinevotingsystem.constants.TableKeys;

public class OfficerPollNumQuery {

    public static String getCreateQuery(){
        return "CREATE TABLE  IF NOT EXISTS "+ TableKeys.TABLE_NAME_OFFICER_POLL_NO+
                "(`"+TableKeys.KEY_OFFICER_POLL_NO_USERNAME+"` varchar(50)," +
                "`"+TableKeys.KEY_OFFICER_POLL_NO_POLL_NO+"`  int NOT NULL," +
                "PRIMARY KEY (`"+TableKeys.KEY_OFFICER_POLL_NO_POLL_NO+"`))";
    }

    public static String GetInsertQuery(int pollNo){
        return "INSERT INTO `" + TableKeys.TABLE_NAME_OFFICER_POLL_NO + "` VALUES (null," + pollNo +")";
    }

    public static String GetAddOfficerToPollQuery(String username, int pollNo){
        return " UPDATE " + TableKeys.TABLE_NAME_OFFICER_POLL_NO + " SET " + TableKeys.KEY_OFFICER_POLL_NO_USERNAME  + " = '" + username +  "' WHERE " + TableKeys.KEY_OFFICER_POLL_NO_POLL_NO  + " = " + pollNo ;
    }

    public static String GetUpdatePollNoQuery(String username, int pollNo){
        return " UPDATE " + TableKeys.TABLE_NAME_OFFICER_POLL_NO + " SET " + TableKeys.KEY_OFFICER_POLL_NO_POLL_NO  + " = " + pollNo +  " WHERE " + TableKeys.KEY_OFFICER_POLL_NO_USERNAME + " = '" + username + "'";
    }

////DOUBT
    public static String GetDeleteOfficeQuery(String username){
        return " DELETE FROM " + TableKeys.TABLE_NAME_OFFICER_POLL_NO + " WHERE " + TableKeys.KEY_OFFICER_POLL_NO_USERNAME + " = '" + username +  "'";
    }

    public static String GetUserNamePollNoQuery(String username){
        return " SELECT *  FROM " + TableKeys.TABLE_NAME_OFFICER_POLL_NO + " WHERE " + TableKeys.KEY_OFFICER_POLL_NO_USERNAME + " = '" + username +  "'";
    }

    public static String GetPollDetailsQuery(int pollNo) {
        return " SELECT *  FROM " + TableKeys.TABLE_NAME_OFFICER_POLL_NO + " WHERE " + TableKeys.KEY_OFFICER_POLL_NO_POLL_NO + " = " + pollNo;
    }

    public static String GetUnassignedPolls(){
        return "SELECT * FROM " + TableKeys.TABLE_NAME_OFFICER_POLL_NO + " WHERE " + TableKeys.KEY_OFFICER_POLL_NO_USERNAME + " IS NULL";
    }

}
