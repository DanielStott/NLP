
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
import CorePipeline.Arabic.ArabicPOS;
import CorePipeline.Arabic.ArabicParser;
import CorePipeline.French.FrenchPOS;
import CorePipeline.French.FrenchParser;
import CorePipeline.Spanish.SpanishNER;
import CorePipeline.Spanish.SpanishPOS;
import CorePipeline.Spanish.SpanishParser;
import javafx.application.Platform;

//https://blog.openshift.com/day-20-stanford-corenlp-performing-sentiment-analysis-of-twitter-using-java/ <-- read later for sentiment example
public class CorePipeline 
{
	private ManageFile mf = new ManageFile();

	private Sentiment sentiment;
	private POS pos;
	private Lemma lem;
	private NER ner;
	private Parser parser;

	private ArabicPOS aPOS;
	private ArabicParser aParser;

	private FrenchPOS fPOS;
	private FrenchParser fParser;
	
	private SpanishPOS sPOS;
	private SpanishParser sParser;
	private SpanishNER sNER;

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
					if(Settings.language.equals("English")) ner = new NER();
					else if (Settings.language.equals("Spanish")) sNER = new SpanishNER();
				break;
				case("Lemma"):
					lem = new Lemma();
				break;
				case("POS"):
					if(Settings.language.equals("English")) pos = new POS();
					else if (Settings.language.equals("Arabic")) aPOS = new ArabicPOS();
					else if (Settings.language.equals("French")) fPOS = new FrenchPOS();
					else if (Settings.language.equals("Spanish")) sPOS = new SpanishPOS();
				break;
				case("Parser"):
					if(Settings.language.equals("English")) parser = new Parser();
					else if (Settings.language.equals("Arabic")) aParser = new ArabicParser();
					else if (Settings.language.equals("French")) fParser = new FrenchParser();
					else if (Settings.language.equals("Spanish")) sParser = new SpanishParser();
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

			Map<String , List<String>> allData = processData(rawData); 
			mf.saveMapToFile(allData, outputLocation);
			Platform.runLater(new Runnable() {
				  @Override public void run() { 
					  GUI.setResult(allData);
				  }
				});
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
			Map<String , List<String>> allData = processData(rawData);
			mf.saveMapToFile(allData, outputLocation); 
			
			Platform.runLater(new Runnable() {
			  @Override public void run() { 
				  GUI.setResult(allData);
			  }
			});
			
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
			if(line != null && !line.equals(""))
			{		

				if(selectedAnnotators.contains("Parser"))
				{
					if(Settings.language.equals("Arabic"))
					{
						processedData.get("Parser").add(aParser.process(line));
					}
					else if(Settings.language.equals("French"))
					{
						processedData.get("Parser").add(fParser.process(line));
					}
					else if(Settings.language.equals("Spanish"))
					{
						processedData.get("Parser").add(sParser.process(line));
					}
					else
					{
						processedData.get("Parser").add(parser.process(line));
					}
				}
				if(selectedAnnotators.contains("Lemma"))
				{				
					processedData.get("Lemma").add(lem.process(line));
				}
				if(selectedAnnotators.contains("POS"))
				{
					if(Settings.language.equals("Arabic"))
					{
						processedData.get("POS").add(aPOS.process(line));
					}
					else if(Settings.language.equals("French"))
					{
						processedData.get("POS").add(fPOS.process(line));
					}
					else if(Settings.language.equals("Spanish"))
					{
						processedData.get("POS").add(sPOS.process(line));
					}
					else
					{
						processedData.get("POS").add(pos.process(line));
					}

				}
				if(selectedAnnotators.contains("NER"))
				{
					if(Settings.language.equals("Spanish"))
					{
						processedData.get("NER").add(sNER.process(line));
					}
					else
					{
						processedData.get("NER").add(ner.process(line));
					}
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
