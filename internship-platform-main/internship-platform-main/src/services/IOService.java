package services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class IOService<T extends Parsable<T> & CSVConvertible> {

    private FileReader fileReader;

    private PrintWriter printWriter;

    private final Supplier<T> ctor;

    private T object;

    public IOService(Supplier<T> tSupplier) {
        this.ctor = Objects.requireNonNull(tSupplier);
        object = this.ctor.get();
    }

    // usage: IOService<Student> studentIOService = new IOService<>(Student::new);

    private FileReader initReader(String fileName) {
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return null;
        }
        return fileReader;
    }

    public List<T> retrieveObjects(String fileName) throws IOException {

        FileReader fileReader = initReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<T> objectList = new ArrayList<>();

        String line = bufferedReader.readLine();
        while (line != null) {
            object = object.parseCSVLine(line);
            objectList.add(object);
            line = bufferedReader.readLine();
        }

        return objectList;
    }


    private PrintWriter initWriter(String fileName) {

        try {
            File outputFile = new File(fileName);
            printWriter = new PrintWriter(outputFile);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return null;
        }
        return printWriter;
    }

    public void saveObjects(String fileName, List<T> objectList) {

        PrintWriter printWriter = initWriter(fileName);

        if (printWriter == null) {
            System.out.println("Nu s-a putut deschide fisierul de output");
            return;
        }

        objectList
                .stream()
                .map(CSVConvertible::convertToCSV)
                .forEach(printWriter::println);

        printWriter.close();
    }


}
