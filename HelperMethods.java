import java.io.File;

public class HelperMethods {

    /**
     * Helper method which attempts to parse given text to an integer
     * 
     * @param text the text to be parsed to an integer
     * @return Returns the parsed integer or null if we couldn't parse the given
     *         text.
     */
    public static Integer tryParseInt(String text) {

        // Assume we couldn't parse our supplied text so return null.
        Integer value = null;

        try {
            value = Integer.parseInt(text);

        } catch (NumberFormatException ex) {
            // Ignore failed parsing as we will return null anyway
        }
        
        return value;
    }

    /**
     * Helper method to get a file extension of a file.
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

    /**
     * Returns the file name without the file extension.
     *
     * @param file the file whose name is to be extracted
     * @return the file name without the file extension
     */
    public static String getFileName(File file) {

        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }

        return fileName;
    }

    /**
     * Helper method to determine if a string is null or empty
     * 
     * @param string the text to be processed
     * @return Returns true if string empty or is null, else returns false.
     */
    public static boolean isStringNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
