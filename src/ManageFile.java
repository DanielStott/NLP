import java.awt.FileDialog;
import java.awt.Frame;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class ManageFile {

	public Path defaultOutput = Paths.get(System.getProperty("user.dir"));
	public static final String pathSlash = OSValidator.isWindows() ? "\\" : "/";
	
	public static Path selectFile()
	{
    	final Frame f = new Frame();
    	FileDialog fd = new FileDialog(f, "Choose a file", FileDialog.LOAD);
    	fd.setDirectory("C:\\Users\\Stott\\Documents");
    	fd.setVisible(true);

    	return (fd.getDirectory() !=null && fd.getFile() != null) ? Paths.get(fd.getDirectory(), fd.getFile()): null;
	}
	
	public static Path selectFolder()
	{
<<<<<<< HEAD
		
		if (OSValidator.getOS().equals("osx")){
			return null;
		}
		
    	JFileChooser fd = new JFileChooser(".");
    	fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JDialog dialog = new JDialog();  
		
		fd.showSaveDialog(dialog);

=======
		if(OSValidator.isMac()) return null;
		
    	JFileChooser fd = new JFileChooser();
    	fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	fd.showSaveDialog(fd);
    	
    	
>>>>>>> master
    	return (fd.getCurrentDirectory() !=null) ? Paths.get(fd.getSelectedFile().toString()): null;
	}
	
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
	
	public boolean saveMapToFile(Map<String, List<String>> data, Path outputLocation)
	{
		if(outputLocation == null) outputLocation = defaultOutput;
		
		Map<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();
		try {
				    
			for(Entry<String, List<String>> entry : data.entrySet())
			{
				String annotatorName = entry.getKey();
				writers.put(annotatorName,  new BufferedWriter(new FileWriter(String.format("%s%s%s%s", outputLocation, pathSlash, annotatorName, ".txt"))));
				 
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
	
	
	public boolean saveToFile(ArrayList<String> data, Path fileLocation)
	{
		return true;
	}
	
	public static boolean isDirectory(String directory)
	{
		return new File(directory).isDirectory();
	}
	
	public static boolean isFile(String file)
	{
		return new File(file).isFile();
	}
	
	
}
