import java.io.*;
import java.util.*;

public class GameHangman {
    public static final String MESSAGE_WRONG = "Нет такой буквы в этом слове. Осталось ошибок: ";
    private static final List<String> LIBRARY = new ArrayList<>();
    private static final HangmanState[] HANGMAN = HangmanState.values();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static final String MESSAGE_START_OR_RESET_GAME = "Нажми на кнопку \"Enter\" для начала игры или " +
            "\"Пробел + Enter\" если нет желания играть";
    private static final String PATH = "SecretWords.txt";
    private static final String MASK_SYMBOL = "*";
    private static final int RUSSIAN_ALPHABET_LENGTH = 33;
    private static List<String> usedSymbols;
    private static int countOfMistakes;
    private static String secretWord;
    private static String mask;

    public static void main(String[] args) {
        try {
            startGame();
        } catch (Exception e) {
            printMessageForUser("Ой-ой-ой, срочно свяжись с разработчиком и сообщи ему что программа выдала ошибку: " + e);
        }
    }

    private static void startGame() {
        while (startOrResetGame()) {
            countOfMistakes = 6;
            printMessageForUser("Я загадаю существительное в именительном падеже, а ты попробуешь его угадать, " +
                    "у тебя на это будет %s попыток.\n", countOfMistakes);
            initLibrary();
            if (LIBRARY.isEmpty()) {
                printMessageForUser("Мне неоткуда брать слова для загадывания. Чтобы начать играть ты должен мне помочь. " +
                        "Помести словарь со словами для загадывания \"SecretWords.txt\" в корневую папку проекта и запусти игру заново\n");
                return;
            }
            usedSymbols = new ArrayList<>(RUSSIAN_ALPHABET_LENGTH);
            secretWord = chooseRandomSecretWord();
            mask = maskingSecretWord();
            gameLoop();
        }
        printMessageForUser("Запусти меня заново как появится желание сыграть, я буду ждать!\n");
    }

    private static void printMessageForUser(String message) {
        System.out.printf(message);
    }

    private static void printMessageForUser(String message, int countOfMistakes) {
        System.out.printf(message, countOfMistakes);
    }

    private static void printMessageForUser(String message, String messageStartOrReset) {
        System.out.printf(message, messageStartOrReset);
    }

    private static void printMessageForUser(String message, int length, String mask, List<String> usedSymbolsSet) {
        System.out.printf(message, length, mask, usedSymbolsSet);
    }

    private static boolean startOrResetGame() {
        printMessageForUser("Поиграем в виселицу? %s\n", MESSAGE_START_OR_RESET_GAME);
        boolean isExit = true;
        while (isExit) {
            String answer = SCANNER.nextLine();
            if ((answer).equalsIgnoreCase("")) {
                isExit = false;
            } else if ((answer).equalsIgnoreCase(" ")) {
                break;
            } else {
                printMessageForUser("Промахнулся! %s\n", MESSAGE_START_OR_RESET_GAME);
            }
        }
        return !isExit;
    }

    private static void initLibrary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GameHangman.PATH))) {
            while (reader.ready()) {
                LIBRARY.add(reader.readLine());
            }
        } catch (Exception e) {
            printMessageForUser("Упс, инициализация словаря закончилась ошибкой!\n");
        }
    }

    private static String chooseRandomSecretWord() {
        int randomInt = RANDOM.nextInt(LIBRARY.size());
        String word = LIBRARY.get(randomInt);
        return word.toUpperCase();
    }

    private static String maskingSecretWord() {
        return MASK_SYMBOL.repeat(secretWord.length());
    }

    private static void gameLoop() {
        while (checkGameOver()) {
            String newSymbol = playerEnterSymbol();
            if (!newSymbol.matches("[А-ЯЁ]{1}")) {
                printMessageForUser("Это не буква русского языка. Попробуй ввести заново, " +
                        "у тебя получится, я верю в тебя!\n");
                continue;
            }
            if (usedSymbols.contains(newSymbol)) {
                printMessageForUser("Ты уже вводил такую букву, введи другую\n");
                continue;
            }
            usedSymbols.add(newSymbol);
            printMessageSuccess(newSymbol);
        }

        printFinalMessage();
    }

    private static String playerEnterSymbol() {
        printMessageForUser("Отгадай слово из %d букв: %s\nТы уже использовал буквы: %s\nВведи 1 (одну) из " +
                "33 (тридцати трёх) букв русского языка, которая содержится в загаданном слове: \n", secretWord.length(), mask, usedSymbols);
        return SCANNER.nextLine().toUpperCase();
    }

    private static void openNewSymbolInMask(String newSymbol) {
        char[] secretWordCharArray = secretWord.toCharArray();
        char[] maskCharArray = mask.toCharArray();
        char symbol = newSymbol.charAt(0);
        for (int i = 0; i < secretWord.length(); i++) {
            if (maskCharArray[i] == MASK_SYMBOL.charAt(0) && secretWordCharArray[i] == symbol) {
                maskCharArray[i] = symbol;
            }
        }
        mask = String.valueOf(maskCharArray);
    }

    private static void printMessageSuccess(String newSymbol) {
        if (secretWord.contains(newSymbol)) {
            printMessageForUser("Есть такая буква в этом слове!\n");
            openNewSymbolInMask(newSymbol);
        } else {
            countOfMistakes--;
            printMessageForUser(MESSAGE_WRONG + countOfMistakes + HANGMAN[countOfMistakes].toString());
        }
    }

    private static boolean checkGameOver() {
        return (mask.contains(MASK_SYMBOL) && countOfMistakes > 0);
    }

    private static void printFinalMessage() {
        if (!mask.contains(MASK_SYMBOL)) {
            printMessageForUser("Ты выиграл, молодец! правильное слово %s \n (#^_^#)\n", secretWord);
        } else {
            printMessageForUser("Ты проиграл, было загадано слово %s \n", secretWord);
        }
    }
}
