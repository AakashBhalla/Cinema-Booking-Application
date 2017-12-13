package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditProfileModel {
	static Connection connection = SQLiteConnection.Connector();
	
	public static String[] getCustomerInfo(int ID) {
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		
		String query = "SELECT fName, lName, email FROM customerinfo WHERE id = ?";
		
		try {
			pstm = connection.prepareStatement(query);
			pstm.setInt(1, ID);
			resultSet = pstm.executeQuery();
			
			String[] customerInfo = new String[3];
			
			for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
				customerInfo[i-1] = resultSet.getString(i);
			}
			return customerInfo;	
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void updateInfo(int ID, String fName, String lName, String email) {
		PreparedStatement pstm = null;
		String sql = "UPDATE customerinfo SET fName =?, lName = ?, email = ? WHERE id = ?";
		
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, fName);
			pstm.setString(2, lName);
			pstm.setString(3, email);
			pstm.setInt(4, ID);
			
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
