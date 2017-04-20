package CorePipeline;

import java.util.Properties;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

//This is the Lemma annotator class
public class Sentiment {

	private Properties props = new Properties();
	private StanfordCoreNLP pipeline;
	private int lineNumb = 0;

	//Adds properties when constructed
	public Sentiment()
	{
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}

	/**
	 * Process a string of text and return the porcessed data. 
	 * @param  String - A string of data to be processed
	 * @return String - returns a string of processed data.
	 */
	public String process(String text)
	{
		//Sentiment value
		int mainSentiment = 0;
		if (text != null && text.length() > 0) 
		{
			int longest = 0;
			Annotation annotation = pipeline.process(text);
			//Loops through the text
			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) 
			{
				//Creates a tree using the sentiment annotator
				Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
				//Get the sentiment or or -1 if none
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String partText = sentence.toString();
				if (partText.length() > longest) 
				{
					mainSentiment = sentiment;
					longest = partText.length();
				}

			}
		}
		//Returns the sentiment
		return String.format("[Line %s has a sentiment value of %s]", ++lineNumb, String.valueOf(mainSentiment));
	}
}
