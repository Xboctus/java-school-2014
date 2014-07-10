package com.leomze;




import java.sql.*;

public class DBManager {
    public void start ()
    {
        Connection conn = null;
        try
        {
            String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost/test";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
            e.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                    System.out.println ("Database connection terminated");
                }
                catch (Exception e) { }
            }
        }
    }


}
