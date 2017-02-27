
import java.util.List;
import java.util.Map;


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
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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

		//Set listeners
		loadlisteners(webEngine);


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

	private void loadlisteners(WebEngine webEngine)
	{
		EventListener listener = new EventListener() {
			public void handleEvent (Event ev)
			{
				//TODO Make sure that processing is done on a separate thread from JavaFX 
				ProcessJSON pJSON = new ProcessJSON();
				showAlert(pJSON.process(new JSONObject(
						webEngine.executeScript("JSON.stringify($('form').serializeObject());").toString())));
			}
		};

		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() 
		{
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) 
			{
				if (newState == State.SUCCEEDED) 
				{
					Document doc = webEngine.getDocument();
					Element el = doc.getElementById("Analyse");
					((EventTarget) el).addEventListener("click", listener, false);
				}
			}
		});
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

		// --- Menu Edit
		Menu menuEdit = new Menu("Settings");

		// --- Menu View
		Menu menuView = new Menu("Help");

		menuBar.getMenus().addAll(menuFile, menuEdit, menuView);

		return menuBar;
	}

	public static void setResult(Map<String , List<String>> data)
	{
		String allData = "";
		if(data.containsKey("Lemma"))
		{
			allData += "---- LEMMA ---- \n";
			for(String line : data.get("Lemma"))
			{
				allData += line + "\n";
				
			}
			allData += "\n\n\n\n";
		}
		if(data.containsKey("Parser"))
		{
			allData += "---- Parser ---- \n";
			for(String line : data.get("Parser"))
			{
				allData += line + "\n";;
			}
			allData += "\n\n\n\n";
		}
		if(data.containsKey("POS"))
		{
			allData += "---- POS ---- \n";
			for(String line : data.get("POS"))
			{
				allData += line + "\n";;
			}
			allData += "\n\n\n\n";
		}
		if(data.containsKey("NER"))
		{
			allData += "---- NER ---- \n";
			for(String line : data.get("NER"))
			{
				allData += line + "\n";;
			}
			allData += "\n\n\n\n";
		}
		if(data.containsKey("Sentiment"))
		{
			allData += "---- Sentiment ---- \n";
			for(String line : data.get("Sentiment"))
			{
				allData += line + "\n";;
			}
			allData += "\n\n\n\n";
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

