
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CorePipeline.Lemma;
import CorePipeline.NER;
import CorePipeline.POS;
import CorePipeline.Parser;
import CorePipeline.Sentiment;

//https://blog.openshift.com/day-20-stanford-corenlp-performing-sentiment-analysis-of-twitter-using-java/ <-- read later for sentiment example
public class CorePipeline 
{
	private ManageFile mf = new ManageFile();
	
	private Sentiment sentiment;
	private POS pos;
	private Lemma lem;
	private NER ner;
	private Parser parser;
	
	private List<String> selectedAnnotators;
	
	public CorePipeline(List<String> annotators)
	{
		this.selectedAnnotators = annotators;
		
		//Only creates an instance of the class if annotator is selected
		for(String annotator : annotators)
		{
			switch(annotator)
			{
			case("NER"):
				ner = new NER();
				break;
			case("Lemma"):
				lem = new Lemma();
				break;
			case("POS"):
				pos = new POS();
				break;
			case("Parser"):
				parser = new Parser();
				break;
			case("Sentiment"):
				sentiment = new Sentiment();
				break;
			
			}
		}
	}

	public boolean processDataDir(Path dirLocation, Path outputLocation, int outputMode)
	{
		try
		{
			ArrayList<Path> textFileLocations = new ArrayList<Path>();
			textFileLocations = ManageFile.mapDirectory(dirLocation, new ArrayList<Path>(), ".txt");

			for(Path filePath : textFileLocations)
			{
				processData(filePath, outputLocation, outputMode);

			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public boolean processData(Path fileLocation, Path outputLocation, int outputMode)
	{
		try 
		{
			List<String> rawData = Files.readAllLines(fileLocation, StandardCharsets.UTF_8);

			//Creates a folder of the same name as file being processed		
			outputLocation = Paths.get(outputLocation.toString(), ManageFile.pathSlash, fileLocation.toString().substring(fileLocation.toString().lastIndexOf(ManageFile.pathSlash) + 1, fileLocation.toString().lastIndexOf('.')));
			
			if(!Files.exists(outputLocation))
			{
				Files.createDirectory(outputLocation);
			}

			mf.saveMapToFile(processData(rawData), outputLocation);

			return true;
		}
		catch(IOException e)
		{
			System.out.print(e);
			return false;
		}
	}

	public boolean processData(List<String> rawData, Path outputLocation)
	{ 
		try
		{
			
			mf.saveMapToFile(processData(rawData), outputLocation); 
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}

		return true;
	}

	public Map<String, List<String>> processData(List<String> rawData)
	{ 
		Map<String, List<String>> processedData = new HashMap<String, List<String>>();
		for(String annotator : selectedAnnotators)
		{
			if(!processedData.containsKey(annotator))
			{
				processedData.put(annotator, new ArrayList<String>());
			}
		}

		for(String line : rawData)
		{
			if(line != null)
			{		
				
				if(selectedAnnotators.contains("Parser"))
				{
					processedData.get("Parser").add(parser.process(line));
				}
				if(selectedAnnotators.contains("Lemma"))
				{				
					processedData.get("Lemma").add(lem.process(line));
				}
				if(selectedAnnotators.contains("POS"))
				{
					processedData.get("POS").add(pos.process(line));
				}
				if(selectedAnnotators.contains("NER"))
				{
					processedData.get("NER").add(ner.process(line));
				}
				if(selectedAnnotators.contains("Sentiment"))
				{
					processedData.get("Sentiment").add(String.valueOf(sentiment.process(line)));
				}
			}
		}
		return processedData;
	}
	

}
