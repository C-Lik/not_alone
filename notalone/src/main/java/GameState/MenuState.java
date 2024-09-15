package GameState;

import Audio.SoundLibrary;
import Exceptions.CriticalExceptionHandler;
import Database.DBHandler;
import MainGame.Game;
import Graphics.Assets;

import java.awt.*;

/*!
    \brief Menu state.

    Manages the game states according to the pressed buttons.

    \note \note This class extends \ref GameState and represents a component of the State design pattern implementation.
    \note Implements the Singleton design pattern.
 */
public class MenuState extends GameState {

    private static MenuState instance = null;
    private final Rectangle newGame, load, exit, info, music;

    /*!
       \brief Private constructor.

       - load the background image from the "Assets" directory. If an exception occurs, terminate the program with an error.
       - set the dimensions and positions of the buttons.
       - set the value "wasWin" variable based on the data read from the database.
     */
    private MenuState() {
        try {
            Assets.getInstance();
        } catch (Exception e) {
            CriticalExceptionHandler.handle(e);
        }
        image = Assets.getInstance().menu;
        newGame = new Rectangle(490, 253, 170, 57);
        load = new Rectangle(490, 319, 170, 57);
        exit = new Rectangle(490, 387, 170, 57);
        info = new Rectangle(1062, 30, 57, 57);
        music = new Rectangle(1062, 96, 57, 57);
        DBHandler level = DBHandler.getInstance();
        level.loadLevel();
        wasWin = level.won();
    }

    /*!
        \brief Update.

        \li play background music if it is not disabled;
        \li set the game state based on the button pressed;

        If any exceptions occur, write the error message to error.txt and exit the program with an error code (-1).
     */
    @Override
    public void update() {
        if (Game.SoundOn)
            SoundLibrary.menu.loopPlaying();
        else
            SoundLibrary.menu.stop();

        if (buttonWasClicked(newGame)) {
            SoundLibrary.menu.stop();
            Game.option = MenuOption.newGame;
            Game.currentMapNumber = 1;
            try {
                PlayState.getInstance().elements.reset();
            } catch (Exception e) {
                CriticalExceptionHandler.handle(e);
            }
            Game.getInstance().state = PlayState.getInstance();
            wasWin = false;
            // reset the option
            Game.option = MenuOption.none;
        } else if (buttonWasClicked(load)) {
            Game.option = MenuOption.loadGame;
            PlayState state = PlayState.getInstance();
            try {
                state.elements.reset();
            } catch (Exception e) {
                CriticalExceptionHandler.handle(e);
            }
            wasWin = state.elements.items.isEmpty();
            if (!wasWin) {
                SoundLibrary.menu.stop();
                Game.getInstance().state = PlayState.getInstance();
            }
            // reset the option
            Game.option = MenuOption.none;
        } else if (buttonWasClicked(exit)) {
            System.exit(0);
        } else if (buttonWasClicked(info)) {
            Game.getInstance().state = HelpState.getInstance();
        } else if (buttonWasClicked(music)) {
            Game.SoundOn = !Game.SoundOn;
        }
    }

    //! \brief Gets the class instance.
    public static MenuState getInstance() {
        if (instance == null) {
            instance = new MenuState();
        }
        return instance;
    }
}
