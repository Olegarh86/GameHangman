import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Library {
    private final List<String> LIBRARY = new ArrayList<>();

    protected Library() {
        final Path path = Paths.get("C:\\Users\\Olegarh\\IdeaProjects\\hangman\\SecretWords.txt");
        initLibrary(path);
        if (LIBRARY.isEmpty()) {
            Messages.libraryIsEmpty(path);
        }
    }

    private void initLibrary(Path PATH) {

        try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(PATH)))) {
            while (reader.ready()) {
                LIBRARY.add(reader.readLine());
            }
        } catch (IOException e) {
            Messages.exceptionInitLibrary(PATH);
        }
    }

    protected String chooseRandomWord() {
        Random random = new Random();
        int randomInt = random.nextInt(LIBRARY.size());
        String word = LIBRARY.get(randomInt);
        return word.toUpperCase();
    }
}
