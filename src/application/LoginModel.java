package application;

import java.sql.*;

public class LoginModel {
	private Connection connection;

	/**
	 * Generates a connection object. If unsuccessful, application exits.
	 * 
	 * @author Aakash
	 */
	public LoginModel() {
		connection = SQLiteConnection.Connector();
		if (connection == null) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}

	/**
	 * Checks whether the application is connected to the database.
	 * 
	 * @author Aakash
	 * @return boolean
	 */
	public boolean isDbConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks whether the login username/password credentials entered match a
	 * pair in the database login table.
	 * 
	 * @param user
	 *            the username entered by the user
	 * @param pass
	 *            the password entered by the user
	 * 
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean isLogin(String user, String pass) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		String query = "select * from login where username = ? and password = ?";
		try {
			pstm = connection.prepareStatement(query);

			// sets parameters of query
			pstm.setString(1, user);
			pstm.setString(2, pass);

			// execute query
			resultSet = pstm.executeQuery();

			// return true if row exists
			return resultSet.next() ? true : false;
		} catch (Exception e) {
			return false;
		} finally {
			pstm.close();
		}
	}

	/**
	 * Selects all columns from the login table where the username/password
	 * credentials entered by the user match.
	 * 
	 * @param user
	 *            the username entered by the user
	 * 
	 * @param pass
	 *            the password entered by the user
	 * 
	 * @return String array containing the id and role position
	 * @throws SQLException
	 */
	public String[] roleLogin(String user, String pass) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "select * from login where username = ? and password = ?";
		try {
			preparedStatement = connection.prepareStatement(query);

			// sets parameters of query
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);

			// execute query
			resultSet = preparedStatement.executeQuery();

			// loops through resultSet
			if (resultSet.next()) {
				String[] info = { resultSet.getString("id"), resultSet.getString("position") };
				return info;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally {
			preparedStatement.close();
			resultSet.close();
		}
	}
}
