import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProcessJSON {

	public void process(JSONObject json)
	{
		json = validateJSON(json);

    	
    	JSONArray annotators = json.getJSONArray("annotators");
    	JSONArray files = json.getJSONArray("files");
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
        	
        
    	  if(inputText != null && inputText != "")
          {
          	
          }
    	  else if(url != null && url != "")
          {
          	
          }
    	  else if(fileList != null && !fileList.isEmpty())
    	  {
    		  
    	  }
             	
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
				if(!json.has("files"))
				{
					json.append("files", "");
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
