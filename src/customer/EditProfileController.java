package customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import application.Movie;
import application.User;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class EditProfileController implements Initializable {
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

	private User user;
	private EditProfileModel editProfileModel = new EditProfileModel();
	private String[] customerInfo;
	private ManageBookingsModel manageBookingsModel = new ManageBookingsModel();
	public ObservableList<Movie> data = FXCollections.observableArrayList();
	private ArrayList<String[]> results;

	/**
	 * Called to initialize the Edit Profile Controller after its root element
	 * has been completely processed. Sets listener to text fields so that if a
	 * change has been made, the update button becomes visible.
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
		btnUpdate.setVisible(false);
		for (TextField t : Arrays.asList(txtfName, txtlName, txtEmail)) {
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
	 * The textfields are populated by the information stored in the database.
	 * Called before the scene is set.
	 * 
	 * @author Aakash
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public void initEditProfile() throws NumberFormatException, SQLException {
		customerInfo = editProfileModel.getCustomerInfo(Integer.valueOf(user.getID()));
		txtfName.setText(customerInfo[0]);
		txtlName.setText(customerInfo[1]);
		txtEmail.setText(customerInfo[2]);
		getBookingHistory();
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
	 * Updates the information only if the first name, last name, and email
	 * fields aren't blank and the email address matches a general email address
	 * regex. Called when the user presses the update button.
	 * 
	 * @author Aakash
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public void updateInfo() throws NumberFormatException, SQLException {
		String firstName = txtfName.getText();
		String lastName = txtlName.getText();
		String email = txtEmail.getText();

		// if the fields are blank, reset them and display warning message
		if (firstName.isEmpty()) {
			txtfName.setText(customerInfo[0]);
			lblMsg.setText("Not updated - first name field cannot be blank.");
		}

		else if (lastName.isEmpty()) {
			txtlName.setText(customerInfo[1]);
			lblMsg.setText("Not updated - last name field cannot be blank.");
		}

		else if (email.isEmpty()) {
			txtEmail.setText(customerInfo[2]);
			lblMsg.setText("Not updated - email field cannot be blank.");
		}

		// if the email doesn't match regex for email addresses, display warning
		else if (!email.matches(
				"^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
			txtEmail.setText(customerInfo[2]);
			lblMsg.setText("Not updated - that is not a valid email address.");
		}

		else {
			// update info, make update button visible
			editProfileModel.updateInfo(Integer.valueOf(user.getID()), txtfName.getText(), txtlName.getText(),
					txtEmail.getText());
			btnUpdate.setVisible(false);
			btnBack.setText("Back");
			lblMsg.setText("Your profile has been updated");
		}
	}

	/**
	 * Configure the table view, get the booking history of the customer,
	 * display in the table view.
	 * 
	 * @author Aakash
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public void getBookingHistory() throws NumberFormatException, SQLException {
		configureTableView();
		// get booking history associated with customer ID
		results = manageBookingsModel.getBookingHistory(Integer.valueOf(user.getID()));

		if (results.isEmpty()) {
			tableResults.setPlaceholder(new Label("You have made no bookings!"));
		}

		// cycle through results, create movie objects and add to observable
		// array list
		for (int i = 0; i < results.size(); i++) {
			String[] arr = results.get(i);
			Movie name = new Movie(arr[3], arr[4], arr[5], arr[2]);
			data.add(name);
		}
		tableResults.setItems(data);
	}

	/** Configures the table view.
	 * @author Aakash */
	private void configureTableView() {
		// specify a cell factory for each column using references to the
		// corresponding methods of the Movie class i.e. "title" to getTitle()
		title.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
		date.setCellValueFactory(new PropertyValueFactory<Movie, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Movie, String>("time"));
		seats.setCellValueFactory(new PropertyValueFactory<Movie, String>("seats"));
	}
}
