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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AddMovieController implements Initializable {
	
	@FXML
	private TextField txtTitle;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private TextField txtImg;
	
	@FXML 
	private TextArea txtDescription;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private Button btnSubmit;
	
	@FXML
	private Label lblMsg;
	
	@FXML
	private TextField txt1;
	
	@FXML
	private TextField txt2;
	
	@FXML
	private TextField txt3;
	
	@FXML
	private TextField txt4;

	private Integer[] arr;
	
	private String userName;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		configureDatePicker();
		timeControl();
	}
	
	public void getUser (String user) {
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
	
	public void timeControl() {
		final int maxLength = 2;
		txt1.textProperty().addListener(new ChangeListener<String>() {
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		            if (!newValue.matches("\\d{0,9}")) {
		                txt1.setText(oldValue);
		            }		            
		            if (txt1.getText().length() == maxLength) {
		            	txt2.requestFocus();
		            }
		            if (txt1.getText().length() > maxLength) {
		            	String s = txt1.getText().substring(0, maxLength);
		            	txt1.setText(s);
		            }
			}
		});
		
		txt2.textProperty().addListener(new ChangeListener<String>() {
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		            if (!newValue.matches("\\d{0,9}")) {
		                txt2.setText(oldValue);
		            }
		            if (txt2.getText().length() == maxLength) {
		            	txt3.requestFocus();
		            }
		            if (txt2.getText().length() > maxLength) {
		            	String s = txt2.getText().substring(0, maxLength);
		            	txt2.setText(s);
		            }
			}
		});
		
		txt3.textProperty().addListener(new ChangeListener<String>() {
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		            if (!newValue.matches("\\d{0,9}")) {
		                txt3.setText(oldValue);
		            }
		            if (txt3.getText().length() == maxLength) {
		            	txt4.requestFocus();
		            }
		            if (txt3.getText().length() > maxLength) {
		            	String s = txt3.getText().substring(0, maxLength);
		            	txt3.setText(s);
		            }
			}
		});
		
		txt4.textProperty().addListener(new ChangeListener<String>() {
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		            if (!newValue.matches("\\d{0,9}")) {
		                txt4.setText(oldValue);
		            }
		            if (txt4.getText().length() == maxLength) {
		            	txtImg.requestFocus();
		            }
		            if (txt4.getText().length() > maxLength) {
		            	String s = txt4.getText().substring(0, maxLength);
		            	txt4.setText(s);
		            }
			}
		});
	}
	
	public void Submit(ActionEvent event) throws SQLException {
		if (fieldsValid()) {
			lblMsg.setText("valid entries");
			AddMovieModel.insertMovie(txtTitle.getText(), datePicker.getValue().toString(), formTime(), txtImg.getText(), txtDescription.getText(), 12);
	}
	}
	
	public void Cancel(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Pane EmployeePane = loader.load(getClass().getResource("/application/Employee.fxml").openStream());
			Scene EmployeeScene = new Scene(EmployeePane);
			//This line gets the Stage information
			EmployeeController employeeController = (EmployeeController)loader.getController(); 
			employeeController.getUser(userName);
			Stage window = (Stage)(((Node) event.getSource()).getScene().getWindow());
			window.setScene(EmployeeScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean fieldsValid() {
		if(txtTitle.getText().isEmpty()) {
			txtTitle.requestFocus();
			txtTitle.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter the Film title!");
			
			txtTitle.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
			    	Boolean alphanumeric = txtTitle.getText().matches("[0-9A-Za-z]");
			    	 if(alphanumeric) {
			    		 txtTitle.setStyle(null);
			    		 lblMsg.setText("");
			    	 }
			    }
			    });
			return false;
		}

		else if(datePicker.getValue() == null | !validateDate(datePicker.getValue())) {
			datePicker.setValue(LocalDate.now().plusDays(1));
			datePicker.requestFocus();
			lblMsg.setText("Enter a valid date dd/mm/yy");
			return false;
		}
		
		else if (!validateTime(formTime())) {
			txt1.setText("");
			txt1.setStyle("-fx-background-color: pink;");
			txt1.requestFocus();
			txt2.setText("");
			txt2.setStyle("-fx-background-color: pink;");
			txt3.setText("");
			txt3.setStyle("-fx-background-color: pink;");
			txt4.setText("");
			txt4.setStyle("-fx-background-color: pink;");
			//lblMsg.setText("Enter a valid time xx:xx-xx:xx");
			
			txt1.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
		    		 txt1.setStyle(null);
		    		 txt2.setStyle(null);
		    		 txt3.setStyle(null);
		    		 txt4.setStyle(null);
		    		 lblMsg.setText("");
			    }
			    });
			
			txt2.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
		    		 txt1.setStyle(null);
		    		 txt2.setStyle(null);
		    		 txt3.setStyle(null);
		    		 txt4.setStyle(null);
		    		 lblMsg.setText("");
			    }
			    });
			
			txt3.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
		    		 txt1.setStyle(null);
		    		 txt2.setStyle(null);
		    		 txt3.setStyle(null);
		    		 txt4.setStyle(null);
		    		 lblMsg.setText("");
			    }
			    });
			
			txt4.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
		    		 txt1.setStyle(null);
		    		 txt2.setStyle(null);
		    		 txt3.setStyle(null);
		    		 txt4.setStyle(null);
		    		 lblMsg.setText("");
			    }
			    });
			
			return false;
		}
		
		else if(txtImg.getText().isEmpty()) {
			txtImg.requestFocus();
			txtImg.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter an image URL!");
			
			txtImg.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
			    	Boolean alphanumeric = txtImg.getText().matches("[0-9A-Za-z]");
			    	 if(alphanumeric) {
			    		 txtImg.setStyle(null);
			    		 lblMsg.setText("");
			    	 }
			    }
			    });
			return false;
		}
				
		else if(txtDescription.getText().isEmpty()) {
			txtDescription.requestFocus();
			Region region = (Region) txtDescription.lookup(".content");
			region.setStyle("-fx-background-color: pink;");
			lblMsg.setText("Enter a Film description!");
			
			txtDescription.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
			    	Boolean alphanumeric = txtDescription.getText().matches("[0-9A-Za-z]");
			    	 if(alphanumeric) {
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
	
	private String formTime() {
		return txt1.getText() + ":" + txt2.getText() + "-" + txt3.getText() + ":" + txt4.getText();
	}

	private boolean validateTime(String time) {
		if(timeLegal(time)) {
			if (startTimeOK()) {
				if(timeNoClash()) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			lblMsg.setText("Illegal time!");
			return false;
		}
	}

	private boolean startTimeOK() {
		int startHr, startMin, endHr, endMin;
		if (Integer.valueOf(txt1.getText().charAt(0)) == 0) {
			startHr = Integer.valueOf(txt1.getText().charAt(1));
		}
		else {
			startHr = Integer.valueOf(txt1.getText());
		}
		
		if (Integer.valueOf(txt2.getText().charAt(0)) == 0) {
			startMin = Integer.valueOf(txt2.getText().charAt(1));
		}
		else {
			startMin = Integer.valueOf(txt2.getText());
		}
		
		if (Integer.valueOf(txt3.getText().charAt(0)) == 0) {
			endHr = Integer.valueOf(txt3.getText().charAt(1));
		}
		else {
			endHr = Integer.valueOf(txt3.getText());
		}
		
		if (Integer.valueOf(txt4.getText().charAt(0)) == 0) {
			endMin = Integer.valueOf(txt4.getText().charAt(1));
		}
		else {
			endMin  = Integer.valueOf(txt4.getText());
		}
		
		if (startHr < endHr) {
			return true;
		}
		else if (startHr == endHr) {
			if(startMin < endMin) {
				return true;
			}
			else {
				lblMsg.setText("Start time cannot be after End time!");
				return false;
			}
		}
		
		else {
			lblMsg.setText("Start time cannot be after End time!");
			return false;
		}
	}
	
	private boolean timeLegal(String time) {
		String regex = "(([01][0-9]|2[0-3]):[0-5][0-9])-(([01][0-9]|2[0-3]):[0-5][0-9])";
		return time.matches(regex);
	}
	
	private boolean timeNoClash() {
		int startHr, startMin, endHr, endMin;
		if (Integer.valueOf(txt1.getText().charAt(0)) == 0) {
			startHr = Integer.valueOf(txt1.getText().charAt(1));
		}
		else {
			startHr = Integer.valueOf(txt1.getText());
		}
		
		if (Integer.valueOf(txt2.getText().charAt(0)) == 0) {
			startMin = Integer.valueOf(txt2.getText().charAt(1));
		}
		else {
			startMin = Integer.valueOf(txt2.getText());
		}
		
		if (Integer.valueOf(txt3.getText().charAt(0)) == 0) {
			endHr = Integer.valueOf(txt3.getText().charAt(1));
		}
		else {
			endHr = Integer.valueOf(txt3.getText());
		}
		
		if (Integer.valueOf(txt4.getText().charAt(0)) == 0) {
			endMin = Integer.valueOf(txt4.getText().charAt(1));
		}
		else {
			endMin = Integer.valueOf(txt4.getText());
		}
		
		int i=0;
		int timesSize = 0;
		
		try {
			ArrayList<Integer[]> times = AddMovieModel.checkMovies(datePicker.getValue().toString());
			timesSize = times.size();
			for (; i < times.size(); i++) { 
				System.out.print(i);
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
					}
					else {
						break;
					}
				}
				
				if (startHr > existingEndHr) {
					continue;
				}
				
				if (startHr == existingEndHr) {
					if (startMin > existingEndMin) {
						continue;
					}
					else {
						break;
					}
				}		
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
		}
		if (i == timesSize) {
			return true;
		}
		else {
//			for (int j = 0; j < arr.length; j++) {
//				String parts[j] = String.valueOf(arr[j]); 
//			}
			
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
			}
			else {
				return true;
			}
		} catch(NullPointerException ne) {
			return false;
		}
	}
}
