package org.kouzma.schedule.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

import org.kouzma.schedule.Event;
import org.kouzma.schedule.User;

public class ScheduleConnection {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
	
	public static ScheduleConnection instance;
	public static ScheduleConnection getInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (instance == null)
			instance = new ScheduleConnection();
			
		return instance;
	}

	private Statement stat;
	
	// TODO
	private ScheduleConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String str = "jdbc:mysql://localhost/schedule";
		String userName = "root";
		String pass = "pass";
        Class.forName ("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection(str, userName, pass);
		stat = conn.createStatement();
	}	
	
	public int insertUser(User user) throws SQLException {
		stat.executeUpdate("INSERT INTO user(name, timezone, status) " +
				"VALUES('" + user.getName() + "', " + user.getTimeZone() + ", " + user.getStatus() + ")");
		
		ResultSet res = stat.executeQuery("SELECT user.ID_user" +
				" FROM user " +
				"WHERE name LIKE '" + user.getName() +
				"' LIMIT 1");
		if (res.next())
			return res.getInt("ID_user");
		
		return -1;
	}

	public void updateUser(User user) throws SQLException {
		stat.executeUpdate("UPDATE user" +
				"SET timezone = " + user.getTimeZone() + ", " +
				"status = " + user.getStatus() + "" +
				"WHERE ID_user = " + user.getId());
	}

	public int insertEvent(Event event) throws SQLException {
		String strQuery = "INSERT INTO event(text, date, ID_user) " +
			"VALUES('" + event.getText() + "', '" + 
				dateFormat.format(event.getDate()) + "', " + 
				event.getUser().getId() + ")";
		stat.executeUpdate(strQuery);
	
		ResultSet res = stat.executeQuery("SELECT event.ID_event" +
				" FROM event " +
				"WHERE text LIKE '" + event.getText() +
				"' LIMIT 1");
		if (res.next())
			return res.getInt("ID_event");
		
		return -1;
	}

	/*public void updateEvent(Event event) throws SQLException {
		stat.executeUpdate("UPDATE event" +
				"SET date = " + dateFormat.format(event.getDate()) + ", " +
				"WHERE ID_event = " + event.getId());		
	}*/
	
	public void deleteEvents(List<Integer> lstEventIds) throws SQLException {
		StringBuffer strIn = new StringBuffer();
		for (Integer eventId : lstEventIds) {
			strIn.append(eventId + ",");
		}
		strIn.subSequence(0, strIn.length() - 2); // Обрезаем последнюю запятую
		
		stat.executeUpdate("DELETE FROM event" +
				"WHERE ID_event IN (" + strIn + ")");		
	}
}
