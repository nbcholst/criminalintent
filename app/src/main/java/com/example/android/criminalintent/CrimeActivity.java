package com.example.android.criminalintent;

//deprecated activity replaced by CrimePagerActivity

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity{

    private static final String EXTRA_CRIME_ID =
            "com.example.android.criminalintent.crime_id";
    private static final String EXTRA_CRIME_POSITION =
            "com.example.android.criminalintent.crime_pos";

    //function for creating an intent before calling the createFragment function (called from other activity)
    public static Intent newIntent(Context packageContext, UUID crimeId, int itemPosition) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_CRIME_POSITION, itemPosition);
        return intent;
    }

    //retried the intent specified
    @Override
    protected Fragment createFragment(){
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}
