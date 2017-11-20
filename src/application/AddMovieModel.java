package application;

import java.sql.*;
import java.util.ArrayList;

public class AddMovieModel {
	public static void insertMovie(String title, String date, String time, String image, String description, int noSeats) throws SQLException {
		PreparedStatement pstm = null;
		String sql = "INSERT INTO movies(title, date, time, image, description, availableseats) VALUES(?,?,?,?,?,?)";
		try {
			Connection connection = SQLiteConnection.Connector();
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, title);
			pstm.setString(2, date);
			pstm.setString(3, time);
			pstm.setString(4, image);
			pstm.setString(5, description);
			pstm.setInt(6, noSeats);

			pstm.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			pstm.close(); 
		}
	}
	
	public static ArrayList<Integer[]> checkMovies(String date) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		String query = "SELECT time from movies WHERE date = ?";		
		try {
			Connection connection = SQLiteConnection.Connector();
			pstm = connection.prepareStatement(query);
			pstm.setString(1, date);
			
			resultSet = pstm.executeQuery();
			
			ArrayList<Integer[]> times = new ArrayList<Integer[]>();
			while (resultSet.next()) {
				String[] x = resultSet.getString("time").split(":");
				String[] y = x[1].split("-");
				times.add(new Integer[]{Integer.valueOf(x[0]), Integer.valueOf(y[0]), Integer.valueOf(y[1]), Integer.valueOf(x[2])});				
			}
			return times;
			
//			for (int i=0; i< times.size(); i++) { 
//				System.out.print(Arrays.toString(times.get(i)));
//			}

			
		} catch (Exception e) {
			return null;
			
		} finally {
			pstm.close();
			resultSet.close();
		}		
	}
}
