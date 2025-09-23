import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private static final HangmanState[] hangman = HangmanState.values();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Messages message = new Messages();
    private static int countOfMistakes;
    private SecretWord secretWord;

    private void setCountOfMistakes() {
        countOfMistakes = 6;
    }

    void run() {
        while (startOrResetGame()) {
            setCountOfMistakes();
            try {
                secretWord = new SecretWord(new Library());
            } catch (IllegalArgumentException e) {
                break;
            }

            message.printStartMessage(countOfMistakes);
            int RUSSIAN_ALPHABET_LENGTH = 33;
            List<String> usedSymbols = new ArrayList<>(RUSSIAN_ALPHABET_LENGTH);
            gameLoop(usedSymbols);
            message.printFinalMessage(secretWord);
        }
        message.runMeAnotherTime();
    }

    private boolean startOrResetGame() {
        message.wantYouPlay();
        boolean isExit = true;
        while (isExit) {
            String answer = SCANNER.nextLine();
            if ((answer).equalsIgnoreCase("")) {
                isExit = false;
            } else if ((answer).equalsIgnoreCase(" ")) {
                break;
            } else {
                message.incorrectEnter();
            }
        }
        return !isExit;
    }

    private void gameLoop(List<String> usedSymbols) {
        while (checkGameOver(secretWord)) {
            String newSymbol = playerEnterSymbol(usedSymbols);
            if (!validationNewSymbol(newSymbol)) {
                continue;
            }

            if (checkNewSymbolAlreadyUsed(usedSymbols, newSymbol)) {
                continue;
            }

            usedSymbols.add(newSymbol);

            if (secretWord.getSecretWord().contains(newSymbol)) {
                message.youAreRight();
                secretWord.openNewSymbolInMask(newSymbol);
            } else {
                message.mistake(hangman, --countOfMistakes);
            }
        }
    }

    private String playerEnterSymbol(List<String> usedSymbols) {
        message.guessWord(secretWord, usedSymbols);
        return SCANNER.nextLine().toUpperCase();
    }

    private boolean validationNewSymbol(String newSymbol) {
        if (!newSymbol.matches("[А-ЯЁ]{1}")) {
            message.incorrectSymbol();
            return false;
        }
        return true;
    }

    private boolean checkNewSymbolAlreadyUsed(List<String> usedSymbols, String newSymbol) {
        if (usedSymbols.contains(newSymbol)) {
            message.itIsAlreadyUsedSymbol();
            return true;
        }
        return false;
    }

    private boolean checkGameOver(SecretWord secretWord) {
        return (!secretWord.allLettersOpened() && countOfMistakes > 0);
    }
}
