package application;

import java.sql.*;

public class LoginModel {
	Connection connection;

	public LoginModel() {
		connection = SQLiteConnection.Connector();
		if (connection == null) {
			System.out.println("Connection not successful");
			System.exit(1);
		}
	}

	public boolean isDbConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isLogin(String user, String pass) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "select * from login where username = ? and password = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);

			resultSet = preparedStatement.executeQuery();
			return resultSet.next() ? true:false;
		} catch (Exception e) {
			return false;
		} finally {
			preparedStatement.close();
		}
	}

	public String roleLogin(String user, String pass) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "select * from login where username = ? and password = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("position");
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
