
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget; 
import javafx.beans.value.ChangeListener;
import javafx.application.Application;
import javafx.application.Platform;
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
    
    
    @Override public void start(Stage stage) {
    	final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        // create the scene
    	
    	String workingDir = System.getProperty("user.dir");
        webEngine.load("file:///" + workingDir + "/GUI.html");
        
        
        VBox root = new VBox();
        root.setPrefSize(800, 600);

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

    String outcome;
    private void loadlisteners(WebEngine webEngine)
    {
    	EventListener listener = new EventListener() {
        	public void handleEvent (Event ev)
        	{

            	ProcessJSON pJSON = new ProcessJSON();
				String x = webEngine.executeScript("JSON.stringify($('form').serializeObject());").toString();

				Thread thread1 = new Thread(new Runnable() {
					public void run() {
						outcome = pJSON.process(new JSONObject(x));
					}
				});
				thread1.start();

				// Finish running thread
				try {
					thread1.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				showAlert(outcome);
         
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


