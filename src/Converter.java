import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Converter {
	public static String process(Map<String, List<String>> mapToConvert){
		JSONObject json = new JSONObject(mapToConvert);
		return json.toString();
	}
	
}
