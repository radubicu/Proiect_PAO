package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoggingService <T extends Loggable> {

    private static String className;

    private static FileWriter fileWriter;

    public LoggingService(Class<T> typeClass)
    {
        className = typeClass.getSimpleName();
    }

    public static void logAction(String actionName, LocalDateTime timestamp)
    {
        try {
            fileWriter = new FileWriter(className + ".csv", true);
            fileWriter
                    .append(actionName)
                    .append(String.valueOf(','))
                    .append(timestamp.toString())
                    .append(String.valueOf('\n'));

            fileWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

}
