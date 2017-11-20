package application;

import java.io.IOException;
import java.net.URL;
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

public class EmployeeController implements Initializable {

	@FXML
	private Label welcomeLbl;
	
	@FXML
	private Button btnLogout;
	
	@FXML
	private Button btnAdd;
	
	private String userName;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void getUser (String user) {
		userName = user; //new
		welcomeLbl.setText("Welcome employee, " + user + ".");
	}
	
	public void Logout (ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane employeePane = loader.load(getClass().getResource("/application/Login.fxml").openStream());
			Scene employeeScene = new Scene(employeePane);
			//This line gets the Stage information
			Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
			window.setScene(employeeScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void AddMovie (ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane AddMoviePane = loader.load(getClass().getResource("/application/AddMovie.fxml").openStream());
			Scene AddMovieScene = new Scene(AddMoviePane);
			AddMovieController addMovieController = (AddMovieController)loader.getController(); 
			addMovieController.getUser(userName);
			//This line gets the Stage information
			Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
			window.setScene(AddMovieScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
