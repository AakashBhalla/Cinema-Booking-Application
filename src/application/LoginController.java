package application;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	// instantiates LoginModel class
	private LoginModel loginModel = new LoginModel();
	// returns whether caps lock is on (true) or off(false)
	private Boolean isOn = (Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK));

	@FXML
	private Label isConnected;

	@FXML
	private TextField txtUsername, txtPassword;

	@FXML
	private Button btnLogin;

	/**
	 * Called to initialize the Login Controller after its root element
	 * has been completely processed. Checks whether the application is
	 * connected to the database and displays error message it is not.
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
		if (loginModel.isDbConnected()) {
			isConnected.setText("Enter your login credentials");
			capsLockState();
		} else {
			isConnected.setText("Not connected to Database - contact Administrator");
		}
	}

	/**
	 * Key Events are used to track whether caps lock state is changed. Mouse
	 * Events are used to display a warning message whenever a user clicks into
	 * a textfield and capslock is on.
	 * 
	 * @author Aakash
	 */
	public void capsLockState() {
		for (TextField t : Arrays.asList(txtUsername, txtPassword)) {
			t.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (isOn) {
						isConnected.setText("Warning - Caps lock is on");
					} else if (!isOn) {
						isConnected.setText("");
					}
				}
			});

			t.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent keyEvent) {
					if (keyEvent.getCode() == KeyCode.CAPS) {
						if (isOn) {
							isConnected.setText("");
							isOn = false;
						} else if (!isOn) {
							isConnected.setText("Warning - Caps lock is on");
							isOn = true;
						}
					}
				}
			});
		}

		// continues to track changes on whether capslock state if the
		// focus is on the button
		btnLogin.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.CAPS) {
					if (isOn) {
						isConnected.setText("");
						isOn = false;
					} else if (!isOn) {
						isConnected.setText("Warning - Caps lock is on");
						isOn = true;
					}
				}
			}
		});
	}

	/**
	 * First checks whether the login credentials are correct when the login
	 * button is pressed. If they are, detects the role of the user and calls
	 * methods to launch either the employee or customer view.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after login is pressed
	 * @throws SQLException
	 * @throws IOException
	 */
	public void Login(ActionEvent event) {
		try {
			String username = txtUsername.getText();
			String password = txtPassword.getText();
			// check whether username/password credentials are correct
			if (loginModel.isLogin(username, password)) {
				// get ID and role of user
				String[] roleLogin = (loginModel.roleLogin(username, password));
				String ID = roleLogin[0];
				String role = roleLogin[1];
				if (role.equals("employee")) {
					launchEmployee(event);
				}
				if (role.equals("customer")) {
					launchCustomer(event, ID);
				} else {
					// if database does not have role entered, nothing will be
					// launched
					isConnected.setText("role not detected");
				}

			} else {
				isConnected.setText("username and/or password is not correct");
			}
		} catch (SQLException e) {
			isConnected.setText("username and/or password is not correct");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launches the customer view of the application. Creates new pane by
	 * loading customer fxml, and a new scene from the pane. Gets the stage
	 * information from the event source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after login button is pressed
	 * @param ID
	 *            ID that is associated with the customer and used for bookings
	 * @throws IOException
	 */
	private void launchCustomer(ActionEvent event, String ID) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane customerPane = loader.load(getClass().getResource("/application/Customer.fxml").openStream());
		CustomerController customerController = (CustomerController) loader.getController();
		// passes parameters username and ID
		customerController.getUser(txtUsername.getText());
		customerController.getID(ID);
		Scene customerScene = new Scene(customerPane);
		// This line gets the Stage information from the event source
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(customerScene);
	}

	/**
	 * Launches the employee view. Creates new pane by loading employee fxml,
	 * and a new scene from the pane. Gets the stage information from the event
	 * source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after login button is pressed
	 * 
	 * @throws IOException
	 */
	private void launchEmployee(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane employeePane = loader.load(getClass().getResource("/application/Employee.fxml").openStream());
		EmployeeController employeeController = (EmployeeController) loader.getController();
		// pass parameter username
		employeeController.getUser(txtUsername.getText());
		Scene employeeScene = new Scene(employeePane);
		// This line gets the Stage information from the event source
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		window.setScene(employeeScene);
	}
}
