package customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Movie;
import application.ScreenController;
import application.User;
import employee.SearchMovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BookMovieController implements Initializable {
	@FXML
	private Button btnBack;

	@FXML
	private TextField txtTitle;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TableView<Movie> tableResults;

	@FXML
	private TableColumn<Movie, String> title;

	@FXML
	private TableColumn<Movie, String> date;

	@FXML
	private TableColumn<Movie, String> time;

	@FXML
	private TableColumn<Movie, Integer> bSeats, aSeats;

	private SearchMovieModel searchMovieModel = new SearchMovieModel();
	private User user;
	private ArrayList<String[]> results;
	public ObservableList<Movie> data = FXCollections.observableArrayList();

	/**
	 * Called to initialize the Book Movie Controller after its root element has
	 * been completely processed. Calls methods to configure the date picker and
	 * table view.
	 * 
	 * @author Aakash
	 * @param location
	 *            The location used to resolve relative paths for the root
	 *            object, or null if the location is not known.
	 * @param resources
	 *            The resources used to localize the root object, or null if the
	 *            object was not localized.
	 * @see initialize
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureDatePicker();
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
	 * Configures the date picker so that it initializes on tomorrrow's date.
	 * All date's prior to today are disabled so that a user can only book movie
	 * showing that have not occured.
	 * 
	 * @author Aakash
	 * @see DatePicker
	 */
	private void configureDatePicker() {
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						// disable dates before today
						if (item.isBefore(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color : #ffc0cb;");
						}
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);
	}

	/**
	 * Configures the table view. A column is added in which cells contain a
	 * button that will allow the user to view the screen seating for that
	 * particular movie.
	 * 
	 * @author Aakash
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void configureTableView() {
		// add a new column which will contain a Button in the cell
		TableColumn viewCol = new TableColumn("Book");
		tableResults.getColumns().add(viewCol);
		viewCol.setPrefWidth(73);
		viewCol.setMaxWidth(73);
		viewCol.setResizable(false);

		// specify a cell factory for each column using references to the
		// corresponding methods of the Movie class i.e. "title" to getTitle()
		title.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
		date.setCellValueFactory(new PropertyValueFactory<Movie, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Movie, String>("time"));
		aSeats.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("aSeats")); 

		tableResults.setVisible(false);

		// Define the table column which contains table cells that contain a
		// button.
		Callback<TableColumn<Movie, String>, TableCell<Movie, String>> cellFactory = //
				new Callback<TableColumn<Movie, String>, TableCell<Movie, String>>() {
					@Override
					public TableCell call(final TableColumn<Movie, String> param) {
						final TableCell<Movie, String> cell = new TableCell<Movie, String>() {

							final Button btn = new Button("Book");

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
											// found, the Screen view can be
											// launched passing the array as a
											// parameter
											if (arr[1].equals(movie.getTitle()) & arr[2].equals(movie.getDate())
													& arr[3].equals(movie.getTime())) {
												try {
													launchScreen(event, arr);
												} catch (IOException e) {
													e.printStackTrace();
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
	 * Search for movies showing based on the title and date. The user can
	 * search by both title and date, by either title or date, or get all
	 * showings but will only be shown future movie showings.
	 * 
	 * @author Aakash
	 * @throws SQLException
	 */
	public void searchMovie() throws SQLException {
		// clear table view whenever a new search is made
		tableResults.getItems().clear();
		String date = null;
		try {
			// get Date entered in traditional German format (used in UK)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			date = datePicker.getValue().format(formatter).toString();
		} catch (Exception e) {
			// catch NullPointerException if date picker is empty
			date = "";
			System.out.println(e);
		} finally {
			// search the database with two parameters
			results = searchMovieModel.searchMovie(txtTitle.getText(), date);
		}

		tableResults.setVisible(true);
		if (results.isEmpty()) {
			tableResults.setPlaceholder(new Label("No movies showing on that day!"));
		}

		// cycle through results array list. For each array, use elements to
		// create a Movie object which is added as a row to the table
		for (int i = 0; i < results.size(); i++) {
			String[] arr = results.get(i);
			String arrTitle = arr[1];
			String arrDate = arr[2];
			String arrTime = arr[3];
			int bookedSeats = 12 - Integer.valueOf(arr[6]);
			int availableSeats = Integer.valueOf(arr[6]);

			// create LocalDateTime object
			String startTime = arrTime.split("-")[0];
			String str = arrDate + " " + startTime;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
			
			// if no seats are available, continue to next loop
			if (bookedSeats == 12) {
				continue;
			}

			// if date + time of the movie is before now, it is not displayed.
			// Ensures that a user cannot book a movie that has already showed.
			if (!dateTime.isBefore(LocalDateTime.now())) {
				Movie name = new Movie(arrTitle, arrDate, arrTime, bookedSeats, availableSeats);
				data.add(name);
			}
		}
		tableResults.setItems(data);
	}

	/**
	 * Launches the launch screen view. Creates new pane by loading launch
	 * screen fxml, and a new scene from the pane. Gets the stage information
	 * from the event source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after logout button is pressed
	 * @param movieArr
	 *            array containing the movie's information held in the database
	 * @throws IOException
	 */
	private void launchScreen(ActionEvent event, String[] movieArr) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane screenPane = loader.load(getClass().getResource("/application/Screen.fxml").openStream());
		Scene screenScene = new Scene(screenPane);
		ScreenController screenController = (ScreenController) loader.getController();
		// pass parameters username, moviArr, role and ID.
		screenController.getUser(user);
		screenController.getArray(movieArr);
		// Initialize screen view which uses the above parameters. Initialize
		// cannot be used because it is called when the fxml is loaded (before
		// the parameters are passed)
		screenController.initScreen();
		// This line gets the Stage information
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(screenScene);
	}
}
