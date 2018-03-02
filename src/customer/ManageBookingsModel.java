package customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import application.SQLiteConnection;

public class ManageBookingsModel extends SQLiteConnection{
	/**
	 * Retrieves users booking history (by their ID) from the bookings table of
	 * the database. Returns an array containing the movies information and
	 * customers confirmed seats.
	 * 
	 * @author Aakash
	 * @param ID
	 *            the user ID
	 * @return an Array List containing arrays, where each array has a booking
	 *         transaction information, including the movie information and the
	 *         seats
	 * @throws SQLException
	 */
	public ArrayList<String[]> getBookingHistory(int ID) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet resultSet = null;

		String query = "SELECT b.ID, b.movieID, seats, m.title, date, time, availableseats FROM bookings b, movies m WHERE b.movieID = m.movieID and ID = ?";
		try {
			pstm = connection.prepareStatement(query);
			// set parameters
			pstm.setInt(1, ID);
			resultSet = pstm.executeQuery();

			ArrayList<String[]> results = new ArrayList<String[]>();

			// cycle through the results. Create sting arrays for each result.
			while (resultSet.next()) {
				String[] arr = new String[resultSet.getMetaData().getColumnCount()];
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					arr[i - 1] = resultSet.getString(i);
					if (i == resultSet.getMetaData().getColumnCount()) {
						results.add(arr);
					}
				}
			}
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			pstm.close();
			resultSet.close();
		}
	}

	/**
	 * Deletes a customer booking from the bookings table of the database.
	 * 
	 * @author Aakash
	 * @param ID
	 *            the user ID
	 * @param movieID
	 *            the movie ID
	 * @param seats
	 *            the seats that were booked
	 * @throws SQLException
	 */
	public void deleteBooking(int ID, int movieID, String seats) throws SQLException {
		PreparedStatement pstm = null;
		String sql = "DELETE FROM bookings WHERE id = ? AND movieID = ? AND seats = ?";

		try {
			pstm = connection.prepareStatement(sql);
			// set the corresponding parameters
			pstm.setInt(1, ID);
			pstm.setInt(2, movieID);
			pstm.setString(3, seats);
			// execute the delete statement
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstm.close();
		}
	}

	/**
	 * Updates the number of available seats in the movies table of the database
	 * after a booking has been cancelled.
	 * 
	 * @author Aakash
	 * @param movieID
	 *            the movie ID
	 * @param availableSeats
	 *            the new number of available seats
	 * @throws SQLException
	 */
	public void updateMovieSeats(int movieID, int availableSeats) throws SQLException {
		PreparedStatement pstm = null;
		String sql = "UPDATE movies SET availableseats = ? WHERE movieID = ?";

		try {
			pstm = connection.prepareStatement(sql);
			pstm.setInt(1, availableSeats);
			pstm.setInt(2, movieID);

			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstm.close();
		}
	}
}
