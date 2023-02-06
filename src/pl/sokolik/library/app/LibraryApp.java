package pl.sokolik.library.app;

public class LibraryApp {

    private static final String APP_NAME = "Biblioteka version 0.9";
    public static void main(String[] args) {
        System.out.println(APP_NAME);
        LibraryControl libraryControl = new LibraryControl();
        libraryControl.controlLoop();
    }
}
