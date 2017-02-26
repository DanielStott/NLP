package CorePipeline.Spanish;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class SpanishNER {
	private Properties props = new Properties();
	private StanfordCoreNLP pipeline;
	
	
	public SpanishNER()
	{
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
		props.setProperty("tokenize.language", "es");
		props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/spanish/spanish-distsim.tagger");
		props.setProperty("ner.model", "edu/stanford/nlp/models/ner/spanish.ancora.distsim.s512.crf.ser.gz");
		pipeline = new StanfordCoreNLP(props);
		
	}
	
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
	    	  if(!token.get(NamedEntityTagAnnotation.class).equals("O"))
	    	  {
	    		 lemma += String.format("[\"%s\" defined as \"%s\"]", token.originalText(), token.get(NamedEntityTagAnnotation.class));
	    	  }
	      }

	      return lemma;
	    }
	   
	    return "Failed to process this line";
	}
}
