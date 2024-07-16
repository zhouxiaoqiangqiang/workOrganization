package com.work.workorganization;

public class StringSpiltTest {
    public static void main(String[] args) {
        String oracleUrl = "jdbc:oracle:thin:@//127.0.0.1:1521:database";
        String mySQLUrl = "jdbc:mysql://127.0.0.1:3306/mydatabase?useSSL=false";

        String oracleIp = getUrlParameter(oracleUrl, "ip");
        System.out.println("oracleIp = " + oracleIp);
        String oraclePort = getUrlParameter(oracleUrl, "port");
        System.out.println("oraclePort = " + oraclePort);
        String oracleDatabaseName = getUrlParameter(oracleUrl, "databaseName");
        System.out.println("oracleDatabaseName = " + oracleDatabaseName);
        String mySQLIp = getUrlParameter(mySQLUrl, "ip");
        System.out.println("mySQLIp = " + mySQLIp);
        String mySQLPort = getUrlParameter(mySQLUrl, "port");
        System.out.println("mySQLPort = " + mySQLPort);
        String mySQLDatabaseName = getUrlParameter(mySQLUrl, "databaseName");
        System.out.println("mySQLPort = " + mySQLDatabaseName);
    }

    public static String getUrlParameter(String url, String type) {
        String ip = null;
        String port = null;
        String databaseName = null;

        if (url.startsWith("jdbc:mysql")) {
            String[] urlParts = url.replace("jdbc:mysql://", "").split(":|/|\\?");
            if ("ip".equalsIgnoreCase(type) && urlParts.length >= 1) {
                ip = urlParts[0];
            } else if ("port".equalsIgnoreCase(type) && urlParts.length >= 2) {
                port = urlParts[1];
            } else if ("databaseName".equalsIgnoreCase(type) && urlParts.length >= 3) {
                databaseName = urlParts[2];
            }
        }  else if (url.startsWith("jdbc:oracle")) {
            String[] urlParts = url.replace("jdbc:oracle:thin:@//", "").split(":|/|@");
            if ("ip".equalsIgnoreCase(type) && urlParts.length >= 1) {
                ip = urlParts[0];
            } else if ("port".equalsIgnoreCase(type) && urlParts.length >= 2) {
                port = urlParts[1];
            } else if ("databaseName".equalsIgnoreCase(type) && urlParts.length >= 3) {
                databaseName = urlParts[2];
            }
        }

        if ("ip".equalsIgnoreCase(type)) {
            return ip;
        } else if ("port".equalsIgnoreCase(type)) {
            return port;
        } else if ("databaseName".equalsIgnoreCase(type)) {
            return databaseName;
        } else {
            return null;
        }


}

}
