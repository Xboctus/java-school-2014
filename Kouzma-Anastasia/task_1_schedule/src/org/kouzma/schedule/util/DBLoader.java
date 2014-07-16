package org.kouzma.schedule.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.kouzma.schedule.Event;
import org.kouzma.schedule.User;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
/**
 * @author Anastasya Kouzma
 */
public class DBLoader {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static DBLoader instance;
	public static DBLoader getInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (instance == null)
			instance = new DBLoader();
			
		return instance;
	}

	private Connection conn;

	private List<User> unsavedUsers = new LinkedList<User>();
	private List<Event> unsavedEvents = new LinkedList<Event>();
	
	private DBLoader() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String str = "jdbc:mysql://localhost/schedule";
		String userName = "root";
		String pass = "pass";
        Class.forName ("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection(str, userName, pass);
	}	
	
	public int insertUser(User user) throws SQLException {
		String strQuery = "INSERT INTO user(name, timezone, status) " +
				"VALUES('" + user.getName() + "', " + user.getTimeZone() + ", " + user.getStatus() + ")";
		
		Statement stat = conn.createStatement();
		
		try {
			stat.executeUpdate(strQuery, Statement.RETURN_GENERATED_KEYS);
		} catch (MySQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return -1;
		}
		
		ResultSet rs = stat.getGeneratedKeys();
		if (rs != null && rs.next())
			return rs.getInt(1);
		
		return -1;
	}

	public void updateUser(User user) throws SQLException {
		conn.createStatement().executeUpdate("UPDATE user" +
				"SET timezone = " + user.getTimeZone() + ", " +
				"status = " + user.getStatus() + "" +
				"WHERE ID_user = " + user.getId());
	}

	public int insertEvent(Event event) throws SQLException {
		String strQuery = "INSERT INTO event(text, date, ID_user) " +
			"VALUES('" + event.getText() + "', '" + 
				dateFormat.format(event.getDate()) + "', " + 
				event.getUser().getId() + ")";
		
		Statement stat = conn.createStatement();
		stat.executeUpdate(strQuery, Statement.RETURN_GENERATED_KEYS);
		
		ResultSet rs = stat.getGeneratedKeys();
		if (rs != null && rs.next())
			return rs.getInt(1);
		
		return -1;
	}
	
	public List<User> selectUsersAndEvents() throws SQLException {
		ResultSet resUsers = conn.createStatement().executeQuery("SELECT * " +
				"FROM user LEFT JOIN event " + 
				"ON user.ID_user = event.ID_user " + 
				"ORDER BY user.ID_user");
		
		List<User> lstUsers = new LinkedList<User>();
		int currentUserId = -1;
		User currentUser = null;
		while (resUsers.next()) {
			int idUser = resUsers.getInt("ID_user");
			if (idUser != currentUserId) {
				String name = resUsers.getString("name");
				int timeZone = resUsers.getInt("timezone");
				boolean status = resUsers.getBoolean("status");
				currentUser = new User(idUser, name, timeZone, status);
				lstUsers.add(currentUser);
				currentUserId = idUser;
			}
			Integer idEvent = resUsers.getInt("ID_event");
			if (idEvent == 0)
				continue;
			String text = resUsers.getString("text");
			Date date = resUsers.getDate("date");
			currentUser.AddEvent(new Event(idEvent, text, date, currentUser));
		}
		resUsers.close();

		return lstUsers;
	}
	
	public boolean saveChanges(List<User> lstNewUsers, List<User> lstModifiedUsers, List<Event> lstNewEvents, List<Event> lstRemovedEvents) {
		try {
			conn.setAutoCommit(false);
			
			if (lstRemovedEvents.size() > 0) {
				deleteEvents(lstRemovedEvents);
				lstRemovedEvents.clear();
			}
			if (lstNewUsers.size() > 0) {
				addUsers(lstNewUsers);
			}
			if (lstModifiedUsers.size() > 0) {
				modifyUsers(lstModifiedUsers);
			}
			if (lstNewEvents.size() > 0) {
				addEvents(lstNewEvents);
			}
			
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public void addUsers(List<User> lstNewUsers) throws SQLException {
		for (User user : lstNewUsers) {
			int id = insertUser(user);
			user.setId(id);
			if (id == -1)
				unsavedUsers.add(user);
		}
	}
	
	public void addEvents(List<Event> lstNewEvents) throws SQLException {
		for (Event event : lstNewEvents) {
			int id = insertEvent(event);
			event.setId(id);
			if (id == -1)
				unsavedEvents.add(event);
		}
	}
	
	private void modifyUsers(List<User> lstModifiedUsers) throws SQLException {
		for (User user : lstModifiedUsers) {
			updateUser(user);
		}
	}
	
	public void deleteEvents(List<Event> lstRemoveEvents) throws SQLException {
		StringBuffer strIn = new StringBuffer();
		for (Event event : lstRemoveEvents) {
			strIn.append(event.getId() + ",");
		}
		strIn.subSequence(0, strIn.length() - 2); // Обрезаем последнюю запятую
		
		conn.createStatement().executeUpdate("DELETE FROM event" +
				"WHERE ID_event IN (" + strIn + ")");		
	}
	
	public List<User> getUnsavedUsers() {
		return unsavedUsers;
	}

	public List<Event> getUnsavedEvents() {
		return unsavedEvents;
	}
	
	public void clearUnsavedObjects() {
		unsavedUsers.clear();
		unsavedEvents.clear();
	}
}