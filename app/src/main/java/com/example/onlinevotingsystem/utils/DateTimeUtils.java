package com.example.onlinevotingsystem.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public static String getDisplayDate(Long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        String stringDate="";

        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);

        if(day<10)
            stringDate+="0";
        stringDate+=day+"-";

        if(month<10)
            stringDate+="0";
        stringDate+=month+"-";

        stringDate+=year;

        return stringDate;
    }

    public static String getDisplayTime(Long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        String stringTime="";

        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);

        if(hour==0)
            stringTime+="12:";
        else{
            if(hour<10)
                stringTime+="0";
            stringTime+=hour+":";
        }

        if(minute<10)
            stringTime+="0";
        stringTime+=minute+" ";

        if(calendar.get(Calendar.AM_PM)== Calendar.PM)
            stringTime+="PM";
        else
            stringTime+="AM";

        return stringTime;
    }

}
