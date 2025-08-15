import java.util.List;

public class Messages {
    final String MESSAGE_START_OR_RESET_GAME = "Нажми на кнопку \"Enter\" для начала игры или " +
            "\"Пробел + Enter\" если нет желания играть";

    protected Messages() {
    }

    protected static void printException(Exception e) {
        System.out.printf("Ой-ой-ой, срочно свяжись с разработчиком и сообщи ему что программа выдала ошибку: " + e);
    }

    protected static void exceptionInitLibrary() {
        System.out.print("Инициализация словаря закончилась ошибкой, файл не найден!\n");
    }

    protected static void libraryIsEmpty() {
        System.out.print("Мне неоткуда брать слова для загадывания. Чтобы начать играть ты должен мне помочь. " +
                "Помести словарь со словами для загадывания \"SecretWords.txt\" в корневую папку проекта и запусти игру заново\n");
    }

    protected void printFinalMessage(SecretWord secretWord) {
        if (!secretWord.getMask().contains(secretWord.getMASK_SYMBOL())) {
            printYouWin(secretWord);
        } else {
            printYouLoss(secretWord);
        }
    }

    protected void wantYouPlay() {
        System.out.printf("Поиграем в виселицу? %s\n", MESSAGE_START_OR_RESET_GAME);
    }

    protected void incorrectEnter() {
        System.out.printf("Промахнулся! %s\n", MESSAGE_START_OR_RESET_GAME);
    }

    protected void runMeAnotherTime() {
        System.out.print("Запусти меня заново как появится желание поиграть, я буду ждать!\n");
    }

    void printStartMessage(int countOfMistakes) {
        System.out.printf("Я загадаю существительное в именительном падеже, а ты попробуешь его угадать, " +
                "у тебя на это будет %s попыток.\n", countOfMistakes);
    }

    void guessWord(SecretWord secretWord, List<String> usedSymbols) {
        System.out.printf("Отгадай слово из %d букв: %s\nТы уже использовал буквы: %s\nВведи 1 (одну) из 33 (тридцати трёх) " +
                "букв русского языка, которая содержится в загаданном слове: \n", secretWord.getSecretWord().length(), secretWord.getMask(), usedSymbols);
    }

    void incorrectSymbol() {
        System.out.print("Это не буква русского языка. Попробуй ввести заново, у тебя получится, я верю в тебя!\n");
    }

    void itIsAlreadyUsedSymbol() {
        System.out.print("Ты уже вводил такую букву, введи другую\n\n");
    }

    void mistake(HangmanState[] hangman, int countOfMistakes) {
        System.out.print("Нет такой буквы в этом слове. Осталось ошибок: " + countOfMistakes + hangman[countOfMistakes].toString());
    }

    void youAreRight() {
        System.out.print("Есть такая буква в этом слове!\n");
    }

    void printYouWin(SecretWord secretWord) {
        System.out.printf("Ты выиграл, молодец! правильное слово %s \n (#^_^#)\n", secretWord.getSecretWord());
    }
    void printYouLoss(SecretWord secretWord) {
        System.out.printf("Ты проиграл, было загадано слово %s \n", secretWord.getSecretWord());
    }


}
