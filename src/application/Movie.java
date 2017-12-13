package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class Movie {

	private final SimpleStringProperty title;
	private final SimpleStringProperty date;
	private final SimpleStringProperty time;
	private final SimpleIntegerProperty bSeats;
	private final SimpleIntegerProperty aSeats;
	private final SimpleStringProperty seats;
	private Button button;


	public Movie (String title, String date, String time, Integer bSeats, Integer aSeats) {
		super();
		this.title = new SimpleStringProperty(title);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
		this.bSeats = new SimpleIntegerProperty(bSeats);
		this.aSeats = new SimpleIntegerProperty(aSeats);
		this.button = new Button("View");
		this.seats = null;
	}

	public Movie (String title, String date, String time, String seats) {
		this.title = new SimpleStringProperty(title);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
		this.seats = new SimpleStringProperty(seats);
		this.bSeats = null;
		this.aSeats = null;
	}

	public String getTitle() {
		return title.get();
	}
	public void setTitle(String sTitle) {
		title.set(sTitle);
	}
	
	public String getDate() {
		return date.get();
	}
	
	public void setDate(String sDate){
		date.set(sDate);
	}
	
	public String getTime() {
		return time.get();
	}
	
	public void setTime(String sTime) {
		time.set(sTime);
	}
	
	public Integer getBSeats() {
		return bSeats.get();
	}
	
	public void setBSeats(Integer sbSeats) {
		bSeats.set(sbSeats);
	}
	
	public Integer getASeats() {
		return aSeats.get();
	}
	
	public void setASeats(Integer sASeats) {
		bSeats.set(sASeats);
	}
	
	public void setButton(Button sButton) {
		this.button = sButton;
	}
	
	public Button getButton() {
		return button;
	}
	
	public String getSeats() {
		return seats.get();
	}
	
	public void setSeats(String sSeats) {
		seats.set(sSeats);
	}
}
