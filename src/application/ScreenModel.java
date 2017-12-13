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
			pstm.setInt(1, movieID); //to be changed to (1, movieID)
			
			resultSet = pstm.executeQuery();
			
			char[] screenSeating = new char[resultSet.getMetaData().getColumnCount()]; //new character array with number of columns
			
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
	
	public static void updateScreen(int movieID, char[] seating)  {
		PreparedStatement pstm = null;
		
		String sql = "UPDATE screen SET A1 = ?, A2 = ?, A3 = ?, A4 = ?, B1 = ?, B2 = ?, B3 = ?, B4 = ?, C1 = ?, C2 = ?, C3 = ?, C4 = ? WHERE movieID = ?";
		try {
			Connection connection = SQLiteConnection.Connector();
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, String.valueOf(seating[0]));
			pstm.setString(2, String.valueOf(seating[1]));
			pstm.setString(3, String.valueOf(seating[2]));
			pstm.setString(4, String.valueOf(seating[3]));
			pstm.setString(5, String.valueOf(seating[4]));
			pstm.setString(6, String.valueOf(seating[5]));
			pstm.setString(7, String.valueOf(seating[6]));
			pstm.setString(8, String.valueOf(seating[7]));
			pstm.setString(9, String.valueOf(seating[8]));
			pstm.setString(10, String.valueOf(seating[9]));
			pstm.setString(11, String.valueOf(seating[10]));
			pstm.setString(12, String.valueOf(seating[11]));
			pstm.setInt(13, movieID);
			
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void updateMovie(int movieID, int availableSeats) {
		PreparedStatement pstm = null;
		
		String sql = "UPDATE movies SET availableseats = ? WHERE movieID = ?";
		
		try {
			Connection connection = SQLiteConnection.Connector();
			pstm = connection.prepareStatement(sql);
			pstm.setInt(1, availableSeats);
			pstm.setInt(2, movieID);
			
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
	
	public static void addBooking(int ID, int movieID, String seats) {
		PreparedStatement pstm = null;
		
		String sql = "INSERT INTO bookings(id, movieID, seats) VALUES(?, ?, ?)";
		
		try {
			Connection connection = SQLiteConnection.Connector();
			pstm = connection.prepareStatement(sql);
			pstm.setInt(1, ID);
			pstm.setInt(2, movieID);
			pstm.setString(3, seats);
			
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
