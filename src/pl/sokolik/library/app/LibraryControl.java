package pl.sokolik.library.app;

import pl.sokolik.library.Exception.DataExportException;
import pl.sokolik.library.Exception.DataImportException;
import pl.sokolik.library.Exception.InvalidDataException;
import pl.sokolik.library.Exception.NoSuchOptionException;
import pl.sokolik.library.io.ConsolePrinter;
import pl.sokolik.library.io.DataReader;
import pl.sokolik.library.io.file.FileManager;
import pl.sokolik.library.io.file.FileManagerBuilder;
import pl.sokolik.library.model.Book;
import pl.sokolik.library.model.Library;
import pl.sokolik.library.model.Magazine;
import pl.sokolik.library.model.Publication;
import pl.sokolik.library.model.comparators.AlphabeticalComparator;

import java.util.Arrays;
import java.util.InputMismatchException;

public class LibraryControl {

    private Library library;
    private final ConsolePrinter printer = new ConsolePrinter();
    private final DataReader dataReader = new DataReader(printer);
    private final FileManager fileManager;

    public LibraryControl() {
        //tworzenie obiektów w konstruktorze
        fileManager = new FileManagerBuilder(printer, dataReader).build();

        //jeżeli nie uda się wczytać danych z pliku
        try {
            library = fileManager.importData();
            printer.printLine("Poprawnie zaimportowano plik z danymi");
        } catch (DataImportException | InvalidDataException e) {
            printer.printLine(e.getMessage());
            printer.printLine("Stworzono nowy plik z publikacjami");
            library = new Library();
        }
    }

    public void controlLoop(){
        Option option; // opcja pobrana od usera

        do {
            printOptions();
            option = getOption(); //do obsługi wyjątków
            switch(option){
                case EXIT -> exit();
                case ADD_BOOK -> addBook();
                case ADD_MAGAZINE -> addMagazines();
                case PRINT_BOOKS -> printBooks();
                case PRINT_MAGAZINES -> printMagazines();
                case DELETE_BOOK -> deleteBook();
                case DELETE_MAGAZINE -> deleteMagazine();
                default -> printer.printLine("Wybrałeś błędną opcję");
            }
        } while (option != Option.EXIT);
    }


    private Option getOption() {
        boolean optionOk = false;
        Option option = null; //musi być deklaracja dla intelij

        //pętla dopóki user nie poda poprawnej opcji
        while(!optionOk){
            try {
                option = Option.createFromInt(dataReader.getInt()); //przekształcenie na enuma liczby, którą podał user
                optionOk = true; //koniec działania pętli
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage()); //komunikat z obiektu wyjątku
            } catch (InputMismatchException ex) {
                printer.printLine("Wprowadzono wartość, która nie jest liczbą. Spróbuj jeszcze raz !");
            }
        }
        return option;
    }

    private void printMagazines() {
        Publication[] publication = getSortedPubication();
        printer.printMagazines(publication);
    }

    private void addMagazines() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine); // wywołanie addMagazine z klasy Library
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć magazynu. Błędne dane !");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Osiągnięto limit magazynów. Nie można dodać kolejnych");
        }
    }

    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine(); //magazyn, który chcemy usunąć
            library.removePublication(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się usunąć magazynu, niepoprawne dane !");
        }
    }

    private void exit() {
        try {
            fileManager.exportData(library); //przy zakończeniu programu automatycznie wykona się zapis danych do pliku
            printer.printLine("Zapisano wprowadzone dane");
        } catch (DataExportException e) {
            printer.printLine(e.getMessage());
        }

        printer.printLine("Koniec programu :) \n Do zobaczenia !");
        dataReader.close();
    }

    private void printBooks() {
        Publication[] publication = getSortedPubication();
        printer.printBooks(publication);
    }

    private Publication[] getSortedPubication() {
        Publication[] publication = library.getPublication();
        Arrays.sort(publication, new AlphabeticalComparator());
        return publication;
    }

    private void addBook() {
        //jeżeli ktoś poda błędne dane to blok try nie powinien się wykonać tylko od razu catch
        try {
            Book book = dataReader.readAndCreateBook();
            library.addPublication(book); // wywołanie addBook z klasy Book
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć książki. Błędne dane !");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Osiągnięto limit książki. Nie można dodać kolejnej");
        }
    }

    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();//książka którą chcemy usunąć
            library.removePublication(book);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się usunąć magazynu, niepoprawne dane !");
        }
    }

    private void printOptions() {
        printer.printLine("Wybierz opcję");
        for (Option value : Option.values()) {
            System.out.println(value);
        }
    }

    private enum Option {
        EXIT(0, "- Wyjście z programu"),
        ADD_BOOK(1, "- Dodanie nowej książki"),
        ADD_MAGAZINE(2, "- Dodanie nowego magazynu"),
        PRINT_BOOKS(3, "- Wyświetl dostępne książki"),
        PRINT_MAGAZINES(4, "- Wyświetl dostępne magazyny"),
        DELETE_BOOK(5,"Usuń książkę"),
        DELETE_MAGAZINE(6,"Usuń magazyn");

        private final int value;
        private final String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        //metoda zamieniająca wpisaną opcję przez usera na typ "value" z ENUM
        //values zwraca tablicę z stałymi w enumie tj. EXIT, ADD_BOOK... , a [option] sprawdzi co user wpisał
        //wówczas wartość enuma zostanie poprawnie pobrana i można ją dalej mielić w programie
        //try po to aby user nie mógł wprowadzić liczby z poza zakresu
        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("Brak opcji o ID " + option); //własny wyjątek kontrolowany
            }
        }
    }
}
