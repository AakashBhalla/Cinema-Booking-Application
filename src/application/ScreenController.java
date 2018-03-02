package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

import customer.BookMovieController;
import customer.ConfirmationController;
import employee.SearchMovieController;
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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ScreenController implements Initializable {
	@FXML
	private Label txtTitle, txtDate, txtTime, txtaSeats, txtbSeats, lblMsg, lblBookedSeats, lblInstructions,
			lblInstructions1;

	@FXML
	private TextArea txtDescription;

	@FXML
	private ImageView img;

	@FXML
	private Button a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4, btnConfirm;

	private ScreenModel screenModel = new ScreenModel();
	private User user;
	private String[] movieArray;
	private char[] seating;
	private String seats = "";
	private ArrayList<String> selectedSeats = new ArrayList<String>();

	/**
	 * Called to initialize the Search Movie Controller after its root element
	 * has been completely processed.
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
	 * Gets array of movie information as a passed parameter from previous
	 * class.
	 * 
	 * @author Aakash
	 * @param movieArray
	 *            the movie information
	 */
	public void getArray(String[] movieArray) {
		this.movieArray = movieArray;
	}

	/**
	 * Initializes the screen page. This method is called before the scene is
	 * set. The code cannot be included in initialize as the data parameters are
	 * not available when the FXML is loaded.
	 * 
	 * The screen seating availability is checked and the graphical display is
	 * set up to indicate whether the seat is available or not. The text for
	 * movie information fields is set. If the user is a customer, makes changes
	 * to the view.
	 * 
	 * @author Aakash
	 * @throws NumberFormatException
	 */
	public void initScreen() {
		try {
			// get seating availability for particular movie (movie ID)
			seating = screenModel.checkScreen(Integer.valueOf(movieArray[0]));
			// initialize the buttons array
			Button[] buttons = { a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4 };
			// cycle through the seating availability array. If it contains an
			// N, the seat is not booked and is available
			// if it contains Y the icon is changed to indicate that it is not
			// available.
			for (int i = 0; i < seating.length; i++) {
				if (seating[i] == 'N') {
					buttons[i].setStyle("-fx-background-color: green;");
				} else {
					// seat not available
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
		// initialize the movie information e.g. title, date etc
		txtTitle.setText(movieArray[1]);
		txtDate.setText(movieArray[2]);
		txtTime.setText(movieArray[3]);
		txtDescription.setWrapText(true);
		txtDescription.setText(movieArray[5]);
		txtaSeats.setText(movieArray[6]);
		txtbSeats.setText(String.valueOf(12 - Integer.valueOf(movieArray[6])));

		// display image from URL
		try {
			Image image = new Image(movieArray[4]);
			img.setImage(image);
		} catch (Exception e) {
			System.out.print(e + "Invalid URL: Invalid URL or resource not found");
			e.printStackTrace();
		}

		// if the user is a customer, makes changes in the customerView method
		if (user.getRole().equals("customer")) {
			customerView();
		}
	}

	/**
	 * Alters the view if the user is a customer. The user cannot see the number
	 * of booked seats for a movie as this is only relevant to the employee. The
	 * user can click on the seat buttons to select seats. This updates a label
	 * displaying the selected seats. If a seat that has already been booked is
	 * clicked, error message is displayed.
	 * 
	 * @author Aakash
	 */
	private void customerView() {
		// hides booked seats information - this is only relevant to the
		// employee. Make confirmation button visible.
		lblMsg.setText("");
		lblBookedSeats.setVisible(false);
		txtbSeats.setVisible(false);
		lblInstructions.setVisible(true);
		lblInstructions.setWrapText(true);
		lblInstructions.setText("Seats can be booked on");
		lblInstructions1.setVisible(true);
		lblInstructions1.setWrapText(true);
		lblInstructions1.setText("the screen tab");
		btnConfirm.setVisible(true);

		// for each seat button, an event listener is set up
		for (Button b : Arrays.asList(a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4)) {
			b.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					// if the seat is available (i.e. style is green), style is
					// set to yellow, the seat name is added to the selected seats
					// ArrayList and updates label displaying selected seats
					if (b.getStyle().equals("-fx-background-color: green;")) {
						b.setStyle("-fx-background-color: yellow;");
						selectedSeats.add(b.getId().toUpperCase());
						updateLabel(selectedSeats, false);
						// if the seat has been selected and is not unseleced,
						// sets style to green and removed selected seat from
						// ArrayList and label
					} else if (b.getStyle().equals("-fx-background-color: yellow;")) {
						b.setStyle("-fx-background-color: green;");
						selectedSeats.remove(b.getId().toUpperCase());
						updateLabel(selectedSeats, false);
					} else {
						// if the seat that has been selected is taken, display
						// error message
						updateLabel(selectedSeats, true);
					}
				}
			});
		}
	}

	/**
	 * Updates the label when the user is selecting seats. Displays the seats in
	 * alphanumeric order. If the seat selected is taken, displays an error
	 * message.
	 * 
	 * @author Aakash
	 * @param selectedSeats
	 *            an ArrayList containing the seats that have been selected
	 * @param isError
	 *            true if a booked seat was selected
	 */
	private void updateLabel(ArrayList<String> selectedSeats, boolean isError) {
		// sort the selected seats ArrayList by alphanumeric
		Collections.sort(selectedSeats);

		String outputMsg = "";
		seats = "";

		// define the String that contains the seats selected
		if (selectedSeats.isEmpty()) {
			outputMsg = "No seats selected.";
		}

		else {
			// cycle through the ArrayList and append each seat with commas. If
			// it's the final element of the list, a comma is not added
			for (int i = 0; i < selectedSeats.size(); i++) {
				if (i == (selectedSeats.size() - 1)) {
					seats = seats + selectedSeats.get(i);
				} else {
					seats = seats + selectedSeats.get(i) + ", ";
				}
			}
			outputMsg = "Selected seats: " + seats;
		}

		// if the seat selected is already taken, displays error message.
		if (isError) {
			outputMsg = "That seat is taken. " + outputMsg;
		}
		lblMsg.setText(outputMsg);
	}

	/**
	 * Launches the view Book Movie if the user is a customer, or searchMovie if
	 * the user is an employee. Creates new pane by loading either BookMovie or
	 * SearchMovie fxml, and a new scene from the pane. Gets the stage
	 * information from the event source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after back button is pressed
	 */
	public void back(ActionEvent event) {
		if (user.getRole().equals("customer")) {
			FXMLLoader loader = new FXMLLoader();
			try {
				Pane BookMoviePane = loader.load(getClass().getResource("/customer/BookMovie.fxml").openStream());
				Scene BookMovieScene = new Scene(BookMoviePane);
				BookMovieController bookMovieController = (BookMovieController) loader.getController();
				// if the role is a customer, pass ID too.
				bookMovieController.getUser(user);
				Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
				window.setScene(BookMovieScene);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else {
			FXMLLoader loader = new FXMLLoader();
			try {
				Pane SearchMoviePane = loader
						.load(getClass().getResource("/employee/SearchMovie.fxml").openStream());
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

	/**
	 * Launches the Confirmation view if at least one seat has been selected.
	 * Calls methods to update the screen, movies and booking table of the
	 * database. Creates new pane by loading Confirmation fxml, and a new scene
	 * from the pane. Gets the stage information from the event source and sets
	 * the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after confirmation button is pressed
	 */
	public void confirm(ActionEvent event) {
		if (!selectedSeats.isEmpty()) {
			updateScreenTable();
			updateMovieTable();
			addBooking();
			FXMLLoader loader = new FXMLLoader();
			try {
				Pane ConfirmationPane = loader
						.load(getClass().getResource("/customer/Confirmation.fxml").openStream());
				Scene ConfirmationScene = new Scene(ConfirmationPane);
				ConfirmationController confirmationController = (ConfirmationController) loader.getController();
				confirmationController.getUser(user);
				confirmationController.getArray(movieArray);
				confirmationController.getSeats(seats);
				// method needed to initialize the labels with the parameters
				// passed above
				confirmationController.initConfirmation();
				Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
				window.setScene(ConfirmationScene);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			lblMsg.setText("You must select seats!");
		}
	}

	/**
	 * Updates the screen seating availability array where N indicates not
	 * booked and Y indicates booked. Sets the selected seats to Y and updates
	 * the screens table of the database.
	 * 
	 * @author Aakash
	 * @throws NumberFormatException
	 */
	private void updateScreenTable() {
		// initialze button array
		Button[] buttons = { a1, a2, a3, a4, b1, b2, b3, b4, c1, c2, c3, c4 };
		// for each element of the seating availability array
		for (int i = 0; i < seating.length; i++) {
			// if the corresponding seat is yellow (i.e. selected to be booked)
			if (buttons[i].getStyle().equals("-fx-background-color: yellow;")) {
				// set the seating availability array element to 'Y' which
				// corresponds to booked
				seating[i] = 'Y';
			}
		}
		try {
			// update the screen availability data in the screens table of the
			// database using movie ID and the seating array
			screenModel.updateScreen(Integer.valueOf(movieArray[0]), seating);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the number of seats available in the movies table of the database
	 * by subtracting the number of seats available
	 * 
	 * @author Aakash
	 */
	private void updateMovieTable() {
		int availableSeats = Integer.valueOf(movieArray[6]) - selectedSeats.size();
		screenModel.updateMovie(Integer.valueOf(movieArray[0]), availableSeats);
	}

	/**
	 * Add the booking to the bookings table of the database with information
	 * customer ID, movie ID, and selected seats
	 * 
	 * @author Aakash
	 */
	private void addBooking() {
		screenModel.addBooking(Integer.valueOf(user.getID()), Integer.valueOf(movieArray[0]), seats);
	}

}
