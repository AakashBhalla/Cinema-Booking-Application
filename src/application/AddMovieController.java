package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AddMovieController implements Initializable {

	@FXML
	private TextField txtTitle, txt1, txt2, txt3, txt4, txtImg;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TextArea txtDescription;

	@FXML
	private Button btnCancel, btnSubmit;

	@FXML
	private Label lblMsg;

	private Integer[] arr;
	private String userName;
	private final int noSeats = 12;
	private int startHr, startMin, endHr, endMin;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		configureDatePicker();
		timeControl();
	}

	public void getUser(String user) {
		userName = user;
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

	//TODO: issue when backspace is pressed - fixed?
	public void timeControl() { 
		final int maxLength = 2;
		for (TextField b : Arrays.asList(txt1, txt2, txt3, txt4)) {

			b.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d{0,9}")) {
						b.setText(oldValue);
					}
					if (b.getText().length() > maxLength) {
						String s = b.getText().substring(0, maxLength);
						b.setText(s);
					}
					
					if (txt1.getText().length() == maxLength) {
						txt2.requestFocus();
						if (txt2.getText().length() == maxLength) {
							txt3.requestFocus();
							if (txt3.getText().length() == maxLength) {
								txt4.requestFocus();
								if (txt4.getText().length() == maxLength) {
									txtImg.requestFocus();
								}
							}
						}
					}
				}
			});
			
		}
	}

	public void Submit(ActionEvent event) throws SQLException {
		if (fieldsValid()) {
			lblMsg.setText("Movie added!");
			AddMovieModel.insertMovie(txtTitle.getText(), datePicker.getValue().toString(), timeString(),
					txtImg.getText(), txtDescription.getText(), noSeats);
		}
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

	private boolean fieldsValid() {
		if (txtTitle.getText().isEmpty()) {
			txtTitle.requestFocus();
			txtTitle.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter a Movie title");

			txtTitle.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent keyEvent) {
					Boolean isAlphanumeric = txtTitle.getText().matches("[0-9A-Za-z]");
					if (isAlphanumeric) {
						txtTitle.setStyle(null);
						lblMsg.setText("");
					}
				}
			});
			return false;
		}

		else if (datePicker.getValue() == null | !validateDate(datePicker.getValue())) {
			datePicker.setValue(LocalDate.now().plusDays(1));
			datePicker.requestFocus();
			lblMsg.setText("Enter a valid date dd/mm/yy");
			return false;
		}

		else if (!validateTime(timeString())) {
			
			for (TextField b : Arrays.asList(txt1, txt2, txt3, txt4)) {
				b.setText("");
				b.setStyle("-fx-background-color: pink;");
				txt1.requestFocus();
				
				b.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent keyEvent) {
					txt1.setStyle(null);
					txt2.setStyle(null);
					txt3.setStyle(null);
					txt4.setStyle(null);
					lblMsg.setText("");
					}
				});
			}
			return false;
		}

		else if (txtImg.getText().isEmpty()) {
			txtImg.requestFocus();
			txtImg.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter an image URL!");

			txtImg.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent keyEvent) {
					Boolean isAlphanumeric = txtImg.getText().matches("[0-9A-Za-z]");
					if (isAlphanumeric) {
						txtImg.setStyle(null);
						lblMsg.setText("");
					}
				}
			});
			return false;
		}

		else if (txtDescription.getText().isEmpty()) {
			txtDescription.requestFocus();
			Region region = (Region) txtDescription.lookup(".content");
			region.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter the Movie's description");

			txtDescription.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent keyEvent) {
					Boolean alphanumeric = txtDescription.getText().matches("[0-9A-Za-z]");
					if (alphanumeric) {
						region.setStyle(null);
						lblMsg.setText("");
					}
				}
			});
			return false;
		}
		else {
			return true;
		}
	}

	private String timeString() {
		return txt1.getText() + ":" + txt2.getText() + "-" + txt3.getText() + ":" + txt4.getText();
	}

	private boolean validateTime(String time) {
		try {
			getTimeInts();
			if(timeLegal(time) & startTimeOK() & timeNoClash()) {
				return true;
			}
			else return false;		
		} catch (Exception e) { //in the event that no time is entered, a StringIndexOutOfBoundsException occurs
			System.out.print(e);
			lblMsg.setText("Enter a time");
			return false;
		}
	}

	private boolean startTimeOK() {
		if (startHr < endHr) {
			return true;
		}
		
		else if (startHr == endHr) {
			if (startMin < endMin) {
				return true;
			} else {
				lblMsg.setText("Start time cannot be after End time!");
				return false;
			}
		}

		else {
			lblMsg.setText("Start time cannot be after End time!");
			return false;
		}
	}
	
	private void getTimeInts() {
		if (Integer.valueOf(txt1.getText().charAt(0)) == 0) {
			startHr = Integer.valueOf(txt1.getText().charAt(1));
		} else {
			startHr = Integer.valueOf(txt1.getText());
		}

		if (Integer.valueOf(txt2.getText().charAt(0)) == 0) {
			startMin = Integer.valueOf(txt2.getText().charAt(1));
		} else {
			startMin = Integer.valueOf(txt2.getText());
		}

		if (Integer.valueOf(txt3.getText().charAt(0)) == 0) {
			endHr = Integer.valueOf(txt3.getText().charAt(1));
		} else {
			endHr = Integer.valueOf(txt3.getText());
		}

		if (Integer.valueOf(txt4.getText().charAt(0)) == 0) {
			endMin = Integer.valueOf(txt4.getText().charAt(1));
		} else {
			endMin = Integer.valueOf(txt4.getText());
		}		
	}

	private boolean timeLegal(String time) {
		String regex = "(([01][0-9]|2[0-3]):[0-5][0-9])-(([01][0-9]|2[0-3]):[0-5][0-9])";
		if (time.matches(regex)) {
			return true;
		}
		else {
			lblMsg.setText("Illegal time!");
			return false;
		}
	}

	private boolean timeNoClash() { //TODO: is getTimeInts needed?
		int i = 0;
		int timesSize = 0;
		try {
			ArrayList<Integer[]> times = AddMovieModel.checkMovies(datePicker.getValue().toString());
			timesSize = times.size();
			for (; i < times.size(); i++) {
				System.out.print(i); //TODO: delete when done testing
				arr = times.get(i);
				int existingStartHr = arr[0];
				int existingStartMin = arr[1];
				int existingEndHr = arr[2];
				int existingEndMin = arr[3];

				if (endHr < existingStartHr) {
					continue;
				}

				if (endHr == existingStartHr) {
					if (endMin < existingStartMin) {
						continue;
					} else {
						break;
					}
				}

				if (startHr > existingEndHr) {
					continue;
				}

				if (startHr == existingEndHr) {
					if (startMin > existingEndMin) {
						continue;
					} else {
						break;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		if (i == timesSize) {
			return true;
		} else {
			String[] a = Arrays.toString(arr).split("[\\[\\]]")[1].split(", ");

			for (int j = 0; j < a.length; j++) {
				if (a[j].length() == 1) {
					a[j] = "0" + a[j];
				}
			}
			String clashTime = a[0] + ":" + a[1] + "-" + a[2] + ":" + a[3];
			lblMsg.setText("There is already a movie showing from " + clashTime + ".");
			return false;
		}
	}

	private boolean validateDate(LocalDate localDate) {
		try {
			if (localDate.isBefore(LocalDate.now())) {
				return false;
			} else {
				return true;
			}
		} catch (NullPointerException ne) {
			return false;
		}
	}
}
