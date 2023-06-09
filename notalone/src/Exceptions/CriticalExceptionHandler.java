package Exceptions;

/*!
    \brief Critical level error handling.

    Logs the error in a file and exits the application with -1 as the error code.
 */
public class CriticalExceptionHandler {

    public static void handle(Exception e) {
        WriteErrorInFile.writeError(e);
        System.exit(-1);
    }
}
