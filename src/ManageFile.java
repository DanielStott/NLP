import java.awt.FileDialog;
import java.awt.Frame;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import java.io.IOException;
import java.io.OutputStreamWriter;

//This class deals with any interaction with folders or files
public class ManageFile {

	// Make application directory the default path
	public static Path defaultOutput = Paths.get(System.getProperty("user.dir"));
	public static final String pathSlash = OSValidator.isWindows() ? "\\" : "/";

	/**
	 * Selects File
	 * @return Path - returns path of selected file
	 */
	public static Path selectFile()
	{
		final Frame f = new Frame();
		FileDialog fd = new FileDialog(f, "Choose a file", FileDialog.LOAD);
		fd.setDirectory("C:\\Users\\Stott\\Documents");
		fd.setVisible(true);

		return (fd.getDirectory() !=null && fd.getFile() != null) ? Paths.get(fd.getDirectory(), fd.getFile()): null;
	}

	/**
	 * Selects Folder
	 * @return Path - returns path of selected folder
	 */
	public static Path selectFolder()
	{
		// If user is on Mac dont show file chooser - currently having problems with file chooser in mac
		if(OSValidator.isMac()) return null;

		// Initialise FileChooser
		JFileChooser fd = new JFileChooser();
		fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fd.showSaveDialog(fd);

		// From the directory, get the selected file
		return (fd.getCurrentDirectory() !=null) ? Paths.get(fd.getSelectedFile().toString()): null;
	}

	/**
	 * Loop through and find all files in directory with certian extension(s)
	 * @param  Path - directory location
	 * @param  ArrayList<Path> - list of all file paths
	 * @param  String - extension to search for
	 * @return ArrayList<Path> - returns paths of found files
	 */
	public static ArrayList<Path> mapDirectory(Path dirLocation, ArrayList<Path> filePaths, String extenstion)
	{
		File dirFolder = dirLocation.toFile();
		File[] files = dirFolder.listFiles();
		if(files != null)
		{
			for(File file : files)
			{
				if(file.isDirectory())
				{
					mapDirectory(file.toPath(), filePaths, extenstion);
				}
				else if(file.isFile())
				{

					if(file.getName().endsWith(extenstion) &&  file.exists())
					{
						filePaths.add(file.toPath());
					}
				}
			}
		}
		return filePaths;
	}

	/**
	 * Saves contents of a HashMap to a folder. Uses the key as the folder name.
	 * @param  Map<String, List<String>> - Input data
	 * @param  Path - output location
	 * @return Boolean - returns true if successful 
	 */
	public static boolean saveMapToFile(Map<String, List<String>> data, Path outputLocation)
	{
		if(outputLocation == null) outputLocation = defaultOutput;

		Map<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();
		try {

			for(Entry<String, List<String>> entry : data.entrySet())
			{
				String annotatorName = entry.getKey();
				//writers.put(annotatorName,  new BufferedWriter(new FileWriter(String.format("%s%s%s%s", outputLocation, pathSlash, annotatorName, ".txt"))));
				writers.put(annotatorName, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.format("%s%s%s%s", outputLocation, pathSlash, annotatorName, ".txt")), "UTF8")));

				for(String line : entry.getValue())
				{				 
					writers.get(annotatorName).write(line);
					writers.get(annotatorName).newLine(); 		
				}
			}

			for(Entry<String, BufferedWriter> entry : writers.entrySet())
			{
				entry.getValue().close();
			}
		}
		catch(IOException e)
		{
			return false;		
		}

		return true;
	}

	/**
	 * Saves data to file
	 * @param  ArrayList<String> - Data
	 * @param  Path - output location
	 * @return Boolean - returns true if successful 
	 */
	public boolean saveToFile(ArrayList<String> data, Path fileLocation)
	{
		return true;
	}

	/**
	 * Checks if is directory
	 * @param  String - directory location
	 * @return Boolean - returns true if is directory
	 */
	public static boolean isDirectory(String directory)
	{
		return new File(directory).isDirectory();
	}

	/**
	 * Checks if is file
	 * @param  String - file location
	 * @return Boolean - returns true if is file
	 */
	public static boolean isFile(String file)
	{
		return new File(file).isFile();
	}


}
