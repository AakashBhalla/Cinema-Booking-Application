package application;

import java.sql.*;

public class SQLiteConnection {
	public Connection connection = Connector();

	/**
	 * Connects to the cinemabooking.sqlite database
	 * 
	 * @return the connection object
	 */
	public static Connection Connector() {
		try {
			Class.forName("org.sqlite.JDBC");
			// create connection
			Connection connection = DriverManager.getConnection("jdbc:sqlite:cinemabooking.sqlite");
			checkNullConnection(connection);
			return connection;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * If the connection is null, the application is terminated.
	 * 
	 * @author Aakash
	 */
	public static void checkNullConnection(Connection connection) {
		if (connection == null) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}
}
