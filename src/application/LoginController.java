package application;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

public class LoginController implements Initializable{
	public LoginModel loginModel = new LoginModel();
	private Boolean isOn=(Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK)) ? true: false;;
	
	@FXML
	private Label isConnected;
	
	@FXML
	private TextField txtUsername;
	
	@FXML
	private TextField txtPassword;
	
	@FXML 
	private Button btnLogin;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (loginModel.isDbConnected()) {
			isConnected.setText("Connected to Database");
			capsLockState();
		}
		else {
			isConnected.setText("Not connected to Database - contact Administrator");
		}		
	}
	
	public void capsLockState () {		
		txtUsername.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (isOn) {
					isConnected.setText("Warning - Caps Lock is on");
			}
				else if (!isOn) {
					isConnected.setText("");
				}
			}
			
		});
	
	txtUsername.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent keyEvent) {
	        if (keyEvent.getCode() == KeyCode.CAPS)  {
	        	if (isOn) {
	        		isConnected.setText("");
	        		isOn = false;
	        	}
	        	else if (!isOn) {
	        	isConnected.setText("Warning - Caps Lock is on");
	        	isOn = true;
	        	}
	        }
	    }
	});
	
	txtPassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			if (isOn) {
				isConnected.setText("Warning - Caps Lock is on");
		}
			else if (!isOn) {
				isConnected.setText("");
			}
		}
		
	});
	
	txtPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent keyEvent) {
	        if (keyEvent.getCode() == KeyCode.CAPS)  {
	        	if (isOn) {
	        		isConnected.setText("");
	        		isOn = false;
	        	}
	        	else if (!isOn) {
	        	isConnected.setText("Warning - Caps Lock is on");
	        	isOn = true;
	        	}
	        }
	    }
	});
	
	btnLogin.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    @Override
	    public void handle(KeyEvent keyEvent) {
	        if (keyEvent.getCode() == KeyCode.CAPS)  {
	        	if (isOn) {
	        		isConnected.setText("username and password is not correct");
	        		isOn = false;
	        	}
	        	else if (!isOn) {
	        	isConnected.setText("username and password is not correct");
	        	isOn = true;
	        	}
	        }
	    }
	});
	
	}
	
	public void Login (ActionEvent event) {
		try {
			if (loginModel.isLogin(txtUsername.getText(), txtPassword.getText())) {
				if (loginModel.roleLogin(txtUsername.getText(), txtPassword.getText()).equals("employee")) {
					launchEmployee(event);
				}
				if (loginModel.roleLogin(txtUsername.getText(), txtPassword.getText()).equals("customer")) {
					launchCustomer(event);
				}
				else {
					isConnected.setText("role not detected");
				}

			}
			else {
				isConnected.setText("username and password is not correct");
			}
		} catch (SQLException e) {
			isConnected.setText("username and password is not correct");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void launchCustomer(ActionEvent event) throws IOException {
		isConnected.setText("username and password is correct");
		FXMLLoader loader = new FXMLLoader();
		Pane customerPane = loader.load(getClass().getResource("/application/Customer.fxml").openStream());
		CustomerController customerController = (CustomerController)loader.getController(); 
		customerController.getUser(txtUsername.getText());
		Scene customerScene = new Scene(customerPane);
		//This line gets the Stage information
		Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
		window.setScene(customerScene);
	}

	private void launchEmployee(ActionEvent event) throws IOException {
		isConnected.setText("username and password is correct");
		FXMLLoader loader = new FXMLLoader();
		Pane employeePane = loader.load(getClass().getResource("/application/Employee.fxml").openStream());
		EmployeeController employeeController = (EmployeeController)loader.getController(); 
		employeeController.getUser(txtUsername.getText());
		Scene employeeScene = new Scene(employeePane);
		//This line gets the Stage information
		Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
		window.setScene(employeeScene);
	}

}
