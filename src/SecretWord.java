public class SecretWord {
    private static int countOfOpenedLetters;
    private static int secretWordLength;
    private final String MASK_SYMBOL = "*";
    private final String secretWord;
    private String mask;

    protected SecretWord(Library library) {
        this.secretWord = library.chooseRandomWord();
        secretWordLength = secretWord.length();
        this.mask = MASK_SYMBOL.repeat(secretWord.length());
            countOfOpenedLetters = 0;
    }

    protected String getSecretWord() {
        return secretWord;
    }

    protected String getMask() {
        return mask;
    }

    protected String getMASK_SYMBOL() {
        return MASK_SYMBOL;
    }

    void openNewSymbolInMask(String newSymbol) {
        char[] maskChars = mask.toCharArray();
        char symbol = newSymbol.charAt(0);
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == symbol) {
                maskChars[i] = symbol;
                countOfOpenedLetters++;
            }
        }
        mask = new String(maskChars);
    }

    protected boolean allLettersOpened() {
        return countOfOpenedLetters == secretWordLength;
    }
}