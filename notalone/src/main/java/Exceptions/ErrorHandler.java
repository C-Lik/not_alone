package Exceptions;

import java.io.FileWriter;
import java.io.IOException;

/*!
    \brief Writes errors in a files.

    It provides a static method that opens a file and writes the received error as parameter.
 */
public class ErrorHandler {

    public static void writeErrorToFile(Exception e) {
        try {
            FileWriter myWriter = new FileWriter("error.txt");
//            myWriter.write("");
            if(e.getMessage() != null)
                myWriter.write(e.getMessage());
            for (StackTraceElement elem : e.getStackTrace()) {
                myWriter.append(elem.toString()).append("\n");
            }
            myWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
