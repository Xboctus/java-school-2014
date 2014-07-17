package com.javaschool2014.parser;


import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLHandler implements Constants {

    private static Connection connection = null;
    private static String username       = "root";
    private static String password       = "password";
    private static String url            = "jdbc:mysql://127.0.0.1:3306/book_data";

    private static List eventList        = new ArrayList();

    public SQLHandler() {

        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

    }

    public SQLHandler(String url, String username, String password) {

        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

    }

    // Separating saveBook and saveData, to improve readability:
    public boolean saveData(List<Book> list) {

        // Save data
        for (Book book : list) {

            System.out.println(book.toString());

            saveBook(book.getBookId(), book.getAuthor(), book.getTitle(),
                    book.getGenre(), book.getIsbn(), book.getPrice(),
                    book.getPublishDate(), book.getDescription());

        }

        return true;

    }


    public void saveBook(String id, String author, String title,
                                      String genre, String isbn, float price,
                                      String publishDate, String description) {

        Statement statement = null;
        ResultSet resultSet = null;

        try {

            // Execute query
            statement = connection.createStatement();

            if (isbn != null) {

                if (statement.execute("SELECT * FROM books WHERE isbn = \"" + isbn + "\"")) {
                    resultSet = statement.getResultSet();
                }

            } else {

                if (statement.execute("SELECT * FROM books WHERE author = \"" + author +"\" AND title = \"" + title +"\"")) {
                    resultSet = statement.getResultSet();
                }

            }

            if (resultSet.next()) {
                // Update
                statement.execute("UPDATE books SET book_id=\"" + id + "\", genre=\"" + genre + "\", price=" + price + ", " +
                        "publish_date=\"" + publishDate + "\", description=\"" + description + "\" " +
                        "WHERE author = \"" + author +"\" AND title = \"" + title +"\"");
            } else {
                // Insert
                statement.execute("INSERT INTO books (book_id, author, title, genre, isbn, price, publish_date, description) " +
                        "VALUES (\"" + id + "\", \"" + author + "\", \"" + title + "\", \"" + genre + "\", " +
                        "\"" + isbn + "\", " + price + ", \"" + publishDate + "\", \"" + description + "\")");
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {

            if (resultSet != null) {

                try {
                    resultSet.close();
                } catch (SQLException e) {}

                resultSet = null;

            }

            if (statement != null) {

                try {
                    statement.close();
                } catch (SQLException e) {}

                statement = null;

            }

        }

    }

    public void closeConnection() {

        // CLose db connection
        if (connection != null) {

            try {
                connection.close();
            } catch (SQLException ignore) {
                // No sense.
            }

        }

    }

}