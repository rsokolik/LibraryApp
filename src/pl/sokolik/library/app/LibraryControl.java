package pl.sokolik.library.app;

import pl.sokolik.library.Exception.*;
import pl.sokolik.library.io.ConsolePrinter;
import pl.sokolik.library.io.DataReader;
import pl.sokolik.library.io.file.FileManager;
import pl.sokolik.library.io.file.FileManagerBuilder;
import pl.sokolik.library.model.*;

import java.util.Comparator;
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
                case ADD_BOOK -> addBook();
                case ADD_MAGAZINE -> addMagazines();
                case PRINT_BOOKS -> printBooks();
                case PRINT_MAGAZINES -> printMagazines();
                case DELETE_BOOK -> deleteBook();
                case DELETE_MAGAZINE -> deleteMagazine();
                case ADD_USER -> addUser();
                case PRINT_USER -> printUser();
                case FIND_PUBLICATION -> findBook();
                case EXIT -> exit();
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

    private void findBook() {
        printer.printLine("Podaj tytuł publikacji: ");
        String foundTitle = dataReader.getString();
        String notFoundMessage = "Brak publikacji o takim tytule";
        library.findPublicationByTitle(foundTitle)
                .map(Publication::toString)
                .ifPresentOrElse(System.out::println, () -> System.out.println(notFoundMessage));
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

    private void printMagazines() {
//        Publication[] publication = getSortedPubication(); //podejście tablicowe
//        printer.printMagazines(publication);

        printer.printMagazines(library.getSortedPublications(
                Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine(); //magazyn, który chcemy usunąć
            library.removePublication(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się usunąć magazynu, niepoprawne dane !");
        }
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

    private void printBooks() {
//        Publication[] publication = getSortedPubication(); //podejście tablicowe
//        printer.printBooks(publication);

        printer.printBooks(library.getSortedPublications(
                Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();//książka którą chcemy usunąć
            library.removePublication(book);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się usunąć magazynu, niepoprawne dane !");
        }
    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser(); //wczytanie od usera
        try {
            library.addUser(libraryUser);
        } catch (UserAlreadyExistsException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void printUser() {
        printer.printUsers(library.getSortedUsers(
//                ((p1, p2) -> p1.getPesel().compareToIgnoreCase(p2.getLastName())));
                Comparator.comparing(User::getLastName, String.CASE_INSENSITIVE_ORDER)));
        //referencja do metody, wyciągany jest user LastName i porównywany
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

    // <<<<<<<<<<<podejście tablicowe>>>>>>>>>>>>>>>
//    private Publication[] getSortedPubication() {
//        Publication[] publication = library.getPublication();
//        Arrays.sort(publication, new AlphabeticalComparator());
//        return publication;
//    }

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
        DELETE_MAGAZINE(6,"Usuń magazyn"),
        ADD_USER(7, "Dodaj czytelnika"),
        PRINT_USER(8,"Wyświetl czytelnika"),
        FIND_PUBLICATION(9, "Wyszukaj publikację");

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
