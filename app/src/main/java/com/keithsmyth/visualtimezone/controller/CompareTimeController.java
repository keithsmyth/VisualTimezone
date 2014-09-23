package com.keithsmyth.visualtimezone.controller;

import android.graphics.Color;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by keithsmyth on 31/07/2014.
 */
public class CompareTimeController {

    private static final int DISPLAY_HOURS = 100;
    private final List<List<String>> mDisplayLists;
    private DateTimeFormatter mDateTimeFormatter;

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
        mDateTimeFormatter = timeZones.length < 3
                ? DateTimeFormat.shortDateTime()
                : DateTimeFormat.shortTime();

        // Iterate over the next n hours
        for (int i = 0; i < DISPLAY_HOURS; i++) {
            List<String> displayList = new ArrayList<String>();
            for (DateTimeZone dateTimeZone : timeZones) {
                displayList.add(dateTime.withZone(dateTimeZone).toString(mDateTimeFormatter));
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

    private static enum BusinessHours {
        NOPE(1),
        MEH(2),
        OK(3),
        GOOD(4);

        private final int mValue;

        BusinessHours(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        /**
         * Returns the Business Hours enum based on the current and comparison
         *
         * @param current
         * @param dateTimeString
         * @param dateTimeFormatter
         *
         * @return
         */
        public static BusinessHours getBusinessHours(BusinessHours current,
                                                     String dateTimeString,
                                                     DateTimeFormatter dateTimeFormatter) {
            // always go with lowest value for items
            int value =
                    Math.min(current.getValue(), isBusinessHours(dateTimeString, dateTimeFormatter)
                            .getValue());

            // retrieve an enum to match int value
            for (BusinessHours hours : values()) {
                if (hours.getValue() == value) {
                    return hours;
                }
            }
            return null;
        }

        /**
         * returns based on DateTime if is in business hours
         *
         * @param dateTimeString
         * @param dateTimeFormatter
         *
         * @return
         */
        private static BusinessHours isBusinessHours(String dateTimeString,
                                                     DateTimeFormatter dateTimeFormatter) {
            DateTime dateTime = DateTime.parse(dateTimeString, dateTimeFormatter);

            int minute = dateTime.getMinuteOfDay();

            // GOOD = 9-17
            if (minute >= TimeUnit.HOURS.toMinutes(9) && minute <= TimeUnit.HOURS.toMinutes(17)) {
                return BusinessHours.GOOD;
            }

            // OK = 0800-0859, 1701-1800
            if (minute >= TimeUnit.HOURS.toMinutes(8) && minute < TimeUnit.HOURS.toMinutes(9) ||
                    (minute > TimeUnit.HOURS.toMinutes(17) && minute <= TimeUnit.HOURS
                            .toMinutes(18))) {
                return BusinessHours.OK;
            }

            // MEH = 0700-0759, 1801-1900
            if (minute >= TimeUnit.HOURS.toMinutes(7) && minute < TimeUnit.HOURS.toMinutes(8) ||
                    (minute > TimeUnit.HOURS.toMinutes(18) && minute <= TimeUnit.HOURS
                            .toMinutes(19))) {
                return BusinessHours.MEH;
            }

            return BusinessHours.NOPE;
        }
    }

    public int getItemBackgroundColour(int position) {
        List<String> values = getItem(position);

        BusinessHours hours = BusinessHours.GOOD;

        for (String dateTime : values) {
            hours = BusinessHours.getBusinessHours(hours, dateTime, mDateTimeFormatter);
        }

        switch (hours) {
        case MEH:
            return Color.GRAY;
        case OK:
            return Color.BLUE;
        case GOOD:
            return Color.GREEN;
        default:
            return Color.TRANSPARENT;
        }
    }


}
