package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchMovieModel {

	public static ArrayList<String[]> searchMovie (String title, String date) throws SQLException {
		String query;
		Statement stm = null;
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		int x;
		
		if (title.isEmpty() & date.isEmpty()) {
			x = 0;
			query = "select * from movies";
		}
		
		else if (title.isEmpty()) {
			x = 1;
			query = "select * from movies where date = ?";
		}
		
		else if (date.isEmpty()) {
			x=2;
			query = "select * from movies where title = ?";
		}
		
		else {
			x=3;
			query = "select * from movies where title = ? and date = ?";
		}
		
		Connection connection = SQLiteConnection.Connector();
		try {	
			switch(x) {
			case 0:
				stm = connection.createStatement();
				resultSet = stm.executeQuery(query);
				break;
			
			case 1:
				pstm = connection.prepareStatement(query);
				pstm.setString(1, date);
				resultSet = pstm.executeQuery();
				break;
				
			case 2:
				pstm = connection.prepareStatement(query);
				pstm.setString(1, title);
				resultSet = pstm.executeQuery();
				break;
				
			case 3:
				pstm = connection.prepareStatement(query);
				pstm.setString(1, title);
				pstm.setString(2, date);
				resultSet = pstm.executeQuery();
				break;
			}
			
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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
