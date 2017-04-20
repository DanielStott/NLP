package CorePipeline;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

//This is the Named Entity Recognition class
public class NER {
	private Properties props = new Properties();
	private StanfordCoreNLP pipeline;
	private int lineNumb = 0;

	//Adds properties when constructed
	public NER()
	{
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
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
			String ner = "";

			for (CoreLabel token: sentence.get(TokensAnnotation.class)) 
			{
				//Checks to see if the word is seen as a Name
				if(!token.get(NamedEntityTagAnnotation.class).equals("O"))
				{
					//Adds Name to string with the original to compare.
					ner += String.format("[\"%s\" defined as \"%s\"]", token.originalText(), token.get(NamedEntityTagAnnotation.class));
				}
			}
			//Returns data
			return String.format("[Line %s %s]", ++lineNumb, ner);
		}

		return "Failed to process this line";
	}
}
