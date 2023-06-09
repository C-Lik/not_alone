package Exceptions;

/*!
    \brief Non-Critical level error handling.

    Logs the error in a file and lets the application continue.
 */
public class NotCriticalExceptionHandler {

    public static void handle(Exception e) {
        WriteErrorInFile.writeError(e);
    }
}
