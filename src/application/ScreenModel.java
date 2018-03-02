package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScreenModel extends SQLiteConnection{
	/**
	 * Receives the seating availability information of a particular movie from
	 * the screen table of the database, given by movie ID as a character array,
	 * where N indicates not booked and Y indicates booked.
	 * 
	 * @author Aakash
	 * @param movieID
	 *            the movie ID
	 * @return the seating availability character array
	 * @throws SQLException
	 */
	public char[] checkScreen(int movieID) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		String query = "SELECT A1, A2, A3, A4, B1, B2, B3, B4, C1, C2, C3, C4 from screen WHERE movieID = ?";

		try {
			pstm = connection.prepareStatement(query);
			pstm.setInt(1, movieID);

			resultSet = pstm.executeQuery();

			char[] screenSeating = new char[resultSet.getMetaData().getColumnCount()];
			// cycle through result set and assign to the character array
			for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
				screenSeating[i - 1] = resultSet.getString(i).charAt(0);
			}
			return screenSeating;
		} catch (Exception e) {
			return null;
		} finally {
			pstm.close();
			resultSet.close();
		}
	}

	/**
	 * Updates the seating availability information of a particular movie given
	 * by movie ID, in the screen table of the database.
	 * 
	 * @author Aakash
	 * @param movieID
	 *            the movie ID
	 * @param seating
	 *            character array containing N or Y for not booked or booked
	 */
	public void updateScreen(int movieID, char[] seating) {
		PreparedStatement pstm = null;
		String sql = "UPDATE screen SET A1 = ?, A2 = ?, A3 = ?, A4 = ?, B1 = ?, B2 = ?, B3 = ?, B4 = ?, C1 = ?, C2 = ?, C3 = ?, C4 = ? WHERE movieID = ?";

		try {
			pstm = connection.prepareStatement(sql);
			// set parameters then execute update
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

	/**
	 * Updates the number of seats available of a particular movie given by
	 * movie ID, in the movies table of the database.
	 * 
	 * @author Aakash
	 * @param movieID
	 *            the movie ID
	 * @param availableSeats
	 *            the new number of available seats
	 */
	public void updateMovie(int movieID, int availableSeats) {
		PreparedStatement pstm = null;
		String sql = "UPDATE movies SET availableseats = ? WHERE movieID = ?";

		try {
			pstm = connection.prepareStatement(sql);
			// set parameters then execute update
			pstm.setInt(1, availableSeats);
			pstm.setInt(2, movieID);

			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the booking information a customer has made into the bookings table
	 * of the database.
	 * 
	 * @author Aakash
	 * @param ID
	 *            the ID associated with a customer
	 * @param movieID
	 *            the movie ID
	 * @param seats
	 *            the seats a customer has booked
	 */
	public void addBooking(int ID, int movieID, String seats) {
		PreparedStatement pstm = null;
		String sql = "INSERT INTO bookings(id, movieID, seats) VALUES(?, ?, ?)";

		try {
			pstm = connection.prepareStatement(sql);
			// set parameters then execute update
			pstm.setInt(1, ID);
			pstm.setInt(2, movieID);
			pstm.setString(3, seats);

			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
