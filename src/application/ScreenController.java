package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private Label txtTitle, txtDate, txtTime, txtaSeats, txtbSeats, lblMsg;

	@FXML
	private ImageView img;

	@FXML
	private Button a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4, btnConfirm;

	private String userName, role, ID;
	private String[] movieArray;
	// private Button[] buttons = {a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3,
	// c4};
	private char[] seating;
	private String seats = "";
	// private String selectedSeats = "";

	private ArrayList<String> selectedSeats = new ArrayList<String>();

	public void getUser(String userName) {
		this.userName = userName;
	}

	public void getArray(String[] movieArray) {
		this.movieArray = movieArray;
	}

	public void getRole(String role) {
		this.role = role;
	}
	
	public void getID(String ID) {
		this.ID = ID;	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	// this method is needed because initialize is called as soon as the
	// FXMLLoader loads and parses the fxml file
	public void initScreen() {
		try {
			seating = ScreenModel.checkScreen(Integer.valueOf(movieArray[0]));
			Button[] buttons = { a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4 };
			for (int i = 0; i < seating.length; i++) {
				if (seating[i] == 'N') {
					buttons[i].setStyle("-fx-background-color: green;");
				} else {
					Image customerIcon = new Image(this.getClass().getResourceAsStream("/resources/icon.png"));
					buttons[i].setPadding(Insets.EMPTY);
					buttons[i].setStyle("-fx-background-color: gray;");
					buttons[i].setText(null);
					buttons[i].setGraphic(new ImageView(customerIcon));
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
		txtbSeats.setText(String.valueOf(12 - Integer.valueOf(movieArray[6])));

		try {
			Image image = new Image(movieArray[4]);
			img.setImage(image);
		} catch (Exception e) {
			System.out.print(e + "Invalid URL: Invalid URL or resource not found");
			e.printStackTrace();
		}

		if (role.equals("customer")) {
			customerView();
		}
	}

	private void customerView() {
		btnConfirm.setVisible(true);

		for (Button b : Arrays.asList(a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4)) {
			b.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					//System.out.print("clicked " + b.getId().toUpperCase());
					if (b.getStyle().equals("-fx-background-color: green;")) {
						b.setStyle("-fx-background-color: red;");
						selectedSeats.add(b.getId().toUpperCase());
						updateLabel(selectedSeats, false);
					} else if (b.getStyle().equals("-fx-background-color: red;")) {
						b.setStyle("-fx-background-color: green;");
						selectedSeats.remove(b.getId().toUpperCase());
						updateLabel(selectedSeats, false);
					} else {
						updateLabel(selectedSeats, true);
					}
				}
			});
		}
	}

	private void updateLabel(ArrayList<String> selectedSeats, boolean isError) {
//		for (String s : selectedSeats) {
//			System.out.print("element" + s);
//		}
		Collections.sort(selectedSeats);
		
		String outputMsg = "";
		seats = "";

		if (selectedSeats.isEmpty()) {
			outputMsg = "No seats selected.";
		}

		else {
			for (int i = 0; i < selectedSeats.size(); i++) {
				if (i == (selectedSeats.size() - 1)) { // if it's the final
														// element of the list,
														// no comma added
					seats = seats + selectedSeats.get(i);
				} else {
					seats = seats + selectedSeats.get(i) + ", ";
				}
			}
			outputMsg = "Selected seats: " + seats;
		}

		if (isError) {
			outputMsg = "That seat is taken. " + outputMsg;
		}
		lblMsg.setText(outputMsg);
	}

	public void back(ActionEvent event) {
		if (role.equals("customer")) {
			FXMLLoader loader = new FXMLLoader();
			try {
				Pane BookMoviePane = loader.load(getClass().getResource("/application/BookMovie.fxml").openStream());
				Scene BookMovieScene = new Scene(BookMoviePane);
				BookMovieController bookMovieController = (BookMovieController) loader.getController();
				bookMovieController.getUser(userName);
				Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
				window.setScene(BookMovieScene);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		else {
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

	public void confirm(ActionEvent event) {
		if (!selectedSeats.isEmpty()) {
			updateScreenTable();
			updateMovieTable();
			addBooking();
			FXMLLoader loader = new FXMLLoader();
			try {
				Pane ConfirmationPane = loader
						.load(getClass().getResource("/application/Confirmation.fxml").openStream());
				Scene ConfirmationScene = new Scene(ConfirmationPane);
				ConfirmationController confirmationController = (ConfirmationController) loader.getController();
				confirmationController.getUser(userName);
				confirmationController.getArray(movieArray);
				confirmationController.getSeats(seats);
				confirmationController.getID(ID);
				confirmationController.initConfirmation();
				Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
				window.setScene(ConfirmationScene);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else {
			lblMsg.setText("You must select seats!");
		}
	}
	
	private void updateScreenTable() {
		Button[] buttons = { a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4 }; //buttons array
		for (int i = 0; i < seating.length; i++) { //for each element of seating char array
			if (buttons[i].getStyle().equals("-fx-background-color: red;")) { //if corresponding seat is red
				seating[i] = 'Y'; //element set to character 'Y' which corresponds to booked
			}
		}
		try {
			ScreenModel.updateScreen(Integer.valueOf(movieArray[0]), seating);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateMovieTable() {
		int availableSeats = Integer.valueOf(movieArray[6] )- selectedSeats.size();
		ScreenModel.updateMovie(Integer.valueOf(movieArray[0]), availableSeats);
	}
	
	private void addBooking() {
		ScreenModel.addBooking(Integer.valueOf(ID), Integer.valueOf(movieArray[0]), seats);
	}


}
