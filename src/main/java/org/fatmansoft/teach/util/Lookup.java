package org.fatmansoft.teach.util;

import java.sql.*;

public class Lookup {
    public static void main(String[] args)
            throws SQLException, ClassNotFoundException {
        String dbUrl = "jdbc:sqlite:java.db";
        String user = "admin";
        String password = "admin";
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection(
                dbUrl, user, password);
        Statement s = c.createStatement();
        ResultSet r =
                s.executeQuery(
                        "SELECT per_num, per_name FROM person");
        while(r.next()) {
            System.out.println(
                    r.getString("per_num") + ", " + r.getString("per_name"));
        }
        r.close();
        s.close(); // Also closes ResultSet
        c.close();
    }
}


