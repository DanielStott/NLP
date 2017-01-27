import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.List;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ManageFile {

	public static Path selectFile()
	{
    	final Frame f = new Frame();
    	FileDialog fd = new FileDialog(f, "Choose a file", FileDialog.LOAD);
    	fd.setDirectory("C:\\Users\\Stott\\Documents");
    	fd.setVisible(true);

    	return (fd.getDirectory() !=null && fd.getFile() != null) ? Paths.get(fd.getDirectory(), fd.getFile()): null;
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
	
	public boolean saveToFile(ArrayList<String> data, Path fileLocation)
	{
    	
		return true;
	}
	
}
