package pl.sokolik.library.model;

import java.io.Serializable;
import java.util.Arrays;

public class Library implements Serializable {
    private static final int INITIAL_CAPACITY = 1; //początkowy rozmiar
    private int publicationsNumber;
    private Publication[] publication = new Publication[INITIAL_CAPACITY];

    //getter zwraca kopie tablicy z TYLKO istniejącymi obiektami, bez NULL
    public Publication[] getPublication() {
        Publication[] result = new Publication[publicationsNumber]; //tworzenie kopii tablicy publication[]
        for (int i = 0; i < result.length; i++) {
            publication[i] = result[i];
        }
        return result;
    }

    public void addPublication(Publication publications){
        //rzucamy wyjątek, a w addBook() w LibraryControl go przechwycamy
        if (publicationsNumber == publication.length){ //jeżeli prawda to w tablicy nie ma już miejsca
            //towrzymy tablicę o większym rozmiarze i kopiujemy wartości z oryginalnej
            publication = Arrays.copyOf(publication, publication.length * 2);
        }
        //bez ELSE bo jeżeli if się nie wykona to od razu przejdzie niżej
        publication[publicationsNumber] = publications;
        publicationsNumber++;
    }

    public void removePublication (Publication pub){
        final int notFound = -1;
        int foundIndex = notFound;
        int i = 0; //zmienna pomocnicza do pętli

        while (i < publicationsNumber && foundIndex == notFound){
            if (pub.equals(publication[i])) {
                foundIndex = i; //znaleziony i'ty element
            } else {
                i++;
            }
        }

        if (foundIndex != notFound) {
            System.arraycopy(publication, foundIndex + 1,
                    publication, foundIndex, publication.length - foundIndex - 1);
            publicationsNumber--;
            publication[publicationsNumber] = null; //wstawia null na koniec tablicy
        }
    }
}
