package impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import centralStructure.Event;
import centralStructure.User;
import api.UserDao;

public class UserDaoImpl implements UserDao {
	/**
	 * Save user to data base. If User already exist, it deletes and
	 */
	@Override
	public void putUserToBase(User u) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/schooldb", "root", "password");
			if (isUserExist(u.getName(), con)) {
				deleteUser(u.getName(), con);
			}
			try {
				PreparedStatement stat = con
						.prepareStatement("INSERT INTO user VALUES(?,?,?);");
				stat.setString(1, u.getName());
				stat.setBoolean(2, u.isActiveStatus());
				stat.setInt(3, u.getTimeZoneOffset());
				stat.executeUpdate();
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
				PreparedStatement stat = con
						.prepareStatement("UPDATE schooldb.user SET activity = ?,time_zone =? WHERE name = ?;");
				stat.setBoolean(1, u.isActiveStatus());
				stat.setInt(2, u.getTimeZoneOffset());
				stat.setString(3, u.getName());
				stat.executeUpdate();
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

	private boolean isUserExist(String s, Connection c) throws SQLException {
		PreparedStatement stat = null;
		try {
			stat = c.prepareStatement("SELECT * FROM user WHERE user.name =?");
			stat.setString(1, s);
			ResultSet set = stat.executeQuery();
			if (set.next()) {
				return true;
			} else {
				return false;
			}
		} finally {
			if (stat != null) {
				stat.close();
			}
		}
	}

	private void deleteUser(String s, Connection c) throws SQLException {
		PreparedStatement stat = null;
		try {
			stat = c.prepareStatement("DELETE FROM schooldb.event WHERE User_name = ? ;");
			stat.setString(1, s);
			stat.executeUpdate();
		} finally {
			if (stat != null) {
				stat.close();
			}
		}
		try {
			stat = c.prepareStatement("DELETE FROM schooldb.user WHERE name = ? ;");
			stat.setString(1, s);
			stat.executeUpdate();
		} finally {
			if (stat != null) {
				stat.close();
			}
		}
	}

	@Override
	public void setUserMapFromBase() throws IOException {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/schooldb", "root", "password");
			try {
				PreparedStatement statUser = con
						.prepareStatement("SELECT * FROM user");
				PreparedStatement statEvent = con
						.prepareStatement("SELECT* FROM event WHERE User_name=?");
				ResultSet set = statUser.executeQuery();
				while (set.next()) {
					User u = new User(set.getString("name"),
							set.getBoolean("activity"), set.getInt("time_zone"));
					statEvent.setString(1, u.getName());
					ResultSet eventSet = statEvent.executeQuery();
					while (eventSet.next()) {
						GregorianCalendar gc = new GregorianCalendar();
						gc.setTimeInMillis(eventSet.getTimestamp("date")
								.getTime());
						u.addEventToUser(new Event(u, gc, eventSet
								.getString("text")));
					}
				}

			} finally {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
