package CorePipeline;

import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;

public class DepParser {

	private Properties props = new Properties();
	private StanfordCoreNLP pipeline;
	private DependencyParser parser;
	private MaxentTagger tagger;

	public DepParser()
	{
		parser = DependencyParser.loadFromModelFile(DependencyParser.DEFAULT_MODEL);
		tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
	}

	public String process(String text)
	{
		/*Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    pipeline.annotate(document);

	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	   
	    for(CoreMap sentence: sentences) 
	    {
	    	
	    	gs = parser.predict(sentence);
	    
	    }*/
		 GrammaticalStructure gs = null;
	    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
	    
	    for (List<HasWord> sentence : tokenizer) {
	      List<TaggedWord> tagged = tagger.tagSentence(sentence);
	      		gs = parser.predict(tagged);

	      		
	    	}
	    String output = gs.typedDependencies().toString();
	    return output.equals("") ? "Failed to process this line" : output;
	}

}