package pl.sokolik.library.io.file;

import pl.sokolik.library.model.Library;

public interface FileManager {

    Library importData();

    void exportData(Library library);

}
