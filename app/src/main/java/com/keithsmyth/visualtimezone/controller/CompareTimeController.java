package com.keithsmyth.visualtimezone.controller;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keithsmyth on 31/07/2014.
 */
public class CompareTimeController {

    private static final int DISPLAY_HOURS = 100;
    private final List<List<String>> mDisplayLists;

    public CompareTimeController(List<String> timeZoneIds) {
        mDisplayLists = new ArrayList<List<String>>();
        generateTimeComparisons(timeZoneIds);
    }

    private void generateTimeComparisons(List<String> timeZoneIds) {
        mDisplayLists.clear();

        // Cache timezone objects
        DateTimeZone[] timeZones = new DateTimeZone[timeZoneIds.size()];
        for (int i = 0; i < timeZoneIds.size(); i++) {
            timeZones[i] = DateTimeZone.forID(timeZoneIds.get(i));
        }

        // Round current time down to the hour
        DateTime dateTime = new DateTime()
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);

        // format to local date/time (shorten for more comparisons)
        DateTimeFormatter dateTimeFormatter = timeZones.length < 3
                ? DateTimeFormat.shortDateTime()
                : DateTimeFormat.shortTime();

        // Iterate over the next n hours
        for (int i = 0; i < DISPLAY_HOURS; i++) {
            List<String> displayList = new ArrayList<String>();
            for (DateTimeZone dateTimeZone : timeZones) {
                displayList.add(dateTime.withZone(dateTimeZone).toString(dateTimeFormatter));
            }
            mDisplayLists.add(displayList);
            dateTime = dateTime.plusHours(1);
        }
    }

    public int getCount() {
        return mDisplayLists.size();
    }

    public List<String> getItem(int position) {
        return mDisplayLists.get(position);
    }
}
