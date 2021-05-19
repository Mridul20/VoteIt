package com.example.onlinevotingsystem.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.onlinevotingsystem.constants.ConnectionConstants;
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

public class ConnectionEstablisher extends AsyncTask<Void,Void,Boolean> {

    public interface ConnectionInterface{
        void onConnectionResult(boolean result, String error);
    }

    private final String TAG = "ConnectionEstablisher";
    private final ConnectionInterface connectionInterface;
    private String errorResult;

    public ConnectionEstablisher(ConnectionInterface connectionInterface) {
        super();
        this.connectionInterface=connectionInterface;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            Class.forName(ConnectionConstants.JDBC_CLASS_NAME).newInstance();
            Log.d(TAG,"Class " + ConnectionConstants.JDBC_CLASS_NAME + " Loaded Successfully!");

            Connection connection=DriverManager.getConnection(ConnectionConstants.SERVER_URL,ConnectionConstants.USERNAME,ConnectionConstants.PASSWORD);
            Log.d(TAG,"Connection Successful!");
            
            Statement statement=connection.createStatement();

            showTableCheckLog(TableKeys.TABLE_NAME_ADMIN);
            statement.execute(AdminQuery.getCreateQuery());

            Log.d(TAG,"Checking if any Admin exists or not");
            ResultSet resultSet= statement.executeQuery(AdminQuery.GetCheckIfAnyAdminExistsQuery());
            if(resultSet.first()){
                Log.d(TAG,"Admin Already Exists");
            }
            else {
                Log.d(TAG,"No Admin exists! Adding a new Test Admin");
                statement.execute(AdminQuery.GetAddAdminQuery("testAdmin","abcd1234","Test Admin","+919580132139"));
                Log.d(TAG,"Sample Admin Added");
            }

            showTableCheckLog(TableKeys.TABLE_NAME_CANDIDATE);
            statement.execute(CandidateQuery.getCreateQuery());

            showTableCheckLog(TableKeys.TABLE_NAME_OFFICER);
            statement.execute(OfficerQuery.getCreateQuery());

            showTableCheckLog(TableKeys.TABLE_NAME_OFFICER_POLL_NO);
            statement.execute(OfficerPollNumQuery.getCreateQuery());

            showTableCheckLog(TableKeys.TABLE_NAME_POLL);
            statement.execute(PollQuery.getCreateQuery());

            showTableCheckLog(TableKeys.TABLE_NAME_POLL_ADDRESS);
            statement.execute(PollAddressQuery.getCreateQuery());

            showTableCheckLog(TableKeys.TABLE_NAME_VOTERS);
            statement.execute(VotersQuery.getCreateQuery());

            return true;
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            errorResult=e.getLocalizedMessage();
            return false;
        }
    }

    private void showTableCheckLog(String tableName){
        Log.d(TAG,"Checking Table - " + tableName);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
            connectionInterface.onConnectionResult(true,null);
        else
            connectionInterface.onConnectionResult(false,errorResult);
    }
}
