package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

	private String userName, ID;
	
	private ArrayList<String[]> results;

	public ObservableList<Movie> data = FXCollections.observableArrayList();

	public void getUser(String user) {
		userName = user;
	}
	
	public void getID(String ID) {
		this.ID = ID;	
	}

	public void Back(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		Pane customerPane;
		try {
			customerPane = loader.load(getClass().getResource("/application/Customer.fxml").openStream());
			CustomerController customerController = (CustomerController) loader.getController();
			customerController.getUser(userName);
			customerController.getID(ID);
			Scene customerScene = new Scene(customerPane);
			// This line gets the Stage information
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setScene(customerScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureDatePicker();
		configureTableView();
	}
	
	private void configureDatePicker() {
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color : #ffc0cb;");
						}
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);
		datePicker.setValue(LocalDate.now());
	}
	
	private void configureTableView() {
		TableColumn viewCol = new TableColumn("View");	
		tableResults.getColumns().add(viewCol); //adds new column which will contain Buttons
		viewCol.setMaxWidth(70);
		viewCol.setResizable(false);
		
		title.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
		date.setCellValueFactory(new PropertyValueFactory<Movie, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Movie, String>("time"));
		bSeats.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("bSeats")); // booked seats - calls getBSeats																			
		aSeats.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("aSeats")); //available seats

		tableResults.setVisible(false);

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
										Movie movie = getTableView().getItems().get(getIndex());
										for (int i = 0; i < results.size(); i++) {
											String[] arr = results.get(i);
											if (arr[1].equals(movie.getTitle()) & arr[2].equals(movie.getDate()) & arr[3].equals(movie.getTime()) ) {
												//System.out.print(Arrays.toString(arr) + "from updateItem");
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
	
	public void searchMovie() throws SQLException {
		tableResults.getItems().clear(); //clears table view whenever a new search is made
		String date = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			date = datePicker.getValue().format(formatter).toString();
		} catch (Exception e) {
			date = ""; //catches NullPointerException if date picker is empty
			System.out.println(e);
		} finally {
			results = SearchMovieModel.searchMovie(txtTitle.getText(), date); //searches the database based on two parameters
		}

		tableResults.setVisible(true); 
		if (results.isEmpty()) {
			tableResults.setPlaceholder(new Label("No movies showing on that day!"));
		}

		for (int i = 0; i < results.size(); i++) {
			String[] arr = results.get(i);
			Movie name = new Movie(arr[1], arr[2], arr[3], 12 - Integer.valueOf(arr[6]), Integer.valueOf(arr[6]));
			data.add(name);
		}
		tableResults.setItems(data);
	}
	
	private void launchScreen(ActionEvent event, String[] movieArr) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane screenPane = loader.load(getClass().getResource("/application/Screen.fxml").openStream());
		Scene screenScene = new Scene(screenPane);
		ScreenController screenController = (ScreenController) loader.getController();
		screenController.getUser(userName);
		screenController.getArray(movieArr);
		screenController.getRole("customer");
		screenController.getID(ID);
		screenController.initScreen();
		// This line gets the Stage information
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(screenScene);
	}

}
