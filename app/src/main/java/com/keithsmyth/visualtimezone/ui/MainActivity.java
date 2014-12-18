package com.keithsmyth.visualtimezone.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.keithsmyth.visualtimezone.R;

import java.util.ArrayList;

/**
 * @author keithsmyth
 */
public class MainActivity extends Activity implements ICanStartCompare {

    private static final String ARG_TIME_ZONES = "timeZones";
    private static final String BACK_STACK = "back-stack";
    private ArrayList<String> mTimeZones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mTimeZones = null;
            startFragment(new SelectFragment(), false);
        }
        else {
            mTimeZones = savedInstanceState.getStringArrayList(ARG_TIME_ZONES);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(ARG_TIME_ZONES, mTimeZones);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void startCompare(ArrayList<String> timeZones) {
        mTimeZones = timeZones;
        CompareItemFragment fragment = CompareItemFragment.newInstance(timeZones);
        startFragment(fragment, true);
    }

    private void startFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(BACK_STACK);
        }
        transaction.commit();
    }
}
