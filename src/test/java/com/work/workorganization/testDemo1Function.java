package com.work.workorganization;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class testDemo1Function {
//    public static void main(String[] args) {
//        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "TianLuo");
//
//        System.out.println("Uppercase Names:");
//        for (String name : names) {
//            String uppercaseName = name.toUpperCase();
//            System.out.println(uppercaseName);
//        }
//        System.out.println("==============================");
//        System.out.println("Lowercase Names:");
//        for (String name : names) {
//            String lowercaseName = name.toLowerCase();
//            System.out.println(lowercaseName);
//        }
//    }

    public static  void processNames (List<String> nameList, Function<String,String>nameProcessor,String processorType){
        System.out.println(processorType+"Names:");
        for (String name : nameList) {
            String applyName = nameProcessor.apply(name);
            System.out.println(applyName);
        }
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "TianLuo");

        processNames(names,String::toUpperCase,"UpperCase");
        processNames(names,String::toLowerCase,"LowerCase");
    }
}
