package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
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

public class SearchMovieController implements Initializable {

	@FXML
	private Button btnCancel, btnExport;

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

	private String userName, fileName;
	private ArrayList<String[]> results;
	public ObservableList<Movie> data = FXCollections.observableArrayList();

	/**
	 * Called to initialize the Search Movie Controller after its root element
	 * has been completely processed. Calls methods that configure the date
	 * picker and table view.
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		configureDatePicker();
		configureTableView();
		btnExport.setVisible(false); // export is only visible once a search has
										// been made
	}

	/**
	 * Gets username of user as a passed parameter from previous class.
	 * 
	 * @author Aakash
	 * @param user
	 *            the username of the user
	 */
	public void getUser(String user) {
		userName = user;
	}

	/**
	 * Launches the employee view. Creates new pane by loading login fxml, and a
	 * new scene from the pane. Gets the stage information from the event source
	 * and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after cancel button is pressed
	 * @throws IOException
	 */
	public void Cancel(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane EmployeePane = loader.load(getClass().getResource("/application/Employee.fxml").openStream());
			Scene EmployeeScene = new Scene(EmployeePane);
			EmployeeController employeeController = (EmployeeController) loader.getController();
			employeeController.getUser(userName);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setScene(EmployeeScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configures the date picker so that it initializes on current date and the
	 * employee is able to search for movies in the past and future.
	 * 
	 * @author Aakash
	 * @see DatePicker
	 */
	private void configureDatePicker() {
		datePicker.setValue(LocalDate.now());
	}

	/**
	 * Search for movies showing based on the title and date. The user can
	 * search by both title and date, by either title or date, or get all
	 * previous showings.
	 * 
	 * @author Aakash
	 * @throws SQLException
	 */
	public void searchMovie() throws SQLException {
		// clear table view whenever a new search is made
		tableResults.getItems().clear();
		String date = null;
		try {
			// get date entered in traditional German format (what UK uses)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			date = datePicker.getValue().format(formatter).toString();
			// catch NullPointerException if date picker is empty, set date to
			// ""
		} catch (Exception e) {
			date = "";
			System.out.println(e);
		} finally {
			// search the database with two parameters
			results = SearchMovieModel.searchMovie(txtTitle.getText(), date);
		}

		// table view and export button become visible after a search is made
		tableResults.setVisible(true);
		btnExport.setVisible(true);

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

			// create Movie object and add as row
			Movie entry = new Movie(arrTitle, arrDate, arrTime, bookedSeats, availableSeats);
			data.add(entry);
		}
		tableResults.setItems(data);
	}

	/**
	 * Configures the table view. A column is added in which cells contain a
	 * button that will allow the user to view the screen seating for that
	 * particular movie
	 * 
	 * @author Aakash
	 */
	private void configureTableView() {
		// add new column that will contain a button in the cell
		TableColumn viewCol = new TableColumn("View");
		tableResults.getColumns().add(viewCol);
		viewCol.setMaxWidth(70);
		viewCol.setResizable(false);

		title.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
		date.setCellValueFactory(new PropertyValueFactory<Movie, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Movie, String>("time"));
		// calls method getBSeats()
		bSeats.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("bSeats"));
		aSeats.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("aSeats"));

		tableResults.setVisible(false);

		Callback<TableColumn<Movie, String>, TableCell<Movie, String>> cellFactory = //
				new Callback<TableColumn<Movie, String>, TableCell<Movie, String>>() {
					@Override
					public TableCell call(final TableColumn<Movie, String> param) {
						final TableCell<Movie, String> cell = new TableCell<Movie, String>() {

							final Button btn = new Button("View");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										Movie movie = getTableView().getItems().get(getIndex());
										for (int i = 0; i < results.size(); i++) {
											String[] arr = results.get(i);
											if (arr[1].equals(movie.getTitle()) & arr[2].equals(movie.getDate())
													& arr[3].equals(movie.getTime())) {
												// System.out.print(Arrays.toString(arr)
												// + "from updateItem");
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
	 * Creates a new text file named by the local date and time in the
	 * ExportFiles folder and adds the information that is present in the table,
	 * including title, date, time, and number of booked and available seats.
	 * 
	 * @author Aakash
	 * @throws IOException
	 */
	public void exportFile() {

		try {
			fileName = "";

			if (txtTitle.getText().isEmpty()) {
				fileName = "Showings on " + datePicker.getValue().toString();
			}

			else if (!txtTitle.getText().isEmpty() && !datePicker.getValue().toString().isEmpty()) {
				fileName = txtTitle.getText() + " showings on " + datePicker.getValue().toString();
			}
		} catch (Exception e1) {
			fileName = txtTitle.getText() + " showings";
			e1.printStackTrace();
		}
		System.out.print(LocalDateTime.now().toString()); // delet this
		try {
			// File file = new File(
			// "ExportFiles/" + LocalDateTime.now().toString().replaceAll("/",
			// ".").replaceAll(":", ".") + ".txt");
			File file = new File("ExportFiles/" + fileName + ".txt");
			file.getParentFile().mkdirs();
			if (!file.exists()) {
				file.createNewFile();
			}

			PrintWriter pw = new PrintWriter(file);

			if (results.size() == 0) {
				pw.println("No results");
			}

			for (int i = 0; i < results.size(); i++) {
				String[] arr = results.get(i);
				pw.println(Arrays.toString(arr).replace("[", "").replace("]", ""));
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void launchScreen(ActionEvent event, String[] movieArr) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane screenPane = loader.load(getClass().getResource("/application/Screen.fxml").openStream());
		Scene screenScene = new Scene(screenPane);
		ScreenController screenController = (ScreenController) loader.getController();
		screenController.getUser(userName);
		screenController.getArray(movieArr);
		screenController.getRole("employee");
		screenController.initScreen();
		// This line gets the Stage information
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(screenScene);
	}

}
