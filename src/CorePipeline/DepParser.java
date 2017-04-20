package CorePipeline;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;

//This is the Dependancy parser annotator class
public class DepParser {

	private DependencyParser parser;
	private MaxentTagger tagger;
	private int lineNumb = 0;

	//Adds properties when constructed
	public DepParser()
	{
		parser = DependencyParser.loadFromModelFile(DependencyParser.DEFAULT_MODEL);
		tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
	}

	/**
	 * Process a string of text and return the porcessed data. 
	 * @param  String - A string of data to be processed
	 * @return String - returns a string of processed data.
	 */
	public String process(String text)
	{
	    GrammaticalStructure gs = null;
	    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
	    
	    for (List<HasWord> sentence : tokenizer)
	    {
		//Processes word using maxent tagger
	        List<TaggedWord> tagged = tagger.tagSentence(sentence);
		//Adds gramatical structure
	      	gs = parser.predict(tagged);	      		
	    }
	    //Gets dependencies
	    String output = gs.typedDependencies().toString();
	    //Returns dependancies
	    return output.equals("") ? "Failed to process this line" : String.format("[Line %s [%s]]", ++lineNumb, output);
	}

}
