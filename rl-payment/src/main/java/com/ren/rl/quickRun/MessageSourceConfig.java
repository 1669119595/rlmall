package com.ren.rl.quickRun;

import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MessageSourceConfig {
    public static void main(String[] args) {
        String pattern1 = "{0}，你好！你于{1}在工商银行存入{2} 元。";
        String pattern2 = "At {1,time,short} On{1,date,long}，{0} paid {2,number, currency}.";

        Object[] params = {"John", new GregorianCalendar().getTime(),1.0E3};

        MessageFormat messageFormat1 = new MessageFormat(pattern1); //default
        System.out.println("Defalut locale:" + Locale.getDefault());
        // set us locale
        MessageFormat messageFormat2 = new MessageFormat(pattern2, Locale.US);
        String defaultMessage = messageFormat1.format(params);
        String usMessage = messageFormat2.format(params);
        System.out.println(defaultMessage);
        System.out.println(usMessage);
    }
}
