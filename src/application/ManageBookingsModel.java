package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageBookingsModel {

	public static ArrayList<String[]> getBookingHistory(int ID) {
		
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		
		String query = "SELECT b.ID, b.movieID, seats, m.title, date, time, availableseats FROM bookings b, movies m WHERE b.movieID = m.movieID and ID = ?";
		
		Connection connection = SQLiteConnection.Connector();
		try {
			pstm = connection.prepareStatement(query);
			pstm.setInt(1, ID);
			resultSet = pstm.executeQuery();
			
			ArrayList<String[]> results= new ArrayList<String[]>();
			
			while(resultSet.next()) {
				String[] arr = new String[7];
				for (int i =1 ; i <= 7; i++){
					arr[i-1] = resultSet.getString(i);
					if (i == 7) {
						results.add(arr);
					}
				}	
			}
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static void deleteBooking(int ID, int movieID, String seats) {
		PreparedStatement pstm = null;
		String sql = "DELETE FROM bookings WHERE id = ? AND movieID = ? AND seats = ?";
		
		Connection connection = SQLiteConnection.Connector();
		try {
			pstm = connection.prepareStatement(sql);
			//set the corresponding parameters
			pstm.setInt(1, ID);
			pstm.setInt(2, movieID);
			pstm.setString(3, seats);
			//execute the delete statement
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateMovieSeats(int movieID, int availableSeats) {
		PreparedStatement pstm = null;
		String sql = "UPDATE movies SET availableseats = ? WHERE movieID = ?";
		
		Connection connection = SQLiteConnection.Connector();
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setInt(1, availableSeats);
			pstm.setInt(2, movieID);
			
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
