package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EditProfileController implements Initializable{
	
	private String userName, ID;
	
	@FXML
	private TextField txtfName, txtlName, txtEmail;
	
	@FXML
	private Button btnBack, btnUpdate;
	
	@FXML
	private Label lblMsg;
	
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
	
	public ObservableList<Movie> data = FXCollections.observableArrayList();
	
	private ArrayList<String[]> results;
	
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnUpdate.setVisible(false);
		
		for (TextField t: Arrays.asList(txtfName, txtlName, txtEmail)) {
			t.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent arg0) {
					lblMsg.setText("");
					btnBack.setText("Cancel");
					btnUpdate.setVisible(true);	
				}
				
			});
		}
	}
	
	public void initEditProfile() {
		String[] customerInfo = EditProfileModel.getCustomerInfo(Integer.valueOf(ID));
		txtfName.setText(customerInfo[0]);
		txtlName.setText(customerInfo[1]);
		txtEmail.setText(customerInfo[2]);
		
		getBookingHistory();
	}
	
	public void updateInfo() {
		EditProfileModel.updateInfo(Integer.valueOf(ID), txtfName.getText(), txtlName.getText(), txtEmail.getText());
		btnUpdate.setVisible(false);
		btnBack.setText("Back");
		lblMsg.setText("Your profile has been updated");	
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
		title.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
		date.setCellValueFactory(new PropertyValueFactory<Movie, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Movie, String>("time"));
		seats.setCellValueFactory(new PropertyValueFactory<Movie, String>("seats"));
	}
	
	
}
