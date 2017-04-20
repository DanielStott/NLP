package CorePipeline;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

//This is the Lemma annotator class
public class Lemma {
	private Properties props = new Properties();
	private StanfordCoreNLP pipeline;
	private int lineNumb = 0;
	
	//Adds properties when constructed
	public Lemma()
	{
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		pipeline = new StanfordCoreNLP(props);
	}
	
	/**
	 * Process a string of text and return the porcessed data. 
	 * @param  String - A string of data to be processed
	 * @return String - returns a string of processed data.
	 */
	public String process(String text)
	{
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    pipeline.annotate(document);

	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	    	String lemma = "";
	    	
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		  //if the word differs from the original
	    	 if(!token.originalText().equals((token.get(LemmaAnnotation.class))))
	    	 {
			 //Add the converted word
	    		 lemma += String.format("[Line %s [\"%s\" at location %s changed to \"%s\"]]", ++lineNumb, token.originalText(), token.index(), token.get(LemmaAnnotation.class));
	    	 }
	      }
	      //Return the converted word
	      return lemma;
	    }
	   
	    return "Failed to process this line";
	}
}
