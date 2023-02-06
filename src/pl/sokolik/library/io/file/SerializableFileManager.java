package pl.sokolik.library.io.file;

import pl.sokolik.library.Exception.DataExportException;
import pl.sokolik.library.Exception.DataImportException;
import pl.sokolik.library.model.Library;

import java.io.*;

public class SerializableFileManager implements FileManager {

    //plik tworzony na sztywno
    private static final String FILE_NAME = "Library.txt";

    @Override
    public Library importData() {
        //blok TryWithResources po to aby zamknąć strumień wejścia/wyjścia danych
        try(
                FileInputStream fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (Library) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + FILE_NAME);
        } catch (IOException e) {
            throw new DataImportException("Błąd odczytu danych z pliku " + FILE_NAME);
        } catch (ClassNotFoundException e) {
            throw new DataImportException("Niezgodny typ danych w pliku " + FILE_NAME);
        }
    }

    @Override
    public void exportData(Library library) {
        //blok TryWithResources po to aby zamknąć strumień wejścia/wyjścia danych
        try(
                FileOutputStream fos = new FileOutputStream(FILE_NAME); //wskazanie do jakiego pliku zapisujemy
                ObjectOutputStream oos = new ObjectOutputStream(fos); //opakowanie w object potrzebne do serializacji
        ) {
            oos.writeObject(library);
        } catch (FileNotFoundException e) {
            throw new DataExportException("Brak pliku " + FILE_NAME);
        } catch (IOException e) {
            throw new DataExportException("Błąd zapisu danych do pliku " + FILE_NAME);
        }
    }
}
