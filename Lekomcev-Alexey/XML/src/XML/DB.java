package XML;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;

public class DB {
    private static Connection connection;
    private static final String createTableString = "CREATE TABLE IF NOT EXISTS book " +
            "(" +
            " id CHAR(20) NOT NULL," +
            " author CHAR(20)," +
            " genre CHAR(20)," +
            " isbn CHAR(20)," +
            " price FLOAT," +
            " publish_date DATE," +
            " description CHAR(200)," +
            " title CHAR(20)," +
            " PRIMARY KEY(id)" +
            ")";
    private static final String updateString = "UPDATE book SET " +
            "id = ?, author = ?, genre = ?, isbn = ?, price = ?, publish_date = ?, description = ?, title = ? WHERE id = ?";

    public DB() {
        try {
            connection = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        dropTables();
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate(createTableString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void dropTables() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS book");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost/books";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, "root", "password");
    }

    public static void insertIntoTable(ArrayList<Book> books) {
        for (Book book : books) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM book WHERE id = '" + book.getId() + "';";
                ResultSet resultSet = statement.executeQuery(query);
                if (!resultSet.next()) {
                    String stat = "INSERT INTO book (id,author,genre,isbn,price,publish_date,description,title) VALUES ('" +
                            book.getId() + "','" +
                            book.getAuthor() + "','" +
                            book.getGenre() + "','" +
                            book.getIsbn() + "','" +
                            book.getPrice() + "'," +
                            "?,'" +
                            book.getDescription() + "','" +
                            book.getTitle() +
                            "');";
                    PreparedStatement preparedStatement = connection.prepareStatement(stat);
                    Date date = new Date(book.getPublish_date().getTime());
                    preparedStatement.setDate(1, date);
                    preparedStatement.executeUpdate();
                    continue;
                }
                PreparedStatement preparedStatement = connection.prepareStatement(updateString);
                preparedStatement.setString(1, book.getId());
                preparedStatement.setString(2, book.getAuthor());
                preparedStatement.setString(3, book.getGenre());
                preparedStatement.setString(4, book.getIsbn());
                preparedStatement.setFloat(5, book.getPrice());
                preparedStatement.setDate(6, new Date(book.getPublish_date().getTime()));
                preparedStatement.setString(7, book.getDescription());
                preparedStatement.setString(8, book.getTitle());
                preparedStatement.setString(9, book.getId());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
}

