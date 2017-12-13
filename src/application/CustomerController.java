package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CustomerController {
	@FXML
	private Label welcomeLbl;
	@FXML
	private Button btnLogout;
	@FXML
	private Button btnBook;
	
	private String userName, ID;

	public void getUser(String user) {
		welcomeLbl.setText("Welcome customer, " + user + ".");
		userName = user;
	}
	
	public void getID(String ID) {
		this.ID = ID;	
	}

	public void Logout(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane employeePane = loader.load(getClass().getResource("/application/Login.fxml").openStream());
			Scene employeeScene = new Scene(employeePane);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow()); //gets Stage information
			window.setScene(employeeScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void launchBookMovie(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane bookMoviePane = loader.load(getClass().getResource("/application/BookMovie.fxml").openStream());
			Scene bookMovieScene = new Scene(bookMoviePane);
			BookMovieController bookMovieController = (BookMovieController) loader.getController();
			bookMovieController.getUser(userName);
			bookMovieController.getID(ID);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow()); //gets Stage information
			window.setScene(bookMovieScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void launchManageBookings(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane manageBookingsPane = loader.load(getClass().getResource("/application/ManageBookings.fxml").openStream());
			Scene manageBookingsScene = new Scene(manageBookingsPane);
			ManageBookingsController manageBookingsController = (ManageBookingsController) loader.getController();
			manageBookingsController.getUser(userName);
			manageBookingsController.getID(ID);
			manageBookingsController.getBookingHistory();
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow()); //gets Stage information
			window.setScene(manageBookingsScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void launchEditProfile(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane editProfilePane = loader.load(getClass().getResource("/application/EditProfile.fxml").openStream());
			Scene editProfileScene = new Scene(editProfilePane);
			EditProfileController editProfileController = (EditProfileController) loader.getController();
			editProfileController.getUser(userName);
			editProfileController.getID(ID);
			editProfileController.initEditProfile();
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow()); //gets Stage information
			window.setScene(editProfileScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
