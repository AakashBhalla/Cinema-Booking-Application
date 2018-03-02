package employee;

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

public class EmployeeController implements Initializable {

	@FXML
	private Label welcomeLbl;

	@FXML
	private Button btnLogout, btnAdd, btnSearch;

	private User user;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * Gets user object and sets label to welcome user.
	 * 
	 * @author Aakash
	 * @param user
	 *            the user object
	 */
	public void getUser(User user) {
		this.user = user;
		welcomeLbl.setText("Employee: " + user.getUserName());
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
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setScene(employeeScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launches the add movie view. Creates new pane by loading AddMovie fxml,
	 * and a new scene from the pane. Gets the stage information from the event
	 * source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after Add Movie button is pressed
	 */
	public void AddMovie(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane AddMoviePane = loader.load(getClass().getResource("/employee/AddMovie.fxml").openStream());
			Scene AddMovieScene = new Scene(AddMoviePane);
			AddMovieController addMovieController = (AddMovieController) loader.getController();
			addMovieController.getUser(user);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setScene(AddMovieScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launches the search movie view. Creates new pane by loading SearchMovie
	 * fxml, and a new scene from the pane. Gets the stage information from the
	 * event source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after Search Movie button is pressed
	 */
	public void SearchMovie(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane SearchMoviePane = loader.load(getClass().getResource("/employee/SearchMovie.fxml").openStream());
			Scene SearchMovieScene = new Scene(SearchMoviePane);
			SearchMovieController searchMovieController = (SearchMovieController) loader.getController();
			searchMovieController.getUser(user);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setScene(SearchMovieScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
