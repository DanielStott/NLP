import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ProcessHtml {

	public static void main(String[] args) {
		String url = "http://www.jhbjhbhjbjhb";
		dataFromHtml(url);
	}

	private static String[] tags = { "h1", "h2", "h3", "span", "p", "b", "i", "u" };

	public static String dataFromHtml(String theURL) {
		// tags to search html page for
		// String[] tags = { "h1", "h2", "h3", "span", "p", "b", "i", "u" };
		URL url;
		StringBuilder contentBuilder = new StringBuilder();
		String rawHTML = null;
		String data;
		try {
			// get URL content
			System.out.println("url: " + theURL);
			url = new URL(theURL);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				contentBuilder.append(inputLine);
			}
			br.close();
			// Input String builder to string
			rawHTML = contentBuilder.toString();
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		// Use JSOUP library to extract data from the html string recieved
		Document doc = null;
		try{
			doc = Jsoup.parse(rawHTML);
			// Retain new lines
			// makes html preserve linebreaks and spacing
			doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
			doc.select("br").append("\\n");
			doc.select("p").prepend("\\n");

			// Loop through tag list to get data and add it to string
			String usefulHTML = "";
			for (int i = 0; i < tags.length; i++) {
				Elements x = doc.getElementsByTag(tags[i]);
				usefulHTML += x.text();
			}

			data = usefulHTML.replaceAll("\\\\n", "\n");

		}catch(Exception e){
			return null;
		}
	
		System.out.println(data);
		return data;// Jsoup.clean(s, "", Whitelist.none(), new
					// Document.OutputSettings().prettyPrint(false));
	}
}