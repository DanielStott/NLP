import java.util.ArrayList;
import java.util.List;


//declaring a public class for settings

public class Settings 
{
	// these static methods belong to the class
	public static String language = "English";
	public static String dbString = "";
	public static double GUIHeight = 600;
	public static double GUIWidth = 800;
	public static List<String> annotators = new ArrayList<String>();
	//This setting currently doesn't do anything as penPrint is hard coded to true currently on the ConParser.
	public static boolean penPrint = false;
}
