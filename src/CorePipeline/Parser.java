package CorePipeline;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class Parser {

	private Properties props = new Properties();
	private StanfordCoreNLP pipeline;


	public Parser()
	{
		props.setProperty("annotators", "tokenize, ssplit, pos");
		pipeline = new StanfordCoreNLP(props);
	}

	public String process(String text)
	{
		Document doc = new Document(text);
		String parser = "";
		for(Sentence sent : doc.sentences())
		{
			parser += sent.parse().toString();
		}
		return parser.equals("") ? "Failed to process this line" : parser;
	}

}