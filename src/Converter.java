import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Converter {
	
	public static void main (String[] args){
		HashMap<String, String> xz = new HashMap<String,String>();
		xz.put("sdf", "sdf");
		xz.put("test","fasf");
		process(xz);
	}
	
	public static void process(Map<String,String> x){
		JSONObject json = new JSONObject(x);
		System.out.println(json);
		
	}
	
}
