package pl.sokolik.library.io;

import pl.sokolik.library.model.Book;
import pl.sokolik.library.model.Magazine;
import pl.sokolik.library.model.Publication;

public class ConsolePrinter {

    //w Library mamy getPublication, który robi kopię tablicy z samymmi obiektami, bez nulli
    //stąd nie trzeba się odwoływać do i'tego elementu w tablicy
    public void printBooks(Publication[] publication){
        int countBooks = 0; //licznik książek

        for (Publication publications : publication) {
            if (publications instanceof Book) { // jeżeli jest obiekt typu Book to: (czyli jeżeli znajdzie książke)
                System.out.println(publications);
                countBooks++; // zwiększa licznik
            }
        }

        if (countBooks == 0) {
            printLine("Brak książek w bibliotece");
        }
    }

    public void printMagazines(Publication[] publication){
        int countMagazines = 0;

        for (Publication publications : publication) {
            if (publications instanceof Magazine){
                System.out.println(publications);
                countMagazines++;
            }
        }

        if (countMagazines == 0) {
            printLine("Brak magazynów w bibliotece");
        }
    }

    //metoda po to aby w razie potrzeby na wszystkich println w aplikacji zrobić poprawkę tylko tu
    public void printLine(String text){
        System.out.println(text);
    }
}
