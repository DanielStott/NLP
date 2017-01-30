import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProcessJSON {

	public boolean process(JSONObject json)
	{
		try
		{
			CorePipeline cp = new CorePipeline();
			
			json = validateJSON(json);
			
	    	JSONArray annotators = json.getJSONArray("annotators");
	    	JSONArray files = json.getJSONArray("filePath");
	    	String outPutFormat = json.get("outPutFormat").toString();
	    	String inputText = json.get("inputText").toString();
	    	String url = json.get("url").toString();
	    	if((annotators != null && annotators.length() != 0) && 
	    			(files != null && files.length() != 0) && 
	    			(outPutFormat != null && outPutFormat != ""))
	    	{
	        	List<String> annotatorList = new ArrayList<String>();
	        	for(Object annotator: annotators)
	        	{
	        	    if (annotator instanceof String) 
	        	    {
	        	    	annotatorList.add((String)annotator);
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
	        	
	        	
	        	
	    	  if(inputText != null && inputText.length() > 0)
	          {
	    		  List<String> rawData = new ArrayList<String>(Arrays.asList(inputText.split("\r\n"))); 
	    		
	          	  cp.processData(rawData, annotatorList, null); //null is temp
	          }
	    	  else if(url != null && url.length() > 0)
	          {
	    		  cp.processData(ProcessHtml.dataFromHtml(url), annotatorList, null);
	          	
	          }
	    	  else if(fileList != null && !fileList.isEmpty())
	    	  {
	    		  Path defaultDir = Paths.get(System.getProperty("user.dir"));
	    		  for(String file : fileList)
	    		  {
	    			  
	    			  if(ManageFile.isDirectory(file))
	    			  {
	    				  cp.processDataDir(Paths.get(file), annotatorList, defaultDir, 0);
	    			  }
	    			  else if(ManageFile.isFile(file))
	    			  {
	    				  cp.processData(Paths.get(file), annotatorList, defaultDir, 0);
	    			  }
	    		  }
	    		  
	    	  }
	             	
	    	}
	    	return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	private JSONObject validateJSON(JSONObject json)
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
        
        }
		else
		{
			json.append("annotators", new JSONArray() {})
			.append("files", new JSONArray() {})
			.append("outPutFormat", "")
			.append("inputText", "")
			.append("url", "");
		}
		return json;
	}
	
}
