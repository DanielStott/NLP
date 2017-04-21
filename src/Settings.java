import java.util.ArrayList;
import java.util.List;

//Settings class to store user selected settings.
public class Settings 
{
	public static String language = "English";
	public static String dbString = "";
	public static double GUIHeight = 600;
	public static double GUIWidth = 800;
	public static List<String> annotators = new ArrayList<String>();
	//This setting currently doesn't do anything as penPrint is hard coded to true currently on the ConParser.
	public static boolean penPrint = false;
}
