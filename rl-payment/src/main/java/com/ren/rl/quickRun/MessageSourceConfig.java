package com.ren.rl.quickRun;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageSourceConfig {
    public static void main(String[] args) {
//        String pattern1 = "{0}，你好！你于{1}在工商银行存入{2} 元。";
//        String pattern2 = "At {1,time,short} On{1,date,long}，{0} paid {2,number, currency}.";
//
//        Object[] params = {"John", new GregorianCalendar().getTime(),1.0E3};
//
//        MessageFormat messageFormat1 = new MessageFormat(pattern1); //default
//        System.out.println("Defalut locale:" + Locale.getDefault());
//        // set us locale
//        MessageFormat messageFormat2 = new MessageFormat(pattern2, Locale.US);
//        String defaultMessage = messageFormat1.format(params);
//        String usMessage = messageFormat2.format(params);
//        System.out.println(defaultMessage);
//        System.out.println(usMessage);

        messageByContext();
    }

    public static void messageProperties(){
        ResourceBundle rb1 = ResourceBundle.getBundle("resource", Locale.US);
        ResourceBundle rb2 = ResourceBundle.getBundle("resource", Locale.CHINA);
        System.out.println(rb2.getString("greeting.common"));
        System.out.println(rb1.getString("greeting.common"));
    }

    /**
     * use ResourceBundleMessageSource to load config
     */
    public static void messageByContext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Object[] params = {"John", new GregorianCalendar().getTime()};
        String message1 = context.getMessage("greeting.common", params, Locale.CHINA);
        String message2 = context.getMessage("greeting.common", params, Locale.US);
        System.out.println(message1);
        System.out.println(message2);
    }
}
