package customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import application.SQLiteConnection;

public class EditProfileModel extends SQLiteConnection{
	/**
	 * Selects and returns the customer information from the customerinfo table
	 * of the database where the row is chosen by the customer ID
	 * 
	 * @author Aakash
	 * @param ID
	 *            the user ID
	 * @return String array containing the customer information
	 * @throws SQLException
	 */
	public String[] getCustomerInfo(int ID) throws SQLException {
		PreparedStatement pstm = null;
		ResultSet resultSet = null;

		String query = "SELECT fName, lName, email FROM customerinfo WHERE id = ?";

		try {
			pstm = connection.prepareStatement(query);
			pstm.setInt(1, ID);
			resultSet = pstm.executeQuery();

			String[] customerInfo = new String[3];

			for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
				customerInfo[i - 1] = resultSet.getString(i);
			}
			return customerInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			pstm.close();
			resultSet.close();
		}
	}

	/**
	 * Updates the customer information with new values that the user has
	 * entered
	 * 
	 * @author Aakash
	 * @param ID
	 *            the user ID
	 * @param fName
	 *            the user first name
	 * @param lName
	 *            the user last name
	 * @param email
	 *            the user email
	 * @throws SQLException
	 */
	public void updateInfo(int ID, String fName, String lName, String email) throws SQLException {
		PreparedStatement pstm = null;
		String sql = "UPDATE customerinfo SET fName =?, lName = ?, email = ? WHERE id = ?";

		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, fName);
			pstm.setString(2, lName);
			pstm.setString(3, email);
			pstm.setInt(4, ID);

			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstm.close();
		}
	}
}
