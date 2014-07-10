package impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import centralStructure.User;
import api.UserDao;

public class UserDaoImpl implements UserDao {
	@Override
	public void putUserToBase(User u) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/schooldb", "root", "password");
			try {
				Statement stat = con.createStatement();

				String command = "INSERT INTO user VALUES('" + u.getName()
						+ "'," + u.isActiveStatus() + ","
						+ u.getTimeZoneOffset() + ")";
				System.out.println(command);
				stat.executeUpdate(command);
			} finally {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void changeUser(User u) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/schooldb", "root", "password");
			try {
				Statement stat = con.createStatement();
				String command = "UPDATE schooldb.user SET activity = "
						+ u.isActiveStatus() + ",time_zone ="
						+ u.getTimeZoneOffset() + " WHERE name = '"
						+ u.getName() + "'";
				stat.executeUpdate(command);
				for (String eventName : u.getEvents().keySet()) {
					new EventDaoImpl().putEventToBase(u.getEvents().get(
							eventName));
				}
			} finally {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
