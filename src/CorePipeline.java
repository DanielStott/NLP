
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//https://blog.openshift.com/day-20-stanford-corenlp-performing-sentiment-analysis-of-twitter-using-java/ <-- read later for sentiment example

public class CorePipeline 
{

	public class ClassStore<classObject, classInstance> {
	    public Class<?> classObject;
	    public Object classInstance;
	    
	    public ClassStore(Class<?> Object, Object Instance) {
	        this.classObject = Object;
	        this.classInstance = Instance;
	      }
	}

	private ArrayList<ClassStore<Class<?>, Object>> classList = new ArrayList<ClassStore<Class<?>, Object>>();

	public CorePipeline(List<String> annotators) throws Exception
	{


		try
		{
			//Only creates an instance of the class if annotator is selected
			for(String annotator : annotators)
			{
				Class<?> annotatorClass = Settings.language.equals("English") ? 
						Class.forName(String.format("CorePipeline.%s", annotator)) 
						: Class.forName(String.format("CorePipeline.%s.%s", Settings.language, annotator));
				classList.add(new ClassStore<Class<?>, Object>(annotatorClass, annotatorClass.newInstance()));

			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			throw e;
		}

	}

	public boolean processDataDir(Path dirLocation, Path outputLocation, int outputMode)
	{
		try
		{
			ArrayList<Path> textFileLocations = new ArrayList<Path>();
			textFileLocations = ManageFile.mapDirectory(dirLocation, new ArrayList<Path>(), ".txt");

			for(Path filePath : textFileLocations)
			{
				processData(filePath, outputLocation, outputMode);

			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public boolean processData(Path fileLocation, Path outputLocation, int outputMode)
	{
		try 
		{
			List<String> rawData = Files.readAllLines(fileLocation, StandardCharsets.UTF_8);

			//Creates a folder of the same name as file being processed		
			outputLocation = Paths.get(outputLocation.toString(), ManageFile.pathSlash, fileLocation.toString().substring(fileLocation.toString().lastIndexOf(ManageFile.pathSlash) + 1, fileLocation.toString().lastIndexOf('.')));

			if(!Files.exists(outputLocation))
			{
				Files.createDirectory(outputLocation);
			}

			Map<String , List<String>> allData = processData(rawData); 
			ManageFile.saveMapToFile(allData, outputLocation);
			GUI.setResult(allData);


			return true;
		}
		catch(IOException e)
		{
			System.out.print(e);
			return false;
		}
	}

	public boolean processData(List<String> rawData, Path outputLocation)
	{ 
		try
		{
			Map<String , List<String>> allData = processData(rawData);
			ManageFile.saveMapToFile(allData, outputLocation); 
			GUI.setResult(allData);
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}

		return true;
	}

	public Map<String, List<String>> processData(List<String> rawData)
	{ 
		Map<String, List<String>> processedData = new HashMap<String, List<String>>();
		for(String annotator : Settings.annotators)
		{
			if(!processedData.containsKey(annotator))
			{
				processedData.put(annotator, new ArrayList<String>());
			}
		}

		for(String line : rawData)
		{
			if(line != null && !line.equals(""))
			{		
				try 
				{
					for(ClassStore<Class<?>, Object> cs: classList)
					{
						processedData.get(cs.classObject.getName().substring(cs.classObject.getName().lastIndexOf(".") + 1))
						.add((String) cs.classObject.getDeclaredMethod("process", String.class).invoke(cs.classInstance, line));
					}
				} 
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) 
				{
					e.printStackTrace();
				}
			}
		}
		return processedData;
	}

	public static CorePipeline CorePipelineThread() throws Exception {     
		ExecutorService service = Executors.newFixedThreadPool(1);
		try {
			CorePipeline cp = service.submit(new Callable<CorePipeline>() {
				public CorePipeline call() throws Exception {
					return new CorePipeline(Settings.annotators);
				}
			}).get();
			service.shutdown();
			return cp;
		} 
		catch (Exception e) 
		{
			System.out.println(e);
			throw e;
		}	    
	}

}
