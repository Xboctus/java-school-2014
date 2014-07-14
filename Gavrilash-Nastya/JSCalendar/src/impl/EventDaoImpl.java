package impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

import api.EventDao;
import centralStructure.Event;

public class EventDaoImpl implements EventDao {

	@Override
	public void putEventToBase(Event e) {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/schooldb", "root", "password");
			try {
				Timestamp myTimestamp = new Timestamp(e.getDate()
						.getTimeInMillis());
				PreparedStatement stat = con
						.prepareStatement("INSERT INTO event(text,date,user_name)VALUES(?,?,?);");
				stat.setString(1, e.getText());
				stat.setTimestamp(2, myTimestamp);
				stat.setString(3, e.getUser().getName());
				stat.executeUpdate();

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
				PreparedStatement stat = con
						.prepareStatement("DELETE FROM schooldb.event WHERE text = ? AND User_name = ? ;");
				stat.setString(1, e.getText());
				stat.setString(2, e.getUser().getName());
				stat.executeUpdate();

			} finally {
				con.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
