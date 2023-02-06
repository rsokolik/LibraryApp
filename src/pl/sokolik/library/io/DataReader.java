package pl.sokolik.library.io;

import pl.sokolik.library.model.Book;
import pl.sokolik.library.model.Magazine;

import java.util.Scanner;

public class DataReader {
    private Scanner scanner = new Scanner(System.in);
    private ConsolePrinter printer;

    //wstrzyknięcie zależności między printerem a data readerem
    //jeżeli chcemy korzystać z DataReader to musimy mieć również obiekt ConsolePrinter
    public DataReader(ConsolePrinter printer) {
        this.printer = printer;
    }

    public Book readAndCreateBook(){
        printer.printLine("Tytuł:");
        String title = scanner.nextLine();
        printer.printLine("Autor");
        String author = scanner.nextLine();
        printer.printLine("Wydawnictwo");
        String publisher = scanner.nextLine();
        printer.printLine("ISBN");
        String isbn = scanner.nextLine();
        printer.printLine("Rok wydania");
        int releaseDate = getInt();
        printer.printLine("Ilość stron");
        int pages = getInt();

        return new Book(title, author, releaseDate, pages, publisher, isbn);
    }

    public Magazine readAndCreateMagazine(){
        printer.printLine("Tytuł:");
        String title = scanner.nextLine();
        printer.printLine("Wydawnictwo");
        String publisher = scanner.nextLine();
        printer.printLine("Język");
        String language = scanner.nextLine();
        printer.printLine("Rok wydania");
        int year = getInt();
        printer.printLine("Miesiąc");
        int month = getInt();
        printer.printLine("Dzień");
        int day = getInt();

        return new Magazine(title, publisher, language, year, month, day);
    }

    public void close(){
        scanner.close();
    }

    public int getInt(){
        //jeżeli poda liczbę poprawnie to ta liczba zostanie zwrócona, a w finally pozbędziemy się entera
        //który zostaje po wczytaniu liczby, jeżeli poda coś innego to zostanie rzucony wyjątek
        //InputMissmatchException ale finally i tak się wykona, a wyjątek można przechwycić w miejscu
        //gdzie ta metoda została wyowałana
        try {
            return scanner.nextInt();
        } finally {
            scanner.nextLine();
        }
    }
}
