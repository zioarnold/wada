package eni.it.gsrestservice.config;
/* ********************************************************
 * Web Administrator ADAM
 *
 * Writer by Cristian Marino - ENI 12-05-2016
 * Edited by Arnold Charyyev - 16/09/2016
 *
 * Release 1.0.0
 * JDK 1.8.u181++
 * ********************************************************/


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoggingMisc {

    /* ----------------------------------------------------------
     * Print Video Method with CR+LF
     * ----------------------------------------------------------
     */
    public void printConsole(int msg_level, String msg) {
        /* *******************************
         * LOG TYPE 1 - INFO 2 - ERROR 3 - WARNING 4 - CRITICAL 5 - DEBUG
         * ***************************
         */
        // - |DEBUG |Mon Mar 25 12:43:51 CET 2013|Subject : [WFScheduler]

        String msg_type = "";
        if (msg_level == 1) {
            msg_type = "INFO";
        } else if (msg_level == 2) {
            msg_type = "ERROR";
        } else if (msg_level == 3) {
            msg_type = "WARNING";
        } else if (msg_level == 4) {
            msg_type = "CRITICAL";
        } else if (msg_level == 5) {
            msg_type = "DEBUG";
        } else if (msg_level == 6) {
            msg_type = "FATAL";
        }

        Date data_log = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // String format = " - |%1$-8s|%2$-10s|%3$-70s\n";
        String format = "%1$-10s %2$s %3$s\n";
        System.out.format(format, sdf.format(data_log), msg_type, msg);
    }


    //Windows NT time is specified as the number of 100 nanosecond intervals since January 1, 1601.
    //UNIX time is specified as the number of seconds since January 1, 1970. There are 134,774 days (or 11,644,473,600 seconds) between these dates.
    //NT to Unix : Divide by 10,000,000 and subtract 11,644,473,600.
    //Unix to NT : Add 11,644,473,600 and multiply by 10,000,000
    public String timeStampToDate(String windowNTTimeStr) {
        String finalDate = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long windowsTime = Long.parseLong(windowNTTimeStr);
        long javaTime = windowsTime / 10000 - 11644473600000L;

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(javaTime));
        Date date = new Date(javaTime);

        Calendar cCurrent = Calendar.getInstance();
        cCurrent.setTime(new Date());
        cCurrent.add(Calendar.YEAR, 100);

        if (!(c.getTime().getYear() > cCurrent.getTime().getYear())) {
            finalDate = sdf.format(c.getTime());
        }
        //printConsole(1, "pwdLastSet: " + finalDate);
        return finalDate;

    }
}

/* ----------------------------------------------------------
 * END PROGRAM
 * ----------------------------------------------------------
 */
