package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchMovieModel {
	
	

	public static ArrayList<String[]> searchMovie (String title, String date) throws SQLException {
		String query;
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		int x;
		
		if (title.isEmpty()) {
			x = 0;
			query = "select * from movies where date = ?";
		}
		
		else if (date.isEmpty()) {
			x=1;
			query = "select * from movies where title = ?";
		}
		
		else {
			x=2;
			query = "select * from movies where title = ? and date = ?";
		}
		
		
		Connection connection = SQLiteConnection.Connector();
		try {
			pstm = connection.prepareStatement(query);
			
			switch(x) {
			case 0: 
				pstm.setString(1, date);
				break;
				
			case 1:
				pstm.setString(1, title);
				break;
				
			case 2: 	
				pstm.setString(1, title);
				pstm.setString(2, date);
				break;
			}

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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
