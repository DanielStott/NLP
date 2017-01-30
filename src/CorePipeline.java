import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;


//https://blog.openshift.com/day-20-stanford-corenlp-performing-sentiment-analysis-of-twitter-using-java/ <-- read later for sentiment example
public class CorePipeline 
{
	private ManageFile mf = new ManageFile();

	  public boolean processDataDir(Path dirLocation, List<String> selectedAnnotators, Path outputLocation, int outputMode)
	  {
		try
		{
			ArrayList<Path> textFileLocations = new ArrayList<Path>();
			textFileLocations = ManageFile.mapDirectory(dirLocation, new ArrayList<Path>(), ".txt");
			
			for(Path filePath : textFileLocations)
			{
				processData(filePath, selectedAnnotators, outputLocation, outputMode);

			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	  }
	 
	  public boolean processData(Path fileLocation, List<String> selectedAnnotators, Path outputLocation, int outputMode)
	  {
       	try 
       	{
    	 	List<String> rawData = Files.readAllLines(fileLocation, StandardCharsets.UTF_8);
    	 	
    	 	//Creates a folder of the same name as file being processed
    	 	outputLocation = Paths.get(outputLocation.toString(), "\\", fileLocation.toString().substring(fileLocation.toString().lastIndexOf('\\') + 1, fileLocation.toString().lastIndexOf('.')));
    	 	if(!Files.exists(outputLocation))
    	 	{
    	 		Files.createDirectory(outputLocation);
    	 	}
    	 	
    	 	Map<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();
    	 	for(int i = 0; i < selectedAnnotators.size(); i++)
    	 	{
    	 		String name = selectedAnnotators.get(i);
    	 		writers.put(name,  new BufferedWriter(new FileWriter(String.format("%s\\%s%s", outputLocation, name, ".txt"))));
    	 	}
    	 	
    	 	mf.saveMapToFile(processData(rawData, selectedAnnotators), outputLocation);

	    	return true;
    	}
    	catch(IOException e)
    	{
    		System.out.print(e);
    		return false;
    	}
	  }
	  
	  public boolean processData(List<String> rawData, List<String> selectedAnnotators, Path outputLocaton)
	  { 
	  	  try{
	  		mf.saveMapToFile(processData(rawData, selectedAnnotators), outputLocaton); 
	  	  }
	  	  catch(Exception e)
	  	  {
	  		  return false;
	  	  }
		  
		  return true;
	  }
	  
	  public Map<String, List<String>> processData(List<String> rawData, List<String> selectedAnnotators)
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
			        Document doc = new Document(line);
			    	for (Sentence sent : doc.sentences()) 
			    	{  

	    				if(selectedAnnotators.contains("Parser"))
	    				{
	    					processedData.get("Parser").add(parseSentence(sent));
	    				}
	    				if(selectedAnnotators.contains("Lemma"))
	    				{
	    					processedData.get("Lemma").add(lemmaSentence(sent));
	    				}
	    				if(selectedAnnotators.contains("POS"))
	    				{
	    					processedData.get("POS").add(posSentence(sent));
	    				}
	    				if(selectedAnnotators.contains("NER"))
	    				{
		    				processedData.get("NER").add(nerSentence(sent));
	    				}
	    				if(selectedAnnotators.contains("Sentiment"))
	    				{
		    				processedData.get("Sentiment").add(String.valueOf(sentiment(sent.toString())));
	    				}
	    			}
	    		}
	        }
       		return processedData;
	   }
	  
		public int sentiment(String line)
		{
	        Properties props = new Properties();
	        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
	        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	        int mainSentiment = 0;
	        if (line != null && line.length() > 0) 
	        {
	            int longest = 0;
	            Annotation annotation = pipeline.process(line);
	            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) 
	            {
	                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
	                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
	                String partText = sentence.toString();
	                if (partText.length() > longest) 
	                {
	                    mainSentiment = sentiment;
	                    longest = partText.length();
	                }
	 
	            }
	        }
	        return mainSentiment;
	  }

	  private String parseSentence(Sentence sentence)
	  {
		  return sentence.parse().toString();
	  }
	  
	  private String lemmaSentence(Sentence sentence)
	  {
		  return sentence.lemmas().toString();
	  }
	  
	  private String posSentence(Sentence sentence)
	  {
		  return sentence.posTags().toString();
	  }
	
	  private String nerSentence(Sentence sentence)
	  {
		  return sentence.nerTags().toString();
	  }

}
