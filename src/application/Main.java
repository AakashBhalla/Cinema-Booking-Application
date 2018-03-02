package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	/**
	 * The main entry point for all JavaFX applications. The start method is
	 * called after the init method has returned, and after the system is ready
	 * for the application to begin running. NOTE: This method is called on the
	 * JavaFX Application Thread.
	 * 
	 * @param primaryStage
	 *            the primary stage for this application, onto which the
	 *            application scene can be set. The primary stage will be
	 *            embedded in the browser if the application was launched as an
	 *            applet. Applications may create other stages, if needed, but
	 *            they will not be primary stages and will not be embedded in
	 *            the browser.
	 * @see Application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Cinema Booking Application");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launches the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
