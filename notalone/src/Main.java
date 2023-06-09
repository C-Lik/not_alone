import MainGame.Game;

/*!
    \brief Main class

    Contains an instance of the game and calls the \ref MainGame.Game.startGame() function.
 */
public class Main {
    public static void main(String[] args) {
        Game game = Game.getInstance();
        game.startGame();
    }
}
