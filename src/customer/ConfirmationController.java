package customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConfirmationController implements Initializable {

	@FXML
	private Label lblConfirmation, lblTitle, lblDate, lblTime, lblSeats, lblCost;

	@FXML
	private Button btnHome;

	private User user;
	private String[] movieArray;
	private String seats;

	/**
	 * Called to initialize the Confirmation Controller after its root element
	 * has been completely processed
	 * 
	 * @author Aakash
	 * @param arg0
	 *            The location used to resolve relative paths for the root
	 *            object, or null if the location is not known.
	 * @param arg1
	 *            The resources used to localize the root object, or null if the
	 *            object was not localized.
	 * @see initialize
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * Gets User object called user. 
	 * 
	 * @author Aakash
	 * @param user
	 *            a user object
	 */
	public void getUser(User user) {
		this.user = user;
	}

	/**
	 * Gets movie informationas a passed parameter from previous class.
	 * 
	 * @author Aakash
	 * @param movieArray
	 *            the movie information stored in an Array
	 */
	public void getArray(String[] movieArray) {
		this.movieArray = movieArray;
	}

	/**
	 * Gets the seats a user has booked as a passed parameter from previous
	 * class.
	 * 
	 * @author Aakash
	 * @param seats
	 *            the seats a user has booked
	 */
	public void getSeats(String seats) {
		this.seats = seats;
	}
	
	/**
	 * Sets the pages labels. Called before the scene is set.
	 * 
	 * @author Aakash
	 */
	public void initConfirmation() {
		lblConfirmation.setText(user.getUserName() + ", we look forward to seeing you for the Movie showing!");
		lblTitle.setText(movieArray[1]);
		lblDate.setText(movieArray[2]);
		lblTime.setText(movieArray[3]);
		lblSeats.setText(seats);
		int noSeats = seats.split(",").length;
		lblCost.setText("£" + String.valueOf(noSeats * 10));
	}

	/**
	 * Launches the customer view. Creates new pane by loading employee fxml,
	 * and a new scene from the pane. Gets the stage information from the event
	 * source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after home button is pressed
	 * @throws IOException
	 */
	public void home(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane customerPane = loader.load(getClass().getResource("/customer/Customer.fxml").openStream());
		CustomerController customerController = (CustomerController) loader.getController();
		// pass parameters userName and ID
		customerController.getUser(user);
		Scene customerScene = new Scene(customerPane);
		// This line gets the Stage information
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(customerScene);
	}
}
