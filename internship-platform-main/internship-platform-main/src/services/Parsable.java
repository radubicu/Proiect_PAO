package services;

import exceptions.IncorrectEmailException;

public interface Parsable<T> {
    T parseCSVLine(String line);
}
