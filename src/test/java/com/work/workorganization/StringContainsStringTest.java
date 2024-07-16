package com.work.workorganization;

public class StringContainsStringTest {
    public static void main(String[] args) {
        String sqlDetailsA = "select * from table A";
        String sqlDetailsB = "delete * from table A";
        String sqlDetailsC = "Insert into table A";
        String sqlDetailsD = "UPDATE table A ";
        boolean a = containsSQLKeyword(sqlDetailsA);
        System.out.println("a = " + a);
        boolean b = containsSQLKeyword(sqlDetailsB);
        System.out.println("b = " + b);
        boolean c = containsSQLKeyword(sqlDetailsC);
        System.out.println("c = " + c);
        boolean d = containsSQLKeyword(sqlDetailsD);
        System.out.println("d = " + d);
    }
    public static boolean containsSQLKeyword(String sqlDetails) {
        String lowerCaseSqlDetails = sqlDetails.toLowerCase();
        if (lowerCaseSqlDetails.contains("delete") || lowerCaseSqlDetails.contains("update") || lowerCaseSqlDetails.contains("insert")) {
            return true;
        } else {
            return false;
        }
    }
}
