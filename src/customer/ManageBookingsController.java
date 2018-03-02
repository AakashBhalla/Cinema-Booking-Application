package customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Movie;
import application.ScreenModel;
import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ManageBookingsController implements Initializable {

	@FXML
	private TableView<Movie> tableResults;

	@FXML
	private TableColumn<Movie, String> title;

	@FXML
	private TableColumn<Movie, String> date;

	@FXML
	private TableColumn<Movie, String> time;

	@FXML
	private TableColumn<Movie, String> seats;

	@FXML
	private Label lblMsg;

	private ManageBookingsModel manageBookingsModel = new ManageBookingsModel();
	private ScreenModel screenModel = new ScreenModel();
	public ObservableList<Movie> data = FXCollections.observableArrayList();
	private ArrayList<String[]> results;
	private User user;
	private char[] seating;

	/**
	 * Called to initialize the Add Movie Controller after its root element has
	 * been completely processed. Configures the table view.
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
		configureTableView();
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
	 * Launches the customer view. Creates new pane by loading employee fxml,
	 * and a new scene from the pane. Gets the stage information from the event
	 * source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after back button is pressed
	 */
	public void Back(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		Pane customerPane;
		try {
			customerPane = loader.load(getClass().getResource("/customer/Customer.fxml").openStream());
			CustomerController customerController = (CustomerController) loader.getController();
			customerController.getUser(user);
			Scene customerScene = new Scene(customerPane);
			// This line gets the Stage information
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setScene(customerScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the booking history of the customer and displays them in the table
	 * view.
	 * 
	 * @author Aakash
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public void getBookingHistory() throws NumberFormatException, SQLException {
		results = manageBookingsModel.getBookingHistory(Integer.valueOf(user.getID()));

		if (results.isEmpty()) {
			tableResults.setPlaceholder(new Label("You have made no bookings!"));
		}

		// cycle through the arrays in Array list, create new movie objects and
		// add to the observable array list
		for (int i = 0; i < results.size(); i++) {
			String[] arr = results.get(i);
			Movie name = new Movie(arr[3], arr[4], arr[5], arr[2]);
			data.add(name);
		}
		tableResults.setItems(data);
	}

	/**
	 * Configures the table view. A column is added in which cells contain a
	 * button that will allow the user to delete the booking for that particular
	 * movie.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void configureTableView() {
		// add a new column that will contain a button in that cell
		TableColumn viewCol = new TableColumn("Delete Booking");
		tableResults.getColumns().add(viewCol);
		viewCol.setPrefWidth(98);
		viewCol.setMaxWidth(98);
		viewCol.setResizable(false);

		// specify a cell factory for each column using references to the
		// corresponding methods of the Movie class i.e. "title" to getTitle()
		title.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
		date.setCellValueFactory(new PropertyValueFactory<Movie, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Movie, String>("time"));
		seats.setCellValueFactory(new PropertyValueFactory<Movie, String>("seats"));

		// Define the table column which contains table cells that contain a
		// button
		Callback<TableColumn<Movie, String>, TableCell<Movie, String>> cellFactory = //
				new Callback<TableColumn<Movie, String>, TableCell<Movie, String>>() {
					@Override
					public TableCell call(final TableColumn<Movie, String> param) {
						final TableCell<Movie, String> cell = new TableCell<Movie, String>() {

							final Button btn = new Button("Delete");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										// when the button is pressed it creates
										// a Movie object based on the row
										// clicked
										Movie movie = getTableView().getItems().get(getIndex());
										// cycle through the arrays within the
										// results observable array list
										for (int i = 0; i < results.size(); i++) {
											String[] arr = results.get(i);
											// once the corresponding array is
											// found, the booking can be
											// deleted.
											if (arr[3].equals(movie.getTitle()) & arr[4].equals(movie.getDate())
													& arr[5].equals(movie.getTime())
													& arr[2].equals(movie.getSeats())) {

												// booking can only be deleted
												// if it is in the future
												if (movieisFutureBooking(arr[4], arr[5])) {
													try {
														// get the screen
														// seating availability
														// as an array
														seating = screenModel.checkScreen(Integer.valueOf(arr[1]));

														// Define two string
														// arrays. One for the
														// seats. One for the
														// seats being
														// cancelled.
														String[] screenSeats = { "A1", "A2", "A3", "A4", "B1", "B2",
																"B3", "B4", "C1", "C2", "C3", "C4" };
														String[] cancelSeats = arr[2].split(",");
														// remove the white
														// space in the cancel
														// seats array
														for (int j = 0; j < cancelSeats.length; j++) {
															cancelSeats[j] = cancelSeats[j].replace(" ", "");
														}

														// cycle through both
														// arrays, if the
														// elements are equal,
														// set the corresponding
														// seating availability
														// array element as N
														// (not booked)
														for (int j = 0; j < cancelSeats.length; j++) {
															for (int k = 0; k < screenSeats.length; k++) {
																if (cancelSeats[j].equals(screenSeats[k])) {
																	seating[k] = 'N';
																}
															}
														}

														// update the screen
														// seating availability
														screenModel.updateScreen(Integer.valueOf(arr[1]), seating);

													} catch (SQLException e) {
														e.printStackTrace();
													}

													lblMsg.setText("");

													// delete the booking from
													// the bookings table
													try {
														manageBookingsModel.deleteBooking(Integer.valueOf(arr[0]),
																Integer.valueOf(arr[1]), arr[2]);
													} catch (NumberFormatException e1) {
														e1.printStackTrace();
													} catch (SQLException e1) {
														e1.printStackTrace();
													}

													// update the number of
													// seats based on
													// cancellation
													int noSeatsCancelled = arr[2].split(",").length;

													try {
														manageBookingsModel.updateMovieSeats(Integer.valueOf(arr[1]),
																Integer.valueOf(arr[6]) + noSeatsCancelled);
													} catch (NumberFormatException e) {
														e.printStackTrace();
													} catch (SQLException e) {
														e.printStackTrace();
													}

													// update the table view
													data.remove(i);
													results.remove(i);
													tableResults.setItems(data);

													if (results.isEmpty()) {
														tableResults.setPlaceholder(
																new Label("You have made no bookings!"));
													}
												}

				else {
													lblMsg.setText("This movie showing has already happened!");
												}
											}
										}
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		viewCol.setCellFactory(cellFactory);
	}

	/**
	 * Checks that the movie the user is trying to delete hasn't started or has
	 * shown. Creates a LocalDateTime object from the date and starting time and
	 * returns whether the showing is in the future.
	 * 
	 * @author Aakash
	 * @param date
	 *            the date of the movie in format dd/MM/yyyy
	 * @param time
	 *            the time range of the movie in format HH:mm-HH:mm
	 * @return boolean
	 */
	private boolean movieisFutureBooking(String date, String time) {
		// get starting time
		String[] timeArr = time.split("-");
		// create LocalDateTime object
		String str = date + " " + timeArr[0];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

		return LocalDateTime.now().isBefore(dateTime);
	}
}
