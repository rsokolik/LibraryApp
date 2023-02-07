package pl.sokolik.library.io.file;

import pl.sokolik.library.Exception.DataExportException;
import pl.sokolik.library.Exception.DataImportException;
import pl.sokolik.library.Exception.InvalidDataException;
import pl.sokolik.library.model.Book;
import pl.sokolik.library.model.Library;
import pl.sokolik.library.model.Magazine;
import pl.sokolik.library.model.Publication;

import java.io.*;
import java.util.Scanner;

public class CsvFileManager implements FileManager {

    private static final String FILE_NAME = "Library.csv";

    //metoda odczytuje każdą linię tekstu z pliku, sprawdza obiekt i dodaje do odpowiedniej publikacji w bibliotece
    @Override
    public Library importData() {
        Library library = new Library();
        try(
                Scanner scanner = new Scanner(new File(FILE_NAME))
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
        return library;
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
        Publication[] publications = library.getPublication();
        try( //TRY WITH RESOURCES
            FileWriter fileWriter = new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        ){
            for (Publication publication : publications) {
                bufferedWriter.write(publication.toCsv()); //zamiana na Stringa
                bufferedWriter.newLine(); //przejście do kolejnego wiersza
            }
        } catch (IOException e){
            throw new DataExportException("Błąd zapisu danych do pliku " + FILE_NAME);
        }
    }
}
