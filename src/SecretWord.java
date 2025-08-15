public class SecretWord {
    private final String MASK_SYMBOL = "*";
    private final String secretWord;
    private String mask;

    protected SecretWord(Library library) {
            this.secretWord = library.chooseRandomWord();
            this.mask = MASK_SYMBOL.repeat(secretWord.length());
    }

    public String getSecretWord() {
        return secretWord;
    }

    public String getMask() {
        return mask;
    }

    public String getMASK_SYMBOL() {
        return MASK_SYMBOL;
    }

    void openNewSymbolInMask(String newSymbol) {
        char[] maskChars = mask.toCharArray();
        char symbol = newSymbol.charAt(0);
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == symbol) {
                maskChars[i] = symbol;
            }
        }
        mask = new String(maskChars);
    }
}