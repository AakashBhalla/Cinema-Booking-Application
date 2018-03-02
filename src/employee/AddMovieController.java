package employee;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import application.User;
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
import javafx.scene.image.Image;
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

	private User user;
	private AddMovieModel addMovieModel = new AddMovieModel();
	private static final int NO_SEATS = 12;

	/**
	 * Called to initialize the Add Movie Controller after its root element has
	 * been completely processed. Calls methods to configure the date picker and
	 * time text fields.
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
		configureDatePicker();
		timeControl();
		txtDescription.setWrapText(true);
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
	 * Configures the date picker so that it initializes on tomorrrow's date.
	 * All date's prior to tomorrow are disabled so that a user can only add
	 * movies movie showing tomorrow onwards.
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

						if (item.isBefore(LocalDate.now().plusDays(1))) {
							// disable and set background color pink
							setDisable(true);
							setStyle("-fx-background-color : #ffc0cb;");
						}
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);
		// set date it initializes on
		datePicker.setValue(LocalDate.now().plusDays(1));
	}

	/**
	 * Controls the time text fields so that only two numbers can be entered.
	 * Once two numbers have been entered, immediately jumps to the next field.
	 * 
	 * @author Aakash
	 */
	public void timeControl() {
		final int MAX_LENGTH = 2;
		for (TextField b : Arrays.asList(txt1, txt2, txt3, txt4)) {
			// add change listener to text fields which immediately deletes any
			// non numeric character, doesn't allow more than two characters per
			// field and
			// jumps to the next text field when 2 numbers have been entered
			b.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d{0,9}")) {
						b.setText(oldValue);
					}
					if (b.getText().length() > MAX_LENGTH) {
						String s = b.getText().substring(0, MAX_LENGTH);
						b.setText(s);
					}

					if (txt1.getText().length() == MAX_LENGTH) {
						txt2.requestFocus();
						if (txt2.getText().length() == MAX_LENGTH) {
							txt3.requestFocus();
							if (txt3.getText().length() == MAX_LENGTH) {
								txt4.requestFocus();
								if (txt4.getText().length() == MAX_LENGTH) {
									txtImg.requestFocus();
								}
							}
						}
					}
				}
			});
		}
	}

	/**
	 * Called whenever the submit button is pressed. Calls a method to check if
	 * all fields are valid. If they are, adds the information to the database
	 * and resets the add movie view.
	 * 
	 * @author Aakash
	 * @param event
	 *            after submit is pressed
	 * @throws SQLException
	 */
	public void Submit(ActionEvent event) throws SQLException {
		if (fieldsValid()) {
			lblMsg.setText("Movie added!");

			// date in traditional German format (used in UK)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String date = datePicker.getValue().format(formatter).toString();
			// adds all fields and available seats to movies table of database
			addMovieModel.insertMovie(txtTitle.getText(), date, timeString(), txtImg.getText(),
					txtDescription.getText(), NO_SEATS);
			// add screen seating availability
			addMovieModel.insertScreen();

			// empty all input fields if movie is successfully added and reset
			// date picker
			for (TextField b : Arrays.asList(txtTitle, txt1, txt2, txt3, txt4, txtImg)) {
				b.setText("");
			}
			datePicker.setValue(LocalDate.now().plusDays(1));
			txtDescription.setText("");
		}
	}

	/**
	 * Launches the employee view. Creates new pane by loading employee fxml,
	 * and a new scene from the pane. Gets the stage information from the event
	 * source and sets the new scene.
	 * 
	 * @author Aakash
	 * @param event
	 *            ActionEvent after cancel button is pressed
	 */
	public void Cancel(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane EmployeePane = loader.load(getClass().getResource("/employee/Employee.fxml").openStream());
			Scene EmployeeScene = new Scene(EmployeePane);
			EmployeeController employeeController = (EmployeeController) loader.getController();
			employeeController.getUser(user);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setScene(EmployeeScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks whether each input field is valid. Checks that the title entered
	 * isn't empty. Checks that the date entered isn't empty and calls a method
	 * to validate the date. Checks that the time entered is valid by calling
	 * the validate time method. Checks that the image entered isn't empty and
	 * is an image url. Checks that the description entered isn't empty. If
	 * there are any problems, requests focus and colour turns pink.
	 * 
	 * @author Aakash
	 */
	private boolean fieldsValid() {
		// set image text field style as null (needed in the event that the user
		// copies and pastes a link into the pink text field
		txtImg.setStyle(null);
		// reset style for text area
		Region region = (Region) txtDescription.lookup(".content");
		region.setStyle(null);

		// if title text field is empty, requests focus and sets pink background
		if (txtTitle.getText().isEmpty()) {
			txtTitle.requestFocus();
			txtTitle.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter a Movie title.");

			// reset style if alphanumeric key is pressed
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

		// if date picker value is null (i.e. text has been deleted) or date is
		// not valid requests focus
		else if (datePicker.getValue() == null | !validateDate(datePicker.getValue())) {
			datePicker.setValue(LocalDate.now().plusDays(1));
			datePicker.requestFocus();
			lblMsg.setText("Enter a valid date in the format dd/mm/yy.");
			return false;
		}

		// if time entered is not valid, empties fields, requests focus and sets
		// pink background
		else if (!validTime()) {
			for (TextField b : Arrays.asList(txt1, txt2, txt3, txt4)) {
				b.setText("");
				b.setStyle("-fx-background-color: pink;");
				txt1.requestFocus();

				// resets field style when key is pressed
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

		// if image text field is empty or is not an image, empties field,
		// requests focus and sets pink background
		else if (txtImg.getText().isEmpty() || !isImage(txtImg.getText())) {
			txtImg.requestFocus();
			txtImg.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter an image URL (png or jpg).");

			// resets field style when alphanumeric key is pressed
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

		// if description field is empty, requests focus and sets pink
		// background
		else if (txtDescription.getText().isEmpty()) {
			txtDescription.requestFocus();
			region.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter the Movie's description.");

			// resets field style when alphanumeric key is pressed
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
		// if all fields are valid then return true
		else {
			return true;
		}
	}

	/**
	 * Checks that the date is valid. A user can only add movies for dates after
	 * today.
	 * 
	 * @author Aakash
	 * @param enteredDate
	 *            the LocalDate object for date entered
	 * @return boolean
	 */
	private boolean validateDate(LocalDate enteredDate) {
		try {
			if (enteredDate.isBefore(LocalDate.now()) | enteredDate.equals(LocalDate.now())) {
				return false;
			} else {
				return true;
			}
		} catch (NullPointerException ne) {
			return false;
		}
	}

	/**
	 * Checks whether the URL entered directs to an image.
	 * 
	 * @author Aakash
	 * @param imageURL
	 *            The URL to the image
	 * @return boolean
	 */
	private boolean isImage(String imageURL) {
		// must be png or jpg
		if (!imageURL.matches("(http(s?):/)(/[^/]+)+" + ".(?:jpg|png)")) {
		return false;
	}
		try {
			// creates image from URL entered and checks width
			Image image = new Image(imageURL);
			if (image.getWidth() == -1) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			System.out.print(e);
			return false;
		}
	}

	/**
	 * Takes the time text field entries and returns a time String.
	 * 
	 * @author Aakash
	 * @return Time String in the format HH:mm-HH:mm
	 */
	private String timeString() {
		return txt1.getText() + ":" + txt2.getText() + "-" + txt3.getText() + ":" + txt4.getText();
	}

	/**
	 * Checks whether the time entered is valid. First checks that LocalTime
	 * objects can be made by parsing a String formed from the text field
	 * inputs. If this is not possible, an illegal time has been entered. Then
	 * checks the start time is before the end time. Then checks if the times
	 * entered clash with any existing times by calling the timeNoClash method.
	 * 
	 * @author Aakash
	 * @return boolean
	 */
	private boolean validTime() {
		LocalTime startTime, endTime;
		// check if times are legal by parsing them i.e. 24:00 or 22:61 is
		// rejected
		try {
			String startTimeString = txt1.getText() + ":" + txt2.getText();
			startTime = LocalTime.parse(startTimeString);

			String endTimeString = txt3.getText() + ":" + txt4.getText();
			endTime = LocalTime.parse(endTimeString);
		} catch (Exception e) {
			System.out.println(e);
			lblMsg.setText("Illegal time!");
			return false;
		}
		// check if start time is before end time
		if (startTime.isAfter(endTime)) {
			lblMsg.setText(("Start time cannot be after end time!"));
			return false;
		}

		// check if times clash with existing movie showings
		if (!timeNoClash(startTime, endTime)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks that the times entered do not clash with the times of existing
	 * movies. Receives all times showing on the day entered from the movies
	 * table of the database. The results are cycled through and each time is
	 * checked for overlap. If there is a clash, the cycle stops. If the cycle
	 * completes, returns that there is no clash.
	 * 
	 * @author Aakash
	 * @param startTime
	 *            LocalTime object for start time entered
	 * @param endTime
	 *            LocalTime object for end time entered
	 * @return boolean
	 * @throws SQLException
	 */
	private boolean timeNoClash(LocalTime startTime, LocalTime endTime) {
		// get date entered in traditional German format (used in UK)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String date = datePicker.getValue().format(formatter).toString();
		// initialize int i and timeSize, and LocalTimes for existing start time
		// and end time
		int i = 0;
		int timeSize = 0;
		LocalTime existingStartTime = null;
		LocalTime existingEndTime = null;
		try {
			// get times of movies that are showing on date entered
			ArrayList<String[]> times = addMovieModel.checkMovieTimes(date);
			timeSize = times.size();
			// cycle through times and get LocalTime object for existing movie
			// start time and end time
			for (; i < times.size(); i++) {
				String[] existingTimeArr = times.get(i);
				existingStartTime = LocalTime.parse(existingTimeArr[0]);
				existingEndTime = LocalTime.parse(existingTimeArr[1]);

				// start time cannot be existing start time and end time cannot
				// be existing end time
				if (startTime.equals(existingStartTime) | endTime.equals(existingEndTime)
						| startTime.equals(existingEndTime) | endTime.equals(existingStartTime)) {
					break;
				}

				// if the movie being added starts before an existing movie, it
				// must also end before
				if (startTime.isBefore(existingStartTime)) {
					if (endTime.isBefore(existingStartTime)) {
						continue;
					} else {
						break;
					}
				}

				// if the movie being added ends before an existing movie ends,
				// it must also end before it starts
				if (endTime.isBefore(existingEndTime)) {
					if (endTime.isBefore(existingStartTime)) {
						continue;
					} else {
						break;
					}
				}

				// if the movie being added starts after an existing movie
				// starts, it must also end before it starts
				if (startTime.isAfter(existingStartTime)) {
					if (startTime.isBefore(existingEndTime)) {
						break;
					}
				}
			}

			// if the cycle is complete, i will be equal to timeSize
			if (i == timeSize) {
				return true;
			} else {
				// if cycle is not complete, display warning message about time
				// clash
				String warning = "There is already a movie showing from " + existingStartTime.toString() + "-"
						+ existingEndTime.toString() + ".";
				lblMsg.setText(warning);
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
