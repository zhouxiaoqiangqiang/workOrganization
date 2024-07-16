package com.work.workorganization;

import org.junit.platform.commons.util.StringUtils;

public class StringBlankTest {
    public static void main(String[] args) {
        String a = "";
        String b = String.valueOf(a);
        System.out.println("b = " + b);
        String b1 = a;
        System.out.println("b1 = " + b1);
        String b2 = StringUtils.isNotBlank(String.valueOf(a)) ? String.valueOf(a) : null;
        System.out.println("b2 = " + b2);

    }
}
