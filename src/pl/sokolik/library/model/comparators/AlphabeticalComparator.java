package pl.sokolik.library.model.comparators;

import pl.sokolik.library.model.Publication;

import java.util.Comparator;

public class AlphabeticalComparator implements Comparator<Publication> {
    @Override
    public int compare(Publication p1, Publication p2) {
        if (p1 == null && p2 == null)
            return 0;
        else if (p1 == null)
            return 1; //wypychamy nulle z lewej na prawą stronę (na koniec tablicy)
        else if (p2 == null)
            return -1; //odwrotnie

        return p1.getTitle().compareToIgnoreCase(p2.getTitle()); //jeżeli nie są nullami to porowónujemy tytuły
    }
}
