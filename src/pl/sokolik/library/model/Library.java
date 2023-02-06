package pl.sokolik.library.model;

import java.io.Serializable;

public class Library implements Serializable {
    private static final int MAX_PUBLICATIONS = 2000;
    private int publicationsNumber;
    private Publication[] publication = new Publication[MAX_PUBLICATIONS];

    //getter zwraca kopie tablicy z TYLKO istniejącymi obiektami, bez NULL
    public Publication[] getPublication() {
        Publication[] result = new Publication[publicationsNumber]; //tworzenie kopii tablicy publication[]
        for (int i = 0; i < result.length; i++) {
            publication[i] = result[i];
        }
        return result;
    }

    public void addBook(Book book){
        addPublication(book);
    }

    public void addMagazine(Magazine magazine){
        addPublication(magazine);
    }

    private void addPublication(Publication publications){
        //rzucamy wyjątek, a w addBook() w LibraryControl go przechwycamy
        if (publicationsNumber >= MAX_PUBLICATIONS){
            throw new ArrayIndexOutOfBoundsException("Osiągnięto max limit publikacji" + MAX_PUBLICATIONS);
        }

        //bez ELSE bo jeżeli if się nie wykona to od razu przejdzie niżej
        publication[publicationsNumber] = publications;
        publicationsNumber++;
    }
}
