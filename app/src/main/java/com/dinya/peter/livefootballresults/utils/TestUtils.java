package com.dinya.peter.livefootballresults.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dinya.peter.livefootballresults.database.DbContract;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(DbContract.TeamEntry._ID, "200");
        cv.put(DbContract.TeamEntry.COLUMN_TEAM_NAME, "Manchester United");
        cv.put(DbContract.TeamEntry.COLUMN_TEAM_CODE, "MANUTD");
        cv.put(DbContract.TeamEntry.COLUMN_TEAM_VALUE, "1500");
        list.add(cv);

        cv = new ContentValues();
        cv.put(DbContract.TeamEntry._ID, "500");
        cv.put(DbContract.TeamEntry.COLUMN_TEAM_NAME, "Chelsea");
        cv.put(DbContract.TeamEntry.COLUMN_TEAM_CODE, "CHE");
        cv.put(DbContract.TeamEntry.COLUMN_TEAM_VALUE, "2000");
        list.add(cv);
        //insert all teams in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (DbContract.TeamEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(DbContract.TeamEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }



    }
}
