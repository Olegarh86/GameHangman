import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Library {
    private final List<String> LIBRARY = new ArrayList<>();

    protected Library() {
        initLibrary();
        if (LIBRARY.isEmpty()) {
            Messages.libraryIsEmpty();
        }
    }

    private void initLibrary() {
        final String PATH = "SecretWords.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            while (reader.ready()) {
                LIBRARY.add(reader.readLine());
            }
        } catch (IOException e) {
            Messages.exceptionInitLibrary();
        }
    }

    protected String chooseRandomWord() {
        Random random = new Random();
        int randomInt = random.nextInt(LIBRARY.size());
        String word = LIBRARY.get(randomInt);
        return word.toUpperCase();
    }
}
