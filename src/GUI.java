
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget; 
import javafx.beans.value.ChangeListener;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;


public class GUI extends Application {
	private static WebView browser;
	private static WebEngine webEngine;

	@Override public void start(Stage stage) {
		browser = new WebView();
		webEngine = browser.getEngine();
		// create the scene

		String workingDir = System.getProperty("user.dir");
		webEngine.load("file:///" + workingDir + "/src/GUI/GUI.html");


		VBox root = new VBox();
		root.setPrefSize(Settings.GUIWidth, Settings.GUIHeight);

		//Set analyse button listener 
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() 
		{
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) 
			{
				if (newState == State.SUCCEEDED) 
				{
					Document doc = webEngine.getDocument();
					Element el = doc.getElementById("Analyse");
					((EventTarget) el).addEventListener("click", new EventListener() {
						public void handleEvent (Event ev)
						{
							ProcessJSON pJSON = new ProcessJSON();

							showAlert(pJSON.process(new JSONObject(
									webEngine.executeScript("JSON.stringify($('form').serializeObject());").toString())));

						}
					}, false);
				}
			}
		});


		stage.setTitle("SimpleNLP");

		// Create the Scene
		Scene scene = new Scene(root);

		root.getChildren().add(createMenuBar());
		root.getChildren().add(browser);

		stage.setScene(scene);


		// Display the Stage

		stage.show();
		stage.setResizable(false);
		stage.setScene(scene);  



		stage.show();
	}

	private void showAlert(String message) {
		Dialog<Void> alert = new Dialog<>();
		alert.getDialogPane().setContentText(message);
		alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
		alert.showAndWait();
	}

	// TODO Add options for menu bars
	private MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();

		// --- Menu File
		Menu menuFile = new Menu("File");
		// add items to menuView
		MenuItem fileExit = new MenuItem("Exit");

		menuFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				System.exit(0);
			}
		});        
		menuFile.getItems().addAll(fileExit);

		// --- Menu Edit
		Menu menuSettings = new Menu("Settings");
		


		// --- Menu View
		Menu menuHelp = new Menu("Help");
		// add items to menuView

		/*
		setOnAction(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent t) {
				final Stage dialog = new Stage();
				System.out.println("sdfsdf");
				dialog.initModality(Modality.APPLICATION_MODAL);
				//dialog.initOwner(primaryStage);
				VBox dialogVbox = new VBox(20);
				dialogVbox.getChildren().add(new Text("This is a Dialog"));
				Scene dialogScene = new Scene(dialogVbox, 300, 200);
				dialog.setScene(dialogScene);
				dialog.show();
			}
		});  */      

		menuBar.getMenus().addAll(menuFile, menuSettings, menuHelp);

		return menuBar;
		}

		public static void setResult(Map<String , List<String>> data)
		{
			String allData = "";
			for(Entry<String, List<String>> entry : data.entrySet())
			{
				allData += String.format("---- %s ----\n", entry.getKey());
				for(String line : entry.getValue())
				{
					allData += line + "\n\n\n";
				}
			}

			allData = allData.replace("'", "\\'")
					.replace(System.getProperty("line.separator"), "\\n")
					.replace("\n", "\\n")
					.replace("\r", "\\n");
			webEngine.executeScript(String.format("$('#result_tab_area').text('%s');", allData));
		}

		public void open()
		{
			Application.launch();
		}

		//TODO allow stop to be called from initialize
		@Override
		public void stop(){
			System.out.println("Stage is closing");
			// Save file
	}


}

