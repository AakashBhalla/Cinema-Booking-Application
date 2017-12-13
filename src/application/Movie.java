package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 * Defines the movie data model and provides methods and fields used when
 * configuring table views. The string and int properties are created to enable
 * referencing to a particular data element.
 * 
 * @author Aakash
 */
public class Movie {

	private final SimpleStringProperty title;
	private final SimpleStringProperty date;
	private final SimpleStringProperty time;
	private final SimpleIntegerProperty bSeats;
	private final SimpleIntegerProperty aSeats;
	private final SimpleStringProperty seats;
	private Button button;

	/**
	 * Defines data model for the table view configured in Search Movie
	 * Controller
	 *
	 * @author Aakash
	 */
	public Movie(String title, String date, String time, Integer bSeats, Integer aSeats) {
		super();
		this.title = new SimpleStringProperty(title);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
		this.bSeats = new SimpleIntegerProperty(bSeats);
		this.aSeats = new SimpleIntegerProperty(aSeats);
		this.button = new Button("View");
		this.seats = null;
	}

	/**
	 * Defines data model for the table view configured in Book Movie Controller
	 * 
	 * @author Aakash
	 */
	public Movie(String title, String date, String time, String seats) {
		this.title = new SimpleStringProperty(title);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
		this.seats = new SimpleStringProperty(seats);
		this.bSeats = null;
		this.aSeats = null;
	}

	/** Get method for title data element
	 * @author Aakash*/
	public String getTitle() {
		return title.get();
	}

	/** Set method for title data element
	 * @author Aakash*/
	public void setTitle(String sTitle) {
		title.set(sTitle);
	}

	/** Get method for date data element
	 * @author Aakash*/
	public String getDate() {
		return date.get();
	}

	/** Set method for date data element
	 * @author Aakash*/
	public void setDate(String sDate) {
		date.set(sDate);
	}

	/** Get method for time data element
	 * @author Aakash*/
	public String getTime() {
		return time.get();
	}

	/** set method for time data element
	 * @author Aakash*/
	public void setTime(String sTime) {
		time.set(sTime);
	}

	/** Get method for bSeats data element
	 * @author Aakash*/
	public Integer getBSeats() {
		return bSeats.get();
	}

	/** Set method for bSeats data element
	 * @author Aakash*/
	public void setBSeats(Integer sbSeats) {
		bSeats.set(sbSeats);
	}

	/** Get method for aSeats data element
	 * @author Aakash*/
	public Integer getASeats() {
		return aSeats.get();
	}

	/** Set method for aSeats data element
	 * @author Aakash*/
	public void setASeats(Integer sASeats) {
		bSeats.set(sASeats);
	}
	
	/** Get method for button data element
	 * @author Aakash*/
	public Button getButton() {
		return button;
	}

	/** Set method for button data element
	 * @author Aakash*/
	public void setButton(Button sButton) {
		this.button = sButton;
	}
	
	/** Get method for seats data element
	 * @author Aakash*/
	public String getSeats() {
		return seats.get();
	}

	/** Set method for seats data element
	 * @author Aakash*/
	public void setSeats(String sSeats) {
		seats.set(sSeats);
	}
}
