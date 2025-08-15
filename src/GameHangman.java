public class GameHangman {

    public static void main(String[] args) {
        try {
            Game game = new Game();
            game.run();
        } catch (Exception e) {
            Messages.printException(e);
        }
    }

}
