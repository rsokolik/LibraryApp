package pl.sokolik.library.io.file;

import pl.sokolik.library.Exception.NoSuchFileTypeException;
import pl.sokolik.library.io.ConsolePrinter;
import pl.sokolik.library.io.DataReader;

public class FileManagerBuilder {

    private ConsolePrinter printer;
    private DataReader reader;

    public FileManagerBuilder(ConsolePrinter printer, DataReader reader) {
        this.printer = printer;
        this.reader = reader;
    }

    public FileManager build() {
        printer.printLine("Wybierz format danych: ");
        FileType fileType = getFileType(); //typ ENUM

        switch (fileType){
            case SERIAL -> {
                return new SerializableFileManager();
            }
            default -> throw new NoSuchFileTypeException("Nieobsługiwany typ danych");
        }
    }

    private FileType getFileType() {
        boolean typeOK = false;
        FileType result = null;

        do {
            printTypes();
            String type = reader.getString().toUpperCase();
            try {
                result = FileType.valueOf(type);
                typeOK = true;
            } catch (IllegalArgumentException e) {
                printer.printLine("Nieobslługiwany typ danych, spróbuj ponownie !");
            }
        } while (!typeOK);

        return result;
    }

    private void printTypes() {
        for (FileType value : FileType.values()) {
            printer.printLine(value.name());
        }
    }
}
