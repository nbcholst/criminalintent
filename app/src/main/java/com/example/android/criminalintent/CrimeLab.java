package com.example.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cholni01 on 5/19/2016.
 */
public class CrimeLab {

    private static CrimeLab sCrimelab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context){

        //retrieve crime database. Getwriteabledatabase also creates a new database file if it doesn't exist already
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);

        //first value is name of the table, third value is the value itself
        // second value is nullColumnHack which allows you to insert empty rows
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void removeCrime(Crime c) {
    }

    public List<Crime> getCrimes(){

        //initialize crimes array, retrieve all crime cursors by using null parameters
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        //cycle through cursors and add to array
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        //update database and specify which rows get updated
        //we use ? text identifier to make sure input uuidString is treated as a string and not a SQL injection attack
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    //convert crime class to a ContentValue for database storing
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }

    //use SQL cursor to retrieve data from database.
    // cursors give you raw column values based on inputs
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

    public static CrimeLab get(Context context){
        if (sCrimelab == null){
            sCrimelab = new CrimeLab(context);
        }
        return sCrimelab;
    }

}
