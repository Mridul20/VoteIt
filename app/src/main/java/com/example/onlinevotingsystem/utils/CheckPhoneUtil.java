package com.example.onlinevotingsystem.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPhoneUtil {

    public static boolean IsValidPhone(String phoneNum){
        if(phoneNum.length()!=10)
            return false;

        String regex="[0-9]+";
        Pattern p=Pattern.compile(regex);

        Matcher m=p.matcher(phoneNum);
        return m.matches();
    }

}
