//Reference: http://stackoverflow.com/questions/14288185/detecting-windows-or-linux
	/**
	 * This class contains functions which check what operating system is running. 
	 */
public class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    /**
    * Checks if is a windows OS. 
    *
    * @return Bolean - returns true if OS is windows
    */
    public static boolean isWindows() 
    {
        return (OS.indexOf("win") >= 0);
    }

    /**
    * Checks if is a Mac OS. 
    *
    * @return Bolean - returns true if OS is Mac
    */
    public static boolean isMac() 
    {
        return (OS.indexOf("mac") >= 0);
    }

     /**
    * Checks if is a Unix OS. 
    *
    * @return Bolean - returns true if OS is Unix
    */
    public static boolean isUnix() 
    {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    /**
    * Checks if is a Solaris OS. 
    *
    * @return Bolean - returns true if OS is Solaris
    */
    public static boolean isSolaris() 
    {
        return (OS.indexOf("sunos") >= 0);
    }
    
    /**
    * Checks the current OS
    *
    * @return String - returns a string of the running OS
    */
    public static String getOS()
    {
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "unix";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }
}
