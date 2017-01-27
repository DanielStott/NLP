import edu.stanford.nlp.simple.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Initialize {
    public static void main(String[] args) throws InterruptedException { 
    
    	GUI browser = new GUI();

    	browser.open();
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	/*ManageFile mf = new ManageFile();
    	CorePipeline cp = new CorePipeline();
    	//Path fileLocation = mf.selectFile();
    	List<String> selectedAnnotators = new ArrayList<String>();
    	String workingDir = System.getProperty("user.dir");
    	
    	int test = cp.sentiment("This is a very bad test string to use for sentiment.");
    	System.out.println(test);

    	selectedAnnotators.add("Parser");
    	selectedAnnotators.add("POS");
    	selectedAnnotators.add("Lemma");
    	selectedAnnotators.add("NER");


    	
    	
    	Path documents = Paths.get("C:\\Users\\Stott\\Documents\\TextFiles");
    	if( cp.processDataDir(documents, selectedAnnotators, Paths.get(workingDir), 0))
    	{
    		System.out.println("Processed Correctly");
    	}
    	else
    	{
    		System.out.println("Failed processing");
    	}
    	
    	
    	/*if( ((fileLocation != null) ? cp.processData(fileLocation, selectedAnnotators, Paths.get(workingDir), 0): false))
    	{
    		System.out.println("Processed Correctly");
    	}
    	else
    	{
    		System.out.println("Failed processing");
    	}*/
    } 
}