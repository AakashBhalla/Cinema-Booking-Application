package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SearchMovieController implements Initializable {
	
	@FXML
	private Button btnCancel;
	
	@FXML 
	private TextField txtTitle;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private Label labelResult;
	
	@FXML
	private TableView tableResults;
	
	private String userName;
	private ArrayList<String[]> results;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		configureDatePicker();
		configureTableView();
		
	}
	
	public void getUser(String user) {
		userName = user;
	}
	
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
	
	private void configureDatePicker() {
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore(LocalDate.now().plusDays(1))) {
							setDisable(true);
							setStyle("-fx-background-color : #ffc0cb;");
						}
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);
		datePicker.setValue(LocalDate.now().plusDays(1));
	}
	
	public void searchMovie() throws SQLException { 
		String date = null;
		try {
			 date = datePicker.getValue().toString();	
		} catch (Exception e) {
			 date = "";
			System.out.println(e);
		} finally {
			results = SearchMovieModel.searchMovie(txtTitle.getText(), date);
		}
		
		for (int i = 0; i < results.size(); i++) {
			System.out.println(Arrays.toString(results.get(i)));
		}
		
	}
	
	private void configureTableView() {
		
	}
	
}
