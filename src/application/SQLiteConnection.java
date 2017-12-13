package application;

import java.sql.*;

public class SQLiteConnection {
	/**
	 * Connects to the cinemabooking.sqlite database
	 * 
	 * @return the connection object
	 */
	public static Connection Connector() {
		try {
			Class.forName("org.sqlite.JDBC");
			// create connection
			Connection conn = DriverManager.getConnection("jdbc:sqlite:cinemabooking.sqlite");
			return conn;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
