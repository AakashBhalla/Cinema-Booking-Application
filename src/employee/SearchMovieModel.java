package employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import application.SQLiteConnection;

public class SearchMovieModel extends SQLiteConnection{
	/**
	 * Searches the movies table in the database for showings based on two
	 * parameters (title and date) which may be empty. Uses different query
	 * statements depending on what parameters have been entered by the user.
	 * The results are stored in an ArrayList of Arrays.
	 * 
	 * @author Aakash
	 * @param title the movie title
	 * @param date the movie date
	 * @return ArrayList of arrays, each array containing a distinct movie
	 *         showing's information
	 * @throws SQLException
	 */
	public ArrayList<String[]> searchMovie(String title, String date) throws SQLException {
		String query;
		Statement stm = null;
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		int x;

		// sets up different queries and assigns an int x which is later used
		// when executing the statement
		if (title.isEmpty() & date.isEmpty()) {
			x = 0;
			query = "select * from movies order by date, time";
		}

		else if (title.isEmpty()) {
			x = 1;
			query = "select * from movies where date = ? order by date, time";
		}

		else if (date.isEmpty()) {
			x = 2;
			query = "select * from movies where title = ? order by date, time";
		}

		else {
			x = 3;
			query = "select * from movies where title = ? and date = ? order by date, time";
		}

		// use x to create or prepare the statement then execute the query
		try {
			switch (x) {
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

			// store the results in an ArrayList of arrays
			ArrayList<String[]> results = new ArrayList<String[]>();

			while (resultSet.next()) {
				// create a new array with number of elements equal to number of
				// columns in table from sql query
				String[] arr = new String[resultSet.getMetaData().getColumnCount()];
				// cycle through row and add each element to arr then add arr to
				// the ArrayList once complete
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					arr[i - 1] = resultSet.getString(i);
					if (i == resultSet.getMetaData().getColumnCount()) {
						results.add(arr);
					}
				}
			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			switch(x) {
			case 0:
				stm.close();
				break;
				
			case 1:
				pstm.close();
				break;
			
			case 2:
				pstm.close();
				break;
				
			case 3:
				pstm.close();
				break;
			
			}
			resultSet.close();
		}
	}
}
