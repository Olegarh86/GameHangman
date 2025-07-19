import java.io.*;
import java.util.*;

public class GameHangman {
    private static final List<String> LIBRARY = new ArrayList<>();
    private static final HangmanState[] HANGMAN = HangmanState.values();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static final String MESSAGE_START_OR_RESET_GAME = "Нажми на кнопку \"Enter\" для начала игры или " +
            "\"Пробел + Enter\" если нет желания играть";
    private static final String PATH = "SecretWords.txt";
    private static final String STAR = "*";
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
                if (!initLibrary().isEmpty()) {
                    usedSymbols = new ArrayList<>(RUSSIAN_ALPHABET_LENGTH);
                    secretWord = chooseRandomSecretWord();
                    mask = maskingSecretWord();
                    gameLoop();
                } else {
                    printMessageForUser("Мне неоткуда брать слова для загадывания. Чтобы начать играть ты должен мне помочь. " +
                            "Помести словарь со словами для загадывания в папку проекта и запусти игру заново\n");
                    return;
                }
            }
        printMessageForUser("Запусти меня заново как появится желание сыграть, я буду ждать!\n");
    }

    private static void printMessageForUser(String message) {
        System.out.printf(message);
    }

    private static void printMessageForUser(String message, int countOfMistakes) {
        System.out.printf(message, countOfMistakes);
    }

    private static void printMessageForUser(String message, String messageStartOrReset ) {
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

    private static List<String> initLibrary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GameHangman.PATH))) {
            while (reader.ready()) {
                LIBRARY.add(reader.readLine());
            }
        } catch (Exception e) {
            printMessageForUser("Упс, инициализация словаря закончилась ошибкой!\n");
        }
        return LIBRARY;
    }

    private static String chooseRandomSecretWord() {
        return LIBRARY.get(RANDOM.nextInt(LIBRARY.size())).toUpperCase();
    }

    private static String maskingSecretWord() {
        return STAR.repeat(secretWord.length());
    }

    private static void gameLoop() {
        while (checkAbilityToMove()) {
            String newSymbol = playerEnterSymbol();
            if (checkValidationEnteredSymbol(newSymbol)) {
                if (!checkContainsNewSymbolInUsedSymbols(newSymbol)) {
                    addNewSymbolToUsedSymbols(newSymbol);
                    hZ(newSymbol);
                } else {
                    printMessageForUser("Ты уже вводил такую букву, введи другую\n");
                }
            } else {
                printMessageForUser("Это не буква русского языка. Попробуй ввести заново, " +
                        "у тебя получится, я верю в тебя!\n");
            }
        }
    }

    private static boolean checkContainsNewSymbolInSecretWord(String newSymbol) {
        return secretWord.contains(newSymbol);
    }

    private static String playerEnterSymbol() {
        printMessageForUser("Отгадай слово из %d букв: %s\nТы уже использовал буквы: %s\nВведи 1 (одну) из " +
                "33 (тридцати трёх) букв русского языка, которая содержится в загаданном слове: \n", secretWord.length(), mask, usedSymbols);
        return SCANNER.nextLine().toUpperCase();
    }

    private static boolean checkValidationEnteredSymbol(String newSymbol) {
        return newSymbol.matches("[А-ЯЁ]{1}");
    }

    private static boolean checkContainsNewSymbolInUsedSymbols(String newSymbol) {
        return usedSymbols.contains(newSymbol);
    }

    private static void addNewSymbolToUsedSymbols(String newSymbol) {
        usedSymbols.add(newSymbol);
    }

    //TODO correct name
    private static void hZ(String newSymbol) {
        if (checkContainsNewSymbolInSecretWord(newSymbol)) {
            printMessageForUser("Есть такая буква в этом слове!\n");
            mask = openNewSymbolInMask(newSymbol);
            if (checkGameOver()) {
                printMessageForUser("Ты выиграл, молодец! правильное слово %s \n (#^_^#)\n", secretWord);
                countOfMistakes = 0;
            }
        } else {
            countOfMistakes--;
            printMessageForUser(HANGMAN[countOfMistakes].toString());
            if (checkGameOver()) {
                printMessageForUser("Ты проиграл, было загадано слово %s \n", secretWord);
            }
        }
    }

    private static boolean checkAbilityToMove() {
        return countOfMistakes > 0;
    }

    private static String openNewSymbolInMask(String newSymbol) {
        char[] secretWordCharArray = secretWord.toCharArray();
        char[] maskCharArray = mask.toCharArray();
        char symbol = newSymbol.charAt(0);
        for (int i = 0; i < secretWord.length(); i++) {
            if (maskCharArray[i] == STAR.charAt(0) && secretWordCharArray[i] == symbol) {
                maskCharArray[i] = symbol;
            }
        }
        return String.valueOf(maskCharArray);
    }

    private static boolean checkGameOver() {
        return !(mask.contains(STAR) && checkAbilityToMove());
    }
}
