package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScreenModel {
	
	public static char[] checkScreen(int movieID) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		String query = "SELECT A1, A2, A3, A4, B1, B2, B3, B4, C1, C2, C3, C4 from screen WHERE movieID = ?";
		
		try {
			Connection connection = SQLiteConnection.Connector();
			pstm = connection.prepareStatement(query);
			pstm.setInt(1, 1);
			
			resultSet = pstm.executeQuery();
			
			char[] screenSeating = new char[resultSet.getMetaData().getColumnCount()];
			
			
			for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i ++) {
				screenSeating[i-1] = resultSet.getString(i).charAt(0);
			}

			return screenSeating;	
		} catch (Exception e) {
			return null;
		} finally {
			pstm.close();
			resultSet.close();
		}
		
	}

}
