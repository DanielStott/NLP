package CorePipeline.Chinese;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.international.arabic.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class ChinesePOS {

	private Properties props = new Properties();
	private StanfordCoreNLP pipeline;
	
	
	public ChinesePOS()
	{
		props.setProperty("annotators", "tokenize, ssplit, pos");
		props.setProperty("tokenize.language", "zh");
		props.setProperty("segment.model", "edu/stanford/nlp/models/segmenter/chinese/ctb.gz");
		props.setProperty("segment.sighanCorporaDict", "edu/stanford/nlp/models/segmenter/chinese");
		props.setProperty("segment.serDictionary", "edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz");
		props.setProperty("segment.sighanPostProcessing", "true");
		props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/chinese-distsim/chinese-distsim.tagger");
		pipeline = new StanfordCoreNLP(props);
	}
	
	public String process(String text)
	{
		if(text.equals("\n")) return "\n";
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
	    	String pos = "";
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	         pos += String.format("[\"%s\" %s]", token.originalText(), token.get(PartOfSpeechAnnotation.class));
	      }

	      return pos;
	    }
	   
	    return "Failed to process this line";
	}
}