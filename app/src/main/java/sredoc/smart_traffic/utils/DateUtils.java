package sredoc.smart_traffic.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String TAG = DateUtils.class.getSimpleName();

    public static final String DATE_TIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
    public static final String TIME_12_HOUR_FORMAT_STRING = "hh:mm a";
    public static final String DATE_DISPLAY_FORMAT_STRING = "EEE dd MMM, yy";
    public static final String DATE_PRINT_FORMAT_STRING = "EEE dd MMM, yy";
    public static final String DATE_MAIL_FORMAT_STRING = "EEE dd MMM, yy HH:mm:ss";
    public static final String DATE_TIME_DISPLAY_FORMAT_STRING = "EEE dd MMM, yy HH:mm";
    public static final String DATE_TIME_PRINT_FORMAT_STRING = "EEE dd MMM, yy HH:mm";
    public static final String TIME_FORMAT_STRING = "HH:mm:ss";
    public static final String DATE_MONTH_FORMAT_STRING = "dd MMM";
    public static final String DAY_FORMAT_STRING = "EEEE";
    public static final String DATE_PRETTY_FORMAT_STRING = "dd/MM/yyyy";

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT_STRING);
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);
    public static final SimpleDateFormat TIME_12_HOUR_FORMAT = new SimpleDateFormat(TIME_12_HOUR_FORMAT_STRING);
    public static final SimpleDateFormat DATE_DISPLAY_FORMAT = new SimpleDateFormat(DATE_DISPLAY_FORMAT_STRING);
    public static final SimpleDateFormat DATE_PRINT_FORMAT = new SimpleDateFormat(DATE_PRINT_FORMAT_STRING);
    public static final SimpleDateFormat DATE_MAIL_FORMAT = new SimpleDateFormat(DATE_MAIL_FORMAT_STRING);
    public static final SimpleDateFormat DATE_TIME_DISPLAY_FORMAT = new SimpleDateFormat(DATE_TIME_DISPLAY_FORMAT_STRING);
    public static final SimpleDateFormat DATE_TIME_PRINT_FORMAT = new SimpleDateFormat(DATE_TIME_PRINT_FORMAT_STRING);
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT_STRING);
    public static final SimpleDateFormat DATE_MONTH_FORMAT = new SimpleDateFormat(DATE_MONTH_FORMAT_STRING);
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat(DAY_FORMAT_STRING);
    public static final SimpleDateFormat DATE_PRETTY_FORMAT = new SimpleDateFormat(DATE_PRETTY_FORMAT_STRING);

    private static String days[] = new String[]{"", "SUN", "MON", "TUE", "WED",
            "THU", "FRI", "SAT"};

    /**
     * Formats the date into yyyy-MM-dd format
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }

    /**
     * Formats the date into HH:mm a format
     */
    public static String formatTime12hour(Date date) {
        if (date == null) {
            return "";
        }
        return TIME_12_HOUR_FORMAT.format(date);
    }

    /**
     * Formats the date into 'yyyy-MM-dd HH:mm:ss' format
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_TIME_FORMAT.format(date);
    }

    public static String getCurrentDayOfTheWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return days[day];
    }

    /**
     * Format date in dd MMM format
     *
     * @param date - date
     * @return string
     */
    public static String formatDateMonth(Date date) {
        if (date == null) return "";
        return DATE_MONTH_FORMAT.format(date);
    }

    /**
     * Format date in EEEE format
     *
     * @param date - date
     * @return string
     */
    public static String formatDay(Date date) {
        if (date == null) return "";
        return DAY_FORMAT.format(date);
    }

    /**
     * Formats the date into 'EEE dd MMM, yy' format
     */
    public static String formatDateDisplay(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_DISPLAY_FORMAT.format(date);
    }

    /**
     * Formats the date into 'EEE dd MMM, yy' format
     */
    public static String formatDatePrint(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_PRINT_FORMAT.format(date);
    }

    /**
     * Formats the date into 'EEE dd MMM, yy' format
     */
    public static String formatDateMail(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_MAIL_FORMAT.format(date);
    }

    /**
     * Formats the date into 'EEE dd MMM, yy HH:mm' format
     */
    public static String formatDateTimeDisplay(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_TIME_DISPLAY_FORMAT.format(date);
    }

    /**
     * Formats the date into 'EEE dd MMM, yy HH:mm' format
     */
    public static String formatDateTimePrint(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_TIME_PRINT_FORMAT.format(date);
    }

    /**
     * Formats the date into 'EEE dd MMM, yy HH:mm' format
     */
    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        return TIME_FORMAT.format(date);
    }

    /**
     * Formats the date into yyyy-MM-dd format
     */
    public static Date parseDate(String date) {
        if ((date == null) || date.isEmpty()) return null;
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Formats the date into dd/MM/yyyy format
     */
    public static Date parsePrettyDate(String date) {
        if ((date == null) || date.isEmpty()) return null;
        try {
            return DATE_PRETTY_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Formats the date into given format
     */
    public static Date parseTime(String time, SimpleDateFormat format) {
        if ((time == null) || time.isEmpty()) return null;
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Formats the date into HH:mm a format
     */
    public static Date parseTime12hour(String date) {
        if ((date == null) || date.isEmpty()) return null;
        try {
            return TIME_12_HOUR_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * Formats the date into 'yyyy-MM-dd HH:mm:ss' format
     */
    public static Date parseDateTime(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            return DATE_TIME_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Formats the date into  format 'EEE dd MMM, yy' format
     */
    public static Date parseDateDisplay(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            return DATE_DISPLAY_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * Formats the date into 'EEE dd MMM, yy HH:mm' format
     */
    public static Date parseDateTimeDisplay(String date) {
        if ((date == null) || date.isEmpty()) return null;

        try {
            return DATE_TIME_DISPLAY_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String getCurrentDate() {
        return DATE_FORMAT.format(new Date());
    }

    public static String getCurrentDateTime() {
        return DATE_TIME_FORMAT.format(new Date());
    }

    public static Date getEpochDateWithCurrentTime() {
        String timeString = "";
        timeString = TIME_FORMAT.format(new Date());
        try {
            return TIME_FORMAT.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date timeTravelDays(Date fromDate, int difference) {
        Calendar portal = Calendar.getInstance();
        portal.setTime(fromDate);

        portal.add(Calendar.DAY_OF_YEAR, difference);

        Date destination = portal.getTime();
        return destination;
    }

    public static Date timeTravelMonth(Date fromDate, int difference) {
        Calendar portal = Calendar.getInstance();
        portal.setTime(fromDate);

        portal.add(Calendar.MONTH, difference);

        Date destination = portal.getTime();
        return destination;
    }

    public static Date timeTravelYear(Date fromDate, int difference) {
        Calendar portal = Calendar.getInstance();
        portal.setTime(fromDate);

        portal.add(Calendar.YEAR, difference);

        Date destination = portal.getTime();
        return destination;
    }

    public static String changeDateFormat(String date, DateFormat from, DateFormat to) {
        try {
            return to.format(from.parse(date));
        } catch (ParseException e) {
           e.printStackTrace();
        }
        return null;
    }

    public static String changeDateFormat(Calendar calendar, DateFormat to) {
        return to.format(calendar.getTime());
    }

    public static String formatDateRange(Date startDate, Date endDate, String conjuction) {
        return (DateUtils.formatDateDisplay(startDate) + " " + conjuction + " " + DateUtils.formatDateDisplay(endDate));
    }

    public static boolean compareDatesEqual(Date date1, Date date2) {
        if (!date1.after(date2) && !date1.before(date2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks for the formatting of the string date and parses the date using either 'parseDate' or 'parseDateTime'
     */
    public static Date smartParseDateTime(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        if (date.length() == 19) {
            //assuming the formatting is 'yyyy-MM-dd HH:mm:ss'
            return parseDateTime(date);
        } else if (date.length() == 10) {
            //assuming the formatiing is 'yyyy-MM-dd'
            return parseDate(date);
        }
        return null;
    }

    /**
     * Gets the current epoch time. Is dependent on the device's H/W time.
     */
    public static long getCurrentEpochTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * Splits a time interval into hours, minutes and seconds
     *
     * @param time The time interval to split(in seconds)
     * @return An array with capacity 3, with the different variables in each of the positions
     * <p/>
     * <ul>
     * <li>0 - Hours</li>
     * <li>1 - Minutes</li>
     * <li>2 - Seconds</li>
     * </ul>
     */
    public static int[] getHoursMinsSecs(long time) {
        int hours = (int) time / 3600;
        int remainder = (int) time - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return new int[]{hours, mins, secs};
    }

    public static int getAge(Date date){
        long timeElapsed = getCurrentEpochTime() - date.getTime()/1000;
        int[] timeValues = getHoursMinsSecs(timeElapsed);
        final int hours = timeValues[0];
        final int days = hours / 24;
        return days/365;
    }

    /**
     * Returns time remaining
     * @param date
     * @return the formatted time
     */
    public static String getTimeRemaining(Date date) {
        long timeElapsed = date.getTime()/1000 - getCurrentEpochTime();
        int[] timeValues = getHoursMinsSecs(timeElapsed);
        final int hours = timeValues[0];
        final int minutes = timeValues[1];
        final int seconds = timeValues[2];
        final int days = hours / 24;
        final int weeks = days / 7;
        if (weeks != 0) {
            return weeks + " Weeks";
        } else if (days != 0) {
            return days + " Days";
        } else if (hours != 0) {
            return hours + " Hours";
        } else
            return minutes + " Mins";
    }
}
