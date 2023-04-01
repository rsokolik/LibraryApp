package pl.sokolik.library.model;

import pl.sokolik.library.Exception.PublicationAlreadyExistsException;
import pl.sokolik.library.Exception.UserAlreadyExistsException;

import java.io.Serializable;
import java.util.*;

public class Library implements Serializable {
    private Map<String, Publication> publications = new HashMap<>(); //przechowywane jakieś publikacje
    private Map<String, LibraryUser> users = new HashMap<>(); //<PESEL_USERA, OBIEKT_REPREZENTUJĄCY_CZYTELNIKA>

    public Map<String, Publication> getPublications() {
        return publications;
    }

    public Collection<Publication> getSortedPublications(Comparator<Publication> comparator){
        List<Publication> list = new ArrayList<>(publications.values());//przekazanie do konstruktora listy
        list.sort(comparator);
        return list;
    }

    public Optional<Publication> findPublicationByTitle(String title){
        return Optional.ofNullable(publications.get(title));
    }
    public Map<String, LibraryUser> getUsers() {
        return users;
    }

    public Collection<LibraryUser> getSortedUsers(Comparator<LibraryUser> comparator){
        List<LibraryUser> list = new ArrayList<>(users.values());//przekazanie do konstruktora listy
        list.sort(comparator);
        return list;
    }

    public void addPublication(Publication publication) {
        if (publications.containsKey(publication.getTitle())){ //szukanie duplikatu po tytule
            throw new PublicationAlreadyExistsException("Taka książka już istnieje ! " + publication.getTitle());
        }

        publications.put(publication.getTitle(), publication); //wartością jest cały obiekt publication
    }

    public void addUser(LibraryUser user) {
        if (users.containsKey(user.getPesel())){
            throw new UserAlreadyExistsException("Taki user już istnieje " + user.getPesel());
        }

        users.put(user.getPesel(), user);
    }

    public boolean removePublication (Publication pub){
        if (publications.containsValue(pub)) {
            publications.remove(pub.getTitle());
            return true;
        } else {
            return false;
        }
    }
/*
        <<<<<<<<<<<<<<<<<<WCZEŚNIEJSZE ROZWIĄZANIE PRZY POMOCY TABLIC>>>>>>>>>>>>>>>>


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

    public boolean removePublication (Publication pub){
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

        return found != notFound;
    }
 */
}
