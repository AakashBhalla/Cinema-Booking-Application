package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ScreenController implements Initializable {
	
	@FXML
	private Label txtTitle, txtDate, txtTime, txtaSeats, txtbSeats;
	
	@FXML
	private ImageView img;
	
	@FXML
	private Button a1,a2,a3,a4,b1,b2,b3,b4,c1,c2,c3,c4;
	
	private String userName;
	private String[] movieArray;
	
	public void getUser(String userName) {
		this.userName = userName;
	}
	
	public void getArray(String[] movieArray) {
		this.movieArray = movieArray;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
	}
	
	//this method is needed because initialize is called as soon as the FXMLLoader loads and parses the fxml file
	public void initScreen() {
		try {
			Button[] buttons = {a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4};
			char[] seating = ScreenModel.checkScreen(Integer.valueOf(movieArray[0]));
			System.out.print(Arrays.toString(seating));
			
			for (int i = 0; i < seating.length; i++) {
				if (seating[i] == 'N') {
					buttons[i].setStyle("-fx-background-color: green;");
					//buttons[i].setDisable(true);
				}
				else {
					Image customerIcon = new Image (this.getClass().getResourceAsStream("/resources/icon.png"));
					buttons[i].setPadding(Insets.EMPTY);
					buttons[i].setStyle("-fx-background-color: gray;");
					buttons[i].setText(null);
					buttons[i].setGraphic(new ImageView(customerIcon));
					//buttons[i].setDisable(true);
				}
				
			}
			
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		txtTitle.setText(movieArray[1]);
		txtDate.setText(movieArray[2]);
		txtTime.setText(movieArray[3]);
		txtaSeats.setText(movieArray[6]);
		txtbSeats.setText(String.valueOf(12-Integer.valueOf(movieArray[6])));
	
		try {
			Image image = new Image (movieArray[4]);
			img.setImage(image);
		} catch (Exception e) {
			System.out.print(e + "Invalid URL: Invalid URL or resource not found");
			e.printStackTrace();
		}
	}
	
	public void back(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane SearchMoviePane = loader.load(getClass().getResource("/application/SearchMovie.fxml").openStream());
			Scene SearchMovieScene = new Scene(SearchMoviePane);
			SearchMovieController searchMovieController = (SearchMovieController) loader.getController();
			searchMovieController.getUser(userName);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setScene(SearchMovieScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
