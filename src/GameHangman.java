import java.io.*;
import java.util.*;

public class GameHangman {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static final int RUSSIAN_ALPHABET_LENGTH = 33;
    public static final String MESSAGE_WRONG = "Нет такой буквы в этом слове. Осталось ошибок: ";
    private static final String MESSAGE_START_OR_RESET_GAME = "Нажми на кнопку \"Enter\" для начала игры или " +
            "\"Пробел + Enter\" если нет желания играть";
    private static final String PATH = "SecretWords.txt";
    private static final String MASK_SYMBOL = "*";
    private static final int MAX_MISTAKES = 6;
    private static int countOfMistakes;
    private static final List<String> LIBRARY = new ArrayList<>();
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
            HangmanState[] hangman = HangmanState.values();
            countOfMistakes = MAX_MISTAKES;
            printMessageForUser(countOfMistakes);
            initLibrary();
            if (LIBRARY.isEmpty()) {
                printMessageForUser("Мне неоткуда брать слова для загадывания. Чтобы начать играть ты должен мне помочь. " +
                        "Помести словарь со словами для загадывания \"SecretWords.txt\" в корневую папку проекта и запусти игру заново\n");
                return;
            }

            List<String> usedSymbols = new ArrayList<>(RUSSIAN_ALPHABET_LENGTH);
            String secretWord = chooseRandomSecretWord();
            mask = maskingSecretWord(secretWord);
            gameLoop(hangman, usedSymbols, secretWord);
            printFinalMessage(secretWord);
        }
        printMessageForUser("Запусти меня заново как появится желание поиграть, я буду ждать!\n");
    }

    private static void printMessageForUser(String message) {
        System.out.printf(message);
    }

    private static void printMessageForUser(int countOfMistakes) {
        System.out.printf("Я загадаю существительное в именительном падеже, а ты попробуешь его угадать, " +
                "у тебя на это будет %s попыток.\n", countOfMistakes);
    }

    private static void printMessageForUser(String message, String messageStartOrReset) {
        System.out.printf(message, messageStartOrReset);
    }

    private static void printMessageForUser(int length, String mask, List<String> usedSymbolsSet) {
        System.out.printf("Отгадай слово из %d букв: %s\nТы уже использовал буквы: %s\nВведи 1 (одну) из 33 (тридцати трёх) " +
                "букв русского языка, которая содержится в загаданном слове: \n", length, mask, usedSymbolsSet);
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

    private static String maskingSecretWord(String secretWord) {
        return MASK_SYMBOL.repeat(secretWord.length());
    }

    private static void gameLoop(HangmanState[] hangman, List<String> usedSymbols, String secretWord) {
        while (checkGameOver()) {
            String newSymbol = playerEnterSymbol(usedSymbols, secretWord);
            if (!validationNewSymbol(newSymbol)) {
                continue;
            }
            if (checkNewSymbolAlreadyUsed(usedSymbols, newSymbol)) {
                continue;
            }
            usedSymbols.add(newSymbol);
            printMessageSuccess(hangman, newSymbol, secretWord);
        }
    }

    private static String playerEnterSymbol(List<String> usedSymbols, String secretWord) {
        printMessageForUser(secretWord.length(), mask, usedSymbols);
        return SCANNER.nextLine().toUpperCase();
    }

    private static boolean validationNewSymbol(String newSymbol) {
        if (!newSymbol.matches("[А-ЯЁ]{1}")) {
            printMessageForUser("Это не буква русского языка. Попробуй ввести заново, " +
                    "у тебя получится, я верю в тебя!\n");
            return false;
        }
        return true;
    }

    private static boolean checkNewSymbolAlreadyUsed(List<String> usedSymbols, String newSymbol) {
        if (usedSymbols.contains(newSymbol)) {
            printMessageForUser("Ты уже вводил такую букву, введи другую\n");
            return true;
        }
        return false;
    }

    private static void openNewSymbolInMask(String newSymbol, String secretWord) {
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

    private static void printMessageSuccess(HangmanState[] hangman, String newSymbol, String secretWord) {
        if (secretWord.contains(newSymbol)) {
            printMessageForUser("Есть такая буква в этом слове!\n\n");
            openNewSymbolInMask(newSymbol, secretWord);
        } else {
            countOfMistakes--;
            printMessageForUser(MESSAGE_WRONG + countOfMistakes + hangman[countOfMistakes].toString());
        }
    }

    private static boolean checkGameOver() {
        return (mask.contains(MASK_SYMBOL) && countOfMistakes > 0);
    }

    private static void printFinalMessage(String secretWord) {
        if (!mask.contains(MASK_SYMBOL)) {
            printMessageForUser("Ты выиграл, молодец! правильное слово %s \n (#^_^#)\n", secretWord);
        } else {
            printMessageForUser("Ты проиграл, было загадано слово %s \n", secretWord);
        }
    }
}
