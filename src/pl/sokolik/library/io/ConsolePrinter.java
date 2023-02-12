package pl.sokolik.library.io;

import pl.sokolik.library.model.Book;
import pl.sokolik.library.model.LibraryUser;
import pl.sokolik.library.model.Magazine;
import pl.sokolik.library.model.Publication;

import java.util.Collection;

public class ConsolePrinter {

    //w Library mamy getPublication, który robi kopię tablicy z samymmi obiektami, bez nulli
    //stąd nie trzeba się odwoływać do i'tego elementu w tablicy
    public void printBooks(Collection<Publication> publication){ //tab: Publication[] publication
        int countBooks = 0; //licznik książek

        for (Publication publications : publication) {
            if (publications instanceof Book) { // jeżeli jest obiekt typu Book to: (czyli jeżeli znajdzie książke)
                printLine(publications.toString());
                countBooks++; // zwiększa licznik
            }
        }

        if (countBooks == 0) {
            printLine("Brak książek w bibliotece");
        }
    }

    public void printMagazines(Collection<Publication> publication){ //tab: Publication[] publication
        int countMagazines = 0;

        for (Publication publications : publication) {
            if (publications instanceof Magazine){
                printLine(publications.toString());
                countMagazines++;
            }
        }

        if (countMagazines == 0) {
            printLine("Brak magazynów w bibliotece");
        }
    }

    public void printUsers(Collection<LibraryUser> user) {
        for (LibraryUser libraryUser : user) {
            printLine(user.toString());
        }
    }

    //metoda po to aby w razie potrzeby na wszystkich println w aplikacji zrobić poprawkę tylko tu
    public void printLine(String text){
        System.out.println(text);
    }
}
