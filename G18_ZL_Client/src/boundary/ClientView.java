package boundary;

import java.io.IOException;

import boundary.fxmlControllers.ClientConsoleController;
import control.MainController;
import entity.MyMessage.MessageType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * the controller for the client view
 *
 */
public class ClientView extends Application {

//	 Screen size properties
	private static double width;
	private static double height;

	private static double centerW;
	private static double centerH;

	public static Parent connect = null;
	public static Parent logIn = null;
	public static Parent clientConsole = null;

	private static Stage primaryStage;
	public static Scene primaryScene;
	
	/**
	 * console log counter
	 */
	private static int printCnt = 1;

	public static void launchApplication(String[] args) {

		ClientView.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {

			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			width = screenBounds.getWidth();
			height = screenBounds.getHeight();
			centerW = width / 2;
			centerH = height / 2;

			ClientView.primaryStage = primaryStage;

			setUpScenes();

			setUpStage(primaryScene);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * This method is closed when the application is closed. Its purpose is to stop
	 * the client connection and make sure all closing procedures are accomplished
	 * 
	 */
	public void stop() {
		if(ClientConsoleController.getUser()!=null)
			MainController.getMyClient().send(MessageType.INFO,"log/out",ClientConsoleController.getUser());
		MainController.getMyClient().disconnectFromServer();
	}

	/**
	 * method to setup the scenes
	 * @throws IOException
	 */
	private void setUpScenes() throws IOException {
		connect = FXMLLoader.load(getClass().getResource("fxmls/connect-view.fxml"));
		primaryScene = new Scene(connect);
	}

	/**
	 * method to setup the stages
	 * @param scene
	 */
	private void setUpStage(Scene scene) {
		scene.getStylesheets().add("/boundary/fxmlControllers/client.css");
		primaryStage.setTitle("Client Console"); // window title
		primaryStage.getIcons().add(new Image("/boundary/media/client_icon.png"));
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	/**
	 * method to setup connect
	 */
	public static void setUpConnect() {
		if (connect == null)
			try {
				connect = FXMLLoader.load((ClientView.class).getResource("fxmls/connect-view.fxml"));
			} catch (IOException e) {
				ClientView.printErr(ClientView.class, "Could not fetch 'connect' FXML");
			}

		primaryScene.setRoot(connect);
		primaryStage.sizeToScene();
		polishStage();
	}

	/**
	 * method to setup login
	 */
	public static void setUpLogIn() {

		try {
			logIn = FXMLLoader.load((ClientView.class).getResource("fxmls/log-in-view.fxml"));

			primaryScene.setRoot(logIn);
			primaryStage.sizeToScene();
			polishStage();
		} catch (IOException e) {
			ClientView.printErr(ClientView.class, "Could not fetch 'log in' FXML");
		}
	}

	/**
	 * method to setup client console
	 */
	public static void setUpClientConsole() {
		try {
			clientConsole = FXMLLoader.load((ClientView.class).getResource("fxmls/client-console-view.fxml"));

			primaryScene.setRoot(clientConsole);
			primaryStage.sizeToScene();
			polishStage();
		} catch (IOException e) {
			ClientView.print(ClientView.class, "Could not fetch 'client console' FXML");
		}

	}
	
	private static void polishStage() {
		primaryStage.sizeToScene();
		primaryStage.setX(centerW - (primaryScene.getWidth() / 2));
		primaryStage.setY(centerH - (primaryScene.getHeight() / 2));
	}
	
	/**
	 * Prints standard info messages
	 * @param from class that wants to print
	 * @param msg simple info message
	 */
	public static void print(Class<?> from, String msg) {
		System.out.println("<" + (printCnt++) + ">\t[" + from.getName() + "]:\t" + msg);
	}
	
	/**
	 * 
	 * @param from class that wants to print
	 * @param msg simple error message
	 */
	public static void printErr(Class<?> from, String msg) {
		System.err.println("<" + (printCnt++) + ">\t[" + from.getName() + "]:\t" + msg);
	}

}
