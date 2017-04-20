
public class Initialise {

        /**
     * This function initialises the program on launch
     *
     * @String[]  Launch paramaters
     * @param  name the location of the image, relative to the url argument
     * @return      the image at the specified URL
     * @see         Image
     */
    public static void main(String[] args) throws Exception 
    { 
    	GUI browser = new GUI();
    	
        //opens the browser
    	browser.open();
    } 
}
