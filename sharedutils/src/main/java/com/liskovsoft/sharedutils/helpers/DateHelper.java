package com.liskovsoft.sharedutils.helpers;

import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Format variants:<br/>
 * "dd.MM.yyyy HH:mm"<br/>
 * "MM/dd/yyyy hh:mm a"<br/>
 * "d MMM, y"<br/>
 * Manual: https://jenkov.com/tutorials/java-internationalization/simpledateformat.html#pattern-syntax
 */
public class DateHelper {
    /**
     * Input example: "2022-09-11T23:39:38+00:00"<br/>
     * https://stackoverflow.com/questions/2597083/illegal-pattern-character-t-when-parsing-a-date-string-to-java-util-date<br/>
     * https://stackoverflow.com/questions/7681782/simpledateformat-unparseable-date-exception
     */
    public static long toUnixTimeMs(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return 0;
        }

        String longPattern = "yyyy-MM-dd'T'HH:mm:ss" + (timestamp.contains("+") && Build.VERSION.SDK_INT > 19 ? "X" : "");
        String shortPattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(timestamp.contains("T") ? longPattern : shortPattern, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = format.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date != null ? date.getTime() : 0;
    }

    //public static String toShortDate(long unixTimeStamp) {
    //    Date date = new Date(unixTimeStamp);
    //
    //    Locale locale = Locale.getDefault();
    //    String pattern1 = "d MMM, y";
    //    String pattern2 = "d MMM " + (is24HourLocale(locale) ? "H:mm" : "h:mm a");
    //    SimpleDateFormat sdf = new SimpleDateFormat(pattern1, locale);
    //
    //    return sdf.format(date);
    //}

    //public static String toUpcomingDate(long timeMs) {
    //    Date date = new Date(timeMs);
    //
    //    Locale locale = Locale.getDefault();
    //    String pattern1 = "dd.MM.yyyy " + (is24HourLocale(locale) ? "HH:mm" : "hh:mm a");
    //    String pattern2 = "d MMM, y " + (is24HourLocale(locale) ? "HH:mm" : "hh:mm a");
    //    SimpleDateFormat sdf = new SimpleDateFormat(pattern2, locale);
    //
    //    return sdf.format(date);
    //}

    public static String toShortDate(long timeMs, boolean showHours) {
        Date date = new Date(timeMs);

        Locale locale = Locale.getDefault();
        String datePattern = is24HourLocale(locale) ? "d MMM, y" : "MMM d, y";
        String hoursPattern = is24HourLocale(locale) ? "HH:mm" : "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(showHours ? String.format("%s %s", datePattern, hoursPattern) : datePattern, locale);

        return sdf.format(date);
    }

    public static boolean is24HourLocale(Locale locale) {
        return !Helpers.equalsAny(locale.getLanguage(), "en", "es", "pt", "fr", "hi", "tl", "ar", "sw", "bn", "ur");
    }
}
