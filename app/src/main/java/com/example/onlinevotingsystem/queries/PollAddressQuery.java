package com.example.onlinevotingsystem.queries;

import com.example.onlinevotingsystem.constants.TableKeys;

public class PollAddressQuery {

    public static String getCreateQuery(){
        return "CREATE TABLE  IF NOT EXISTS "+ TableKeys.TABLE_NAME_POLL_ADDRESS+
                "(`"+TableKeys.KEY_POLL_ADDRESS_NUMBER+"` int NOT NULL," +
                "`"+TableKeys.KEY_POLL_ADDRESS_ADDRESS+"`  varchar(50) NOT NULL," +
                "PRIMARY KEY (`"+TableKeys.KEY_POLL_ADDRESS_NUMBER+"`))";
    }

    public static String GetInsertQuery(int pollNum, String address){
        return "INSERT INTO `" + TableKeys.TABLE_NAME_POLL_ADDRESS + "` VALUES (" + pollNum + ",'"  + address + "')";
    }

    public static String GetPollDetailsQuery(int pollno) {
        return " SELECT *  FROM " + TableKeys.TABLE_NAME_POLL_ADDRESS + " WHERE " + TableKeys.KEY_POLL_ADDRESS_NUMBER + " = " + pollno;
    }

    public static String GetAllPollAddress(){
        return "SELECT * FROM " + TableKeys.TABLE_NAME_POLL_ADDRESS;
    }
}
