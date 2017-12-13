package application;

import java.io.IOException;
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
	
	public ObservableList<Movie> data = FXCollections.observableArrayList();
	
	private ArrayList<String[]> results;
	
	private String userName, ID;
	private char[] seating;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	public void getUser(String userName) {
		this.userName = userName;
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

	public void getBookingHistory() {
		configureTableView();	
		results = ManageBookingsModel.getBookingHistory(Integer.valueOf(ID));
		
		if (results.isEmpty()) {
			tableResults.setPlaceholder(new Label("You have made no bookings!"));
		}
		
		for (int i = 0; i < results.size(); i++) {
			String[] arr = results.get(i);
			System.out.println(Arrays.toString(arr));
			Movie name = new Movie(arr[3], arr[4], arr[5], arr[2]);
			data.add(name);
		}
		tableResults.setItems(data);
	}
	
	private void configureTableView() {
		TableColumn viewCol = new TableColumn("Delete");	
		tableResults.getColumns().add(viewCol); //adds new column which will contain Buttons
		viewCol.setMaxWidth(70);
		viewCol.setResizable(false);
		
		title.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
		date.setCellValueFactory(new PropertyValueFactory<Movie, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Movie, String>("time"));
		seats.setCellValueFactory(new PropertyValueFactory<Movie, String>("seats"));

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
										Movie movie = getTableView().getItems().get(getIndex());
										
										for (int i = 0; i < results.size(); i++) {
											String[] arr = results.get(i);
											if (arr[3].equals(movie.getTitle()) & arr[4].equals(movie.getDate()) & arr[5].equals(movie.getTime()) & arr[2].equals(movie.getSeats()) ) {

												if (movieisFutureBooking(arr[4], arr[5])) {
													try {
														seating = ScreenModel.checkScreen(Integer.valueOf(arr[1]));
														System.out.print(Arrays.toString(seating));
														
														String[] screenSeats = { "A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4" };
														String[] cancelSeats = arr[2].split(",");
														for (int j = 0; j < cancelSeats.length; j++) {
															cancelSeats[j] = cancelSeats[j].replace(" ", "");
														}

														for (int j = 0; j < cancelSeats.length; j++) {
															for (int k = 0; k < screenSeats.length; k++) {
																if (cancelSeats[j].equals(screenSeats[k])) {
																	seating[k] = 'N';
																}
															}
														}	
														
														ScreenModel.updateScreen(Integer.valueOf(arr[1]), seating);
														
													} catch (SQLException e) {
														e.printStackTrace();
													}
													
													lblMsg.setText("");
													
													//delete the booking from the bookings table
													ManageBookingsModel.deleteBooking(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]), arr[2]);

													//update the number of seats based on cancellation
													int noSeatsCancelled = arr[2].split(",").length; //number of seats freed up
													ManageBookingsModel.updateMovieSeats(Integer.valueOf(arr[1]), Integer.valueOf(arr[6]) + noSeatsCancelled);
													
													//remove the booking from the table view
													// is it better to just have the above method again? i.e. rerun the sql statement
													data.remove(i);
													results.remove(i);
													tableResults.setItems(data);
													
													if (results.isEmpty()) {
														tableResults.setPlaceholder(new Label("You have made no bookings!"));
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
	
	private boolean movieisFutureBooking(String date, String time) {
		String[] timeArr = time.split("-");
		String str = date + " " + timeArr[0];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
		
		return LocalDateTime.now().isBefore(dateTime);
	}
}
