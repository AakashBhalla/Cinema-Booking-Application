package employee;

import java.sql.*;
import java.util.ArrayList;
import application.SQLiteConnection;

public class AddMovieModel extends SQLiteConnection{
	/**
	 * Inserts new row into the movie table of the database using parameters
	 * that are entered by an employee user.
	 * 
	 * @author Aakash
	 * @param title
	 *            the movie title
	 * @param date
	 *            the movie date
	 * @param time
	 *            the movie time
	 * @param image
	 *            the movie image url
	 * @param description
	 *            the movie description
	 * @param noSeats
	 *            the number of seats
	 * @throws SQLException
	 */
	public void insertMovie(String title, String date, String time, String image, String description, int noSeats)
			throws SQLException {
		PreparedStatement pstm = null;
		String sql = "INSERT INTO movies(title, date, time, image, description, availableseats) VALUES(?,?,?,?,?,?)";
		try {
			pstm = connection.prepareStatement(sql);
			// set parameters then execute update
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

	/**
	 * Inserts new row into the screen table of the database which indicates
	 * seating availability. By default all values are set the N, where N
	 * indicates that the seat has not been booked. The id in the movies table
	 * matches the screen table.
	 * 
	 * @author Aakash
	 * @throws SQLException
	 */
	public void insertScreen() throws SQLException {
		PreparedStatement pstm = null;
		String sql = "INSERT INTO screen(A1, A2, A3, A4, B1, B2, B3, B4, C1, C2, C3, C4) VALUES ('N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N')";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.executeUpdate();

		} catch (Exception e) {
			System.out.print(e);
		} finally {
			pstm.close();
		}
	}

	/**
	 * Selects and returns times from the movies table of the database where the
	 * date is defined by the user.
	 * 
	 * @author Aakash
	 * @param date
	 *            the date entered by the user
	 * @return an ArrayList containing String arrays. Each array contains the
	 *         start time and end time of a movie.
	 * @throws SQLException
	 */
	public ArrayList<String[]> checkMovieTimes(String date) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		String query = "SELECT time from movies WHERE date = ?";
		try {
			pstm = connection.prepareStatement(query);
			pstm.setString(1, date);

			// set date parameter
			resultSet = pstm.executeQuery();

			ArrayList<String[]> times = new ArrayList<String[]>();
			while (resultSet.next()) {
				// array is formed from splitting time string by dash delimiter
				// e.g. 12:00-13:00 becomes [12:00, 13:00]. The array is added
				// to the ArrayList.
				String[] time = resultSet.getString("time").split("-");
				times.add(time);
			}
			return times;
		} catch (Exception e) {
			return null;
		} finally {
			pstm.close();
			resultSet.close();
		}
	}
}
