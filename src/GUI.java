
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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

/**
 * This class is repsonsible for everything to do with the GUI.
 * Controls launching, communication and response to the HTML GUI.
 */
public class GUI extends Application {
	private static WebView browser;
	private static WebEngine webEngine;

	/**
	 * Starts the HTML GUI
	 * @param  Stage  default paramater for start
	 * @return void
	 * @see         Image
	 */
	@Override public void start(Stage stage) 
	{
		browser = new WebView();
		webEngine = browser.getEngine();
		// create the scene

		String workingDir = System.getProperty("user.dir");
		webEngine.load("file:///" + workingDir + "/src/GUI/GUI.html");
		// uses the web engine to load the front end GUI


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

		// sets the title for the scene
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

	/**
	 * Displays text to the screen. 
	 *
	 * @param  Message - A message that is displayed back to the user
	 */
	private void showAlert(String message) 
	{
		Dialog<Void> alert = new Dialog<>();
		alert.getDialogPane().setContentText(message);
		alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
		alert.showAndWait();

	}

	/**
	 * Displays a menubar at the top of the program. 
	 *
	 * @return  MenuBar - Displays a menubar
	 * @see         Image
	 */
	private MenuBar createMenuBar() 
	{

		MenuBar menuBar = new MenuBar();

		// --- Menu File
		Menu menuFile = new Menu("File");
		// add items to menuView

		//Creates the menu items
		MenuItem newFile = new MenuItem("New");
		MenuItem saveFile = new MenuItem("Save");
		MenuItem openFile = new MenuItem("Open");
		MenuItem fileExit = new MenuItem("Exit");

		fileExit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				System.exit(0);
				//closes the scene
			}
		});               


		//Adds all the menu items
		menuFile.getItems().addAll(newFile,openFile,saveFile,fileExit);

		// --- Menu Edit
		Menu editMenu = new Menu("Edit");


		// Menu items for "Menu edit"
		MenuItem undo = new MenuItem("Undo");
		MenuItem redo = new MenuItem("Redo");

		editMenu.getItems().addAll(undo,redo);
		redo.setDisable(true);
		undo.setDisable(true);
		//		languageFive.setDisable(true);

		// --- Menu View

		Menu menuHelp = new Menu("Help");

		MenuItem helpContent  = new MenuItem("Help Content");
		MenuItem aboutNLP = new MenuItem("About NLP");

		aboutNLP.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent t) 
			{
				JFrame newFrame = new JFrame();
				String pt1 = "<html><body width='";

				//text below appears when "About NLP" menu item is selected
				String pt2 =
						"'><h1>About NLP</h1>" +
								"<p>" +
								" Natural language processing (NLP) is a field of computer science, " +
								" artificial intelligence, and computational linguistics concerned with the interactions " +
								" between computers and human (natural) languages and, in particular, " +
								" concerned with programming computers to fruitfully process large natural language corpora. " +
								"</p>";

				// width settings for the dialog
				int width = 250;
				String s = pt1 + width + pt2 ;

				JOptionPane.showMessageDialog(newFrame, s);

			}
		}); 


		helpContent.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {	
				JFrame aWindow = new JFrame();
				
				String pt1 = "<html><body width='";
                String pt2 =
                    "'><h1>Help page</h1>" +
                    "<p>" +
                    " The project is hosted on GitHub.com, you can download it directly from the website or "
                    + "install it from eclipse using this URL "
                    + "https://github.com/WilliamStott/NLP.git"
                    + "</p>";
                String pt3 = 
                	"<p>" +
                	" To install from eclipse:</p>"
                	+"<p>1. Open eclipse,</p>"
					+"<p>2. File,</p>"
					+"<p>3. Import,</p>"
					+"<p>4. Git,</p>"
					+"<p>5. Projects from Git,</p>"
					+"<p>6. CloneURI,</p>"
					+"<p>7. Enter URL above in URI then press next,</p>"
					+"<p>8. Select Master branch and next,</p>"
					+"<p>9. Select next until completion</p>";
                String pt4 = 
                	"<p>Once you have the project, you will need to install the language models which are not hosted on GitHub as the filesizes are too large.</p>"
                	+ "<p>So you will need to download them from this site.http://stanfordnlp.github.io/CoreNLP/download.html</p>"
                	+ "<p>All the languages will be needed to be installed except from English(KBP) and Chinese.</p>"
                	+ "<p>Place the models into the project library must add the models to the classpath in the java build section and add them as external "
                	+ "JARS classpath can be found by right clicking the project folder and pressing alt+enter</p>";
                
                String pt5 = "<p>To avoid an memory error</p>"
                		+ "<p>You must increase the memory allocation used by Eclipse.</p>"
                		+ "<p>This is done by opening eclipse and clicking run -> run configurations -> "
                		+ "Arguments tab -> in the VM Arguments box type '-Xms2048M -Xmx3075M' -></p>"
                		+ "<p>Apply</p>";
                
                int width = 500;
                
                String te = pt1 + width + pt2 + width + pt3 + width + pt4 + width + pt5;
                
//				aWindow.setBounds(900, 100, 700, 400);				
//				aWindow.setVisible(true);
				JOptionPane.showMessageDialog(aWindow, te);
			}
		});
		// add items to menuView
		menuHelp.getItems().addAll(aboutNLP,helpContent);    

		// adds the menu items to the menu bar
		menuBar.getMenus().addAll(menuFile, editMenu, menuHelp);

		return menuBar;
	}

	// setting functions for how the results are formatted
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
	public void stop()
	{
		System.out.println("Stage is closing");
		// Save file
	}


}

