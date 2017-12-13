package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
	
	private String userName;
	private String[] movieArray;
	private String ID;
	String seats;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
	
	public void initConfirmation() {
		lblConfirmation.setText(userName + ", we look forward to seeing you for the Movie showing!");
		lblTitle.setText(movieArray[1]);
		lblDate.setText(movieArray[2]);
		lblTime.setText(movieArray[3]);
		lblSeats.setText(seats);
		int noSeats = seats.split(",").length;
		lblCost.setText("£" + String.valueOf(noSeats * 10));
	}
	
	public void getUser(String userName) {
		this.userName = userName;
	}
	
	public void getArray(String[] movieArray) {
		this.movieArray = movieArray;
	}
	
	public void getSeats(String seats) {
		this.seats = seats;
	}
	
	public void getID(String ID) {
		this.ID = ID;	
	}
	
	public void home(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane customerPane = loader.load(getClass().getResource("/application/Customer.fxml").openStream());
		CustomerController customerController = (CustomerController) loader.getController();
		customerController.getUser(userName);
		customerController.getID(ID);
		Scene customerScene = new Scene(customerPane);
		// This line gets the Stage information
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(customerScene);
	}

}
