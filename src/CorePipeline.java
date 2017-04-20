
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
//Process text from different source locations and create instances of annotator classes to be analysed.
public class CorePipeline 
{
	//inner class to store a annotator class
	public class ClassStore<classObject, classInstance> 
	{
		public Class<?> classObject;
		public Object classInstance;

		//constructor
		public ClassStore(Class<?> Object, Object Instance) 
		{
			this.classObject = Object;
			this.classInstance = Instance;
		}
	}

	//list of of the class store classes
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

	/**
	 * processes all the files in the directory location
	 * @param  dirLocation input directory location
	 * @param  outputLocation output save location
	 * @param  output format type
	 * @return  boolean
	 */
	public boolean processDataDir(Path dirLocation, Path outputLocation, int outputMode)
	{
		try
		{
			//array of the different file locations
			ArrayList<Path> textFileLocations = new ArrayList<Path>();

			//find all usable file types in directory
			textFileLocations = ManageFile.mapDirectory(dirLocation, new ArrayList<Path>(), ".txt");

			for(Path filePath : textFileLocations)
			{
				//passes each file in the directory to processed
				processData(filePath, outputLocation, outputMode);

			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/*
	 * process file 
	 * @param  dirLocation input file location
	 * @param  outputLocation output save location
	 * @param  output format type
	 * @return  boolean
	 */
	public boolean processData(Path fileLocation, Path outputLocation, int outputMode)
	{
		try 
		{	//read in text from file
			List<String> rawData = Files.readAllLines(fileLocation, StandardCharsets.UTF_8);

			//Creates a folder of the same name as file being processed		
			outputLocation = Paths.get(outputLocation.toString(), ManageFile.pathSlash, fileLocation.toString().substring(fileLocation.toString().lastIndexOf(ManageFile.pathSlash) + 1, fileLocation.toString().lastIndexOf('.')));

			if(!Files.exists(outputLocation))
			{
				Files.createDirectory(outputLocation);
			}
			//passes to a map to be processed
			Map<String , List<String>> allData = processData(rawData); 
			//save file
			ManageFile.saveMapToFile(allData, outputLocation);
			//pass results to GUI
			GUI.setResult(allData);


			return true;
		}
		catch(IOException e)
		{
			System.out.print(e);
			return false;
		}
	}

	/*
	 * process input text and URL
	 * @param  rawData Input text 
	 * @param  dirLocation input file location
	 * @param  outputLocation output save location
	 * @return  boolean
	 */
	public boolean processData(List<String> rawData, Path outputLocation)
	{ 
		try
		{
			//passed to map to processed by annotaotrs
			Map<String , List<String>> allData = processData(rawData);
			//save file
			ManageFile.saveMapToFile(allData, outputLocation); 
			//display in GUI
			GUI.setResult(allData);
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}

		return true;
	}


	/*
	 * process and passes the data to the annotators to be analysed
	 * @param  rawData Input text 
	 */
	public Map<String, List<String>> processData(List<String> rawData)
	{ 
		Map<String, List<String>> processedData = new HashMap<String, List<String>>();
		//for loop of annotators selected by the user
		for(String annotator : Settings.annotators)
		{
			if(!processedData.containsKey(annotator))
			{
				//creates hashmap of the annotators as a key and the data as the value
				processedData.put(annotator, new ArrayList<String>());
			}
		}

		//loops through the rawdata
		for(String line : rawData)
		{
			if(line != null && !line.equals(""))
			{		
				try 
				{	//loop through the arraylist of tuples
					for(ClassStore<Class<?>, Object> cs: classList)
					{
						//gets the annotator(key) from hashmap and inserts processdata by invoking the process function 
						//inside the store class instance.
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


	//initalises an instance of corepipeline on a new thread.
	public static CorePipeline CorePipelineThread() throws Exception 
	{     
		ExecutorService service = Executors.newFixedThreadPool(1);
		try 
		{
			CorePipeline cp = service.submit(new Callable<CorePipeline>() 
			{
				public CorePipeline call() throws Exception 
				{
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
