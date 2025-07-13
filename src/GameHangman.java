import java.io.*;
import java.util.*;

public class GameHangman {
    private static final List<String> LIBRARY = new ArrayList<>();
    private static final HangmanState[] hangman = HangmanState.values();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final String MESSAGE_START_OR_RESET_GAME = "Нажми на кнопку \"Enter\" для начала игры или " +
            "\"Пробел + Enter\" если нет желания играть";
    private static final String PATH = "SecretWords.txt";
    private static final String STAR = "*";
    private static final int RUSSIAN_ALPHABET_LENGTH = 33;
    private static Set<String> USED_LETTERS_SET;
    private static int countOfMistakes;
    private String secretWord;
    private String mask;

    private GameHangman() {
    }

    public static void main(String[] args) {
        gameLoop();
    }

    private static void gameLoop() {
        try {
            while (startOrResetGame()) {
                countOfMistakes = 6;
                System.out.printf("Я загадаю существительное в именительном падеже, а ты попробуешь его угадать, " +
                        "у тебя на это будет %s попыток.\n", countOfMistakes);
                if (!initLibrary().isEmpty()) {
                    GameHangman game = new GameHangman();
                    USED_LETTERS_SET = new HashSet<>(RUSSIAN_ALPHABET_LENGTH);
                    game.secretWord = game.chooseRandomSecretWord();
                    game.mask = game.maskSecretWord(game);
                    while (checkAbilityToMove()) {
                        String newLetter = playerEnterLetter(game);
                        if (game.validationEnteredLetter(newLetter)) {
                            if (!game.addNewLetterToUsedLettersSet(game, newLetter)) {
                                if (game.secretWord.contains(newLetter)) {
                                    System.out.println("Есть такая буква в этом слове!");
                                    game.mask = game.openNewLetterInMask(newLetter);
                                    if (game.checkGameOver(game)) {
                                        System.out.printf("Ты выиграл, молодец! правильное слово %s \n (#^_^#)\n", game.secretWord);
                                        break;
                                    }
                                } else {
                                    countOfMistakes--;
                                    System.out.println(hangman[countOfMistakes]);
                                    if (game.checkGameOver(game)) {
                                        System.out.printf("Ты проиграл, было загадано слово %s \n", game.secretWord);
                                    }
                                }
                            } else {
                                System.out.println("Ты уже вводил такую букву, введи другую");
                            }
                        } else {
                            System.out.println("Не, ну ты вообще читаешь какие символы нужно вводить?! Попробуй заново, " +
                                    "у тебя получится, я верю в тебя!");
                        }
                    }
                } else {
                    System.out.println("Мне неоткуда брать слова для загадывания. Чтобы начать играть ты должен мне помочь. " +
                            "Помести словарь со словами для загадывания в папку проекта и запусти игру заново");
                    return;
                }
            }
            System.out.println("Запусти меня заново как появится желание сыграть, я буду ждать!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean startOrResetGame() {
        System.out.printf("Поиграем в виселицу? %s\n", MESSAGE_START_OR_RESET_GAME);
        boolean isExit = true;
        while (isExit) {
            String answer = scanner.nextLine();
            if ((answer).equalsIgnoreCase("")) {
                isExit = false;
            } else if ((answer).equalsIgnoreCase(" ")) {
                break;
            } else {
                System.out.printf("Промахнулся! %s\n", MESSAGE_START_OR_RESET_GAME);
            }
        }
        return !isExit;
    }

    private static List<String> initLibrary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GameHangman.PATH))) {
            while (reader.ready()) {
                LIBRARY.add(reader.readLine());
            }
        } catch (Exception e) {
            System.out.println("Упс!");
        }
        return LIBRARY;
    }

    private String chooseRandomSecretWord() {
        return LIBRARY.get(random.nextInt(LIBRARY.size())).toUpperCase();
    }

    private String maskSecretWord(GameHangman game) {
        return STAR.repeat(game.secretWord.length());
    }

    private static String playerEnterLetter(GameHangman game) {
        System.out.printf("""
                Отгадай слово из %d букв: %s\s
                Ты уже использовал буквы: %s\s
                Введи 1 (одну) из 33 (тридцати трёх) букв русского языка, которая содержится в загаданном слове:\s
                """, game.secretWord.length(), game.mask, USED_LETTERS_SET);
        return scanner.nextLine().toUpperCase();
    }

    private boolean validationEnteredLetter(String newLetter) {
        return newLetter.matches("[А-Я]{1}");
    }

    private boolean addNewLetterToUsedLettersSet(GameHangman game, String newLetter) {
        if (!USED_LETTERS_SET.toString().contains(newLetter)) {
            USED_LETTERS_SET.add(newLetter);
        } else {
            return true;
        }
        return false;
    }

    private static boolean checkAbilityToMove() {
        return countOfMistakes > 0;
    }

    private String openNewLetterInMask(String newLetter) {
        char[] secretWordCharArray = secretWord.toCharArray();
        char[] maskCharArray = mask.toCharArray();
        char symbol = newLetter.charAt(0);
        for (int i = 0; i < secretWord.length(); i++) {
            if (maskCharArray[i] == STAR.charAt(0) && secretWordCharArray[i] == symbol) {
                maskCharArray[i] = symbol;
            }
        }
        return String.valueOf(maskCharArray);
    }

    private boolean checkGameOver(GameHangman game) {
        return !(game.mask.contains(STAR) && checkAbilityToMove());
    }
}
