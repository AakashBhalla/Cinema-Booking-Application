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

	public void getUser(String user) {
		welcomeLbl.setText("Welcome customer, " + user + ".");
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
}
