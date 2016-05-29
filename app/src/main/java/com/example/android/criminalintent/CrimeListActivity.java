package com.example.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by cholni01 on 5/19/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
