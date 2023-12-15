import java.io.File;

public class HelperMethods {
    

    /** Helper method to get a file extension of a file.
     * If we couldn't find an extention this will null;
     * 
     * @param file which extension will be extracted.
     * @return the extension of the file
     */
    public static String getFileExtension(String filePath) {
        
        // Assume we couldn't get an extension for some reason.
        String ext = null;

        // Get the extension from the file.
        int lastIndexOf = filePath.lastIndexOf(".");
        if (lastIndexOf > 0) {
            ext = filePath.substring(lastIndexOf);
        }

        return ext;
    }

    public static String getFileName(File file) {

        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }

        return fileName;
    }

    public static boolean isSquare(Object[][] matrix){
        //Save the length of the array
        int size = matrix.length;
    
        //Loop over all the rows and Check to see if the given row has the same length
        for (Object[] objects : matrix) {

            if (size != objects.length){ return false; }                
        }
        
        return true;
    }

     /** Helper method to determine if a string is null or empty
     * 
     * @param string the text to be processed
     * @return Returns true if string empty or is null, else returns false.
     */
    public static boolean isStringNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
