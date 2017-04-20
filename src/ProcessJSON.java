import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

//This class processes the data returned by the GUI and call the correct classes to process the data.
public class ProcessJSON {

	/**
	 * Takes in a JSONObject and makes sure its correct, once validated calls the required classes to process the data. 
	 * @param  JSONObject - A JSONObject of all of the users select options
	 * @return String - returns a string of processed data.
	 */
	public String process(JSONObject json)
	{
		try
		{
			//Validates the json ensuring it is formated correctly.
			json = validateJSON(json);

			JSONArray annotators = json.getJSONArray("annotators");
			JSONArray files = json.getJSONArray("filePath");
			String outPutFormat = json.get("outPutFormat").toString();
			String inputText = json.get("inputText").toString();
			String url = json.get("url").toString();
			String language = json.getString("language");
			
			//Checks to make sure required values are not empty
			if((annotators != null && annotators.length() != 0) && 
					(files != null && files.length() != 0) && 
					(outPutFormat != null && outPutFormat != "") && 
					(language != null && language != ""))
			{
				Settings.language = language;
				Settings.annotators.clear();
				
				//Adds the selected annotators to the settings
				for(Object annotator: annotators)
				{
					if (annotator instanceof String) 
					{
						Settings.annotators.add((String)annotator);
					}
				}

				List<String> fileList = new ArrayList<String>();
				for(Object file : files)
				{
					if (file instanceof String) 
					{
						fileList.add((String)file);
					}
				}

				
				// Run corepipline using callable new thread
				CorePipeline cp = CorePipeline.CorePipelineThread();
				
				if(inputText != null && inputText.length() > 0)
				{
					List<String> rawData = new ArrayList<String>(Arrays.asList(inputText.split("\r\n"))); 
					//Proccess raw input data
					return cp.processData(rawData, ManageFile.selectFolder()) ? 
							"Successfully processed the input text." : "Failed to process the input text."; //null is temp
				}
				else if(url != null && url.length() > 0)
				{
					//Processes a HTML page and returns text
					return cp.processData(ProcessHtml.dataFromHtml(url), ManageFile.selectFolder()) ? 
							"Successfully Processed the URL." : "Failed to process the URL.";

				}
				else if(fileList != null && !fileList.isEmpty())
				{
					//Loops through each file
					for(String file : fileList)
					{

						if(ManageFile.isDirectory(file))
						{
							//Get every file in directory and process
							return cp.processDataDir(Paths.get(file), ManageFile.selectFolder(), 0) ? 
									"Successfully processed all files in the directory." : "Failed whilst trying to processing the directory";
						}
						else if(ManageFile.isFile(file))
						{
							//Get single file and process
							return cp.processData(Paths.get(file), ManageFile.selectFolder(), 0) ? 
									"Processed File correctly." : "Failed whilst processing the file";
						}
						else return "Failed to find file or directory.";
					}

				}

			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			return "Somthing went wrong! Error code: 101.";
		}
		return "Somthing went wrong! Error code: 100";
	}

	/**
	 * Validates the json ensuring incorrect values are not processed
	 * @param  JSONObject - a json object to validate
	 * @return JSONObject - returns validated json object
	 */
	private static JSONObject validateJSON(JSONObject json)
	{
		if(json.length() != 0)
		{
			if(!json.has("annotators"))
			{
				json.append("annotators", new JSONArray() {});
			}
			else if(json.has("annotators"))
			{
				//checks to make sure the annotator is part of a JSONArray
				if(!(json.get("annotators") instanceof JSONArray))
				{
					String singleAnnotator = json.get("annotators").toString();
					json.remove("annotators");
					json.append("annotators", singleAnnotator);
				}

			}
			if(!json.has("filePath"))
			{
				json.append("filePath", "");
			}
			else if(json.has("filePath"))
			{
				//checks to make sure the annotator is part of a JSONArray
				if(!(json.get("filePath") instanceof JSONArray))
				{
					String singleAnnotator = json.get("filePath").toString();
					json.remove("filePath");
					json.append("filePath", singleAnnotator);
				}

			}
			if(!json.has("outPutFormat"))
			{
				json.append("outPutFormat", "");
			}
			if(!json.has("inputText"))
			{
				json.append("inputText", "");
			}
			if(!json.has("url"))
			{
				json.append("url", "");
			}
			if(!json.has("language"))
			{
				json.append("language", "English");
			}

		}
		else
		{
			json.append("annotators", new JSONArray() {})
			.append("files", new JSONArray() {})
			.append("language", "English")
			.append("outPutFormat", "")
			.append("inputText", "")
			.append("url", "");
		}
		return json;
	}


}
