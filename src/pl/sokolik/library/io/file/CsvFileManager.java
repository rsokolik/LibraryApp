package pl.sokolik.library.io.file;

import pl.sokolik.library.Exception.DataExportException;
import pl.sokolik.library.Exception.DataImportException;
import pl.sokolik.library.Exception.InvalidDataException;
import pl.sokolik.library.model.*;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

public class CsvFileManager implements FileManager {

    private static final String FILE_NAME = "Library.csv";
    private static final String USERS_FILE_NAME = "Library_user.csv";

    //metoda odczytuje każdą linię tekstu z pliku, sprawdza obiekt i dodaje do odpowiedniej publikacji w bibliotece
    @Override
    public Library importData() {
        Library library = new Library();

        importPublications(library);
        importUsers(library);

        return library;
    }

    private void importPublications(Library library) {
        try(
                var scanner = new Scanner(new File(FILE_NAME))
        ){
            //nie wiadomo ile jest danych w pliku stąd wczytywanie do końca
            while(scanner.hasNextLine()){ //dopóki jest jakiś nowy wiersz do odczytu (hasNextLine)
                String line = scanner.nextLine(); //odczytaj nową linię
                Publication publication = createObjectFromString(line);
                library.addPublication(publication);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + FILE_NAME);
        }
    }

    private void importUsers(Library library) {
        try(
                var scanner = new Scanner(new File(USERS_FILE_NAME))
        ){
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                LibraryUser libraryUser = createUserFromString(line);
                library.addUser(libraryUser);
            }
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + USERS_FILE_NAME);
        }
    }

    private LibraryUser createUserFromString(String csvText) {
        String[] split = csvText.split(";");
        String firstName = split[0];
        String lastName = split[1];
        String pesel = split[2];

        return new LibraryUser(firstName, lastName, pesel);
    }

    //Książka;W pustyni i w puszczy;Greg;2010;Henryk Sienkiewicz;324;1234567890123
    //1 - rozdzielamy frazy między ;
    //2 - split() to metoda rozdzielająca wyrazy między separatorem
    private Publication createObjectFromString(String line) {
        String[] split = line.split(";");
        String firstObject = split[0]; //weryfikacja czy pierwszy wyraz to KSIĄŻKA czy MAGAZYN
        if (Book.TYPE.equals(firstObject)){ //jeżeli TYP książki (z Book) to ten, który w pierwszym wyrazie z pliku to Book
            return createBook(split);
        } else if (Magazine.TYPE.equals(firstObject)) {
            return createMagazine(split);
        }
        throw new InvalidDataException("Nieznany typ publikacji " + firstObject); //np. pierwszy wyraz jako ASDAFSDGASDWAR
    }

    private Magazine createMagazine(String[] data) {
        String title = data[1];
        String publisher = data[2];
        int year = Integer.parseInt(data[3]); //parsowanie na INT ze względu na to, że obiekty będą Stringami
        int month = Integer.parseInt(data[4]);
        int day = Integer.parseInt(data[5]);
        String language = data[6];

        return new Magazine(title, publisher, language, year, month, day);
    }

    private Book createBook(String[] data) {
        String title = data[1];
        String publisher = data[2];
        int year = Integer.parseInt(data[3]);
        String author = data[4];
        int pages = Integer.parseInt(data[5]);
        String isbn = data[6];

        return new Book(title, author, year, pages, publisher, isbn);
    }

    @Override
    public void exportData(Library library) {
        exportPublications(library);
        exportUsers(library);
    }

    private void exportPublications(Library library) {
        Collection<Publication> publications = library.getPublications().values(); //values aby iterować po wartościach
        exportToCsv(publications, FILE_NAME);
    }

    private void exportUsers(Library library) {
        Collection<LibraryUser> users = library.getUsers().values(); //values aby iterować po wartościach
        exportToCsv(users, USERS_FILE_NAME);
    }
    //kolekcja (collection) dostarczana jako parametr metody
    private <T extends CsvConvertible> void exportToCsv(Collection<T> collection, String fileName) {
        try( //TRY WITH RESOURCES
             var fileWriter = new FileWriter(fileName);
             var bufferedWriter = new BufferedWriter(fileWriter)
        ){
            for (T element : collection) {
                bufferedWriter.write(element.toCsv()); //toCsv zdefiniowana w interfejsie CsvConvertible
                bufferedWriter.newLine();
            }
        } catch (IOException e){
            throw new DataExportException("Błąd zapisu danych do pliku " + fileName);
        }
    }
}
