package GameState;

import java.awt.*;

import Audio.Sound;
import Graphics.Assets;
import Input.KeyHandler;
import MainGame.Game;

/*!
    \brief Game over state.

    \note This class extends \ref GameState and represents a component of the State design pattern implementation.
    \note Implements the Singleton design pattern.
 */
public class GameOverState extends GameState {

    private static GameOverState instance = null;
    private final static Sound gameOver = new Sound("gameOver.wav");
    private final Rectangle back;

    /*!
       \brief Private constructor.

       Sets the dimensions, position, and specific image of the button in the game over state.
    */
    private GameOverState() {
        back = new Rectangle(20, 20, 57, 57);
        image = Assets.getInstance().lost;
    }

    /*!
        \brief Update.

        This method checks if the "Back" button or the ESC key has been pressed, and if so, it
        transitions to the Menu state (\ref MenuState).
     */
    @Override
    public void update() {
        if (buttonWasClicked(back) || KeyHandler.exitPressed) {
            Game.getInstance().state = MenuState.getInstance();
        }
    }

    //! \brief Get the class instance and play the corresponding sound.
    public static GameOverState getInstance() {
        if (instance == null) {
            instance = new GameOverState();
        }
        gameOver.play();
        return instance;
    }
}
