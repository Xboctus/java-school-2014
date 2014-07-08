package com.javaschool2014.task1;


import java.sql.*;

public class DataLoaderSQL {

    private static Connection con = null;
    private static String username = "root";
    private static String password = "password";
    private static String URL = "jdbc:jtds:mysql://127.0.0.1:3306";

    public void dbConnection() {

        try {

            DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
            //Загружаем драйвер

            con = DriverManager.getConnection(URL, username, password);
            //соединяемся

            if(con!=null) System.out.println("Connection Successful !\n");
            if (con==null) System.exit(0);

            Statement st = con.createStatement();
            //Statement позволяет отправлять запросы базе данных

            ResultSet rs = st.executeQuery("select hd from pc group by hd having count(hd)>=2");
            //ResultSet получает результирующую таблицу

            int x = rs.getMetaData().getColumnCount();
            //Resultset.getMetaData() получаем информацию
            //результирующей таблице

            while(rs.next()){

                for(int i=1; i<=x;i++){

                    System.out.print(rs.getString(i) + "\t");

                }

                System.out.println();

            }

            System.out.println();

            if(rs!=null)rs.close();
            if(st!=null)st.close();
            if(con!=null)con.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

    }

}
