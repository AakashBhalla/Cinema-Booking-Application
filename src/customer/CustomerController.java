package customer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.SQLException;

import application.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CustomerController{
	@FXML
	private Label welcomeLbl;
	
	@FXML
	private Button btnLogout, btnBook;

	private User user;
	
	/**
	 * Gets user object and sets label to welcome user.
	 * 
	 * @author Aakash
	 * @param user
	 *            the user object
	 */
	public void getUser(User user) {
		this.user = user;
		welcomeLbl.setText("Customer: " + user.getUserName());
	}

	/**
	 * Launches the login view. Creates new pane by loading login fxml, and a
	 * new scene from the pane. Gets the stage information from the event source
	 * and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after logout button is pressed
	 */
	public void Logout(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane employeePane = loader.load(getClass().getResource("/login/Login.fxml").openStream());
			Scene employeeScene = new Scene(employeePane);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow()); //gets Stage information
			window.setScene(employeeScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches the Book Movie view. Creates new pane by loading BookMovie fxml, and a
	 * new scene from the pane. Gets the stage information from the event source
	 * and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            after Book Movie is pressed
	 */
	public void launchBookMovie(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane bookMoviePane = loader.load(getClass().getResource("/customer/BookMovie.fxml").openStream());
			Scene bookMovieScene = new Scene(bookMoviePane);
			BookMovieController bookMovieController = (BookMovieController) loader.getController();
			bookMovieController.getUser(user);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow()); //gets Stage information
			window.setScene(bookMovieScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches the Manage Bookings view. Creates new pane by loading ManageBookings fxml, and a
	 * new scene from the pane. Gets the stage information from the event source
	 * and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            after Manage Bookings is pressed
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public void launchManageBookings(ActionEvent event) throws NumberFormatException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane manageBookingsPane = loader.load(getClass().getResource("/customer/ManageBookings.fxml").openStream());
			Scene manageBookingsScene = new Scene(manageBookingsPane);
			ManageBookingsController manageBookingsController = (ManageBookingsController) loader.getController();
			manageBookingsController.getUser(user);
			manageBookingsController.getBookingHistory();
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow()); //gets Stage information
			window.setScene(manageBookingsScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches the Edit Profile view. Creates new pane by loading EditProfile fxml, and a
	 * new scene from the pane. Gets the stage information from the event source
	 * and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            after Edit Profile is pressed
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public void launchEditProfile(ActionEvent event) throws NumberFormatException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane editProfilePane = loader.load(getClass().getResource("/customer/EditProfile.fxml").openStream());
			Scene editProfileScene = new Scene(editProfilePane);
			EditProfileController editProfileController = (EditProfileController) loader.getController();
			editProfileController.getUser(user);
			editProfileController.initEditProfile();
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow()); //gets Stage information
			window.setScene(editProfileScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
