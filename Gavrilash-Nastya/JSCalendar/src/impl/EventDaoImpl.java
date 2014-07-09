package impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import api.EventDao;
import centralStructure.Event;

public class EventDaoImpl implements EventDao {

	@Override
	public void putEventToBase(Event e) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/schooldb", "root", "password");
			try {
				Statement stat = con.createStatement();
				Date date = new Date(e.getDate().getTimeInMillis());
				Time time = new Time(e.getDate().getTimeInMillis());
				String command = "INSERT INTO event(text,date,time,user_name)VALUES('"
						+ e.getText()
						+ "','"
						+ date
						+ "','"
						+ time
						+ "','"
						+ e.getUser().getName() + "')";
				System.out.println(command);
				stat.executeUpdate(command);

			} finally {
				con.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void removeEventFromBase(Event e) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/schooldb", "root", "password");
			try {
				Statement stat = con.createStatement();
				String command = "DELETE FROM schooldb.event WHERE text = '"
						+ e.getText() + "' AND User_name ='"
						+ e.getUser().getName() + "';";
				System.out.println(command);
				stat.executeUpdate(command);

			} finally {
				con.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
