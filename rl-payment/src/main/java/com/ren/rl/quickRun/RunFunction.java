package com.ren.rl.quickRun;

import java.util.Arrays;
import java.util.Base64;

public class RunFunction {

    public static void main(String[] args) {
        String str = "*11*";
        System.out.println(str.indexOf('*'));
        runBase64();
    }

    public static void runBase64(){
        String content = "this is some content";

        String contentName = String.format("data:%s;base64,%s", "contentName", org.apache.commons.codec.binary.Base64.encodeBase64String(content.getBytes()));
        String contentName1 = String.format("data:%s;base64,%s", "contentName", Base64.getEncoder().encodeToString(content.getBytes()));
        String contentName2 = String.format("data:%s;base64,%s", "contentName", Base64.getEncoder().encode(content.getBytes()));
        String contentName3 = String.format("data:%s;base64,%s", "contentName", Arrays.toString(Base64.getEncoder().encode(content.getBytes())));
        System.out.println(contentName);
        System.out.println(contentName1);
        System.out.println(contentName2);
        System.out.println(contentName3);
    }
}
