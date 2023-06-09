package MainGame;

import Audio.SoundLibrary;
import GameState.GameState;
import GameState.MenuState;
import GameState.MenuOption;
import Input.DBHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;

/*!
    \brief The game engine.
    \note Implements the Singleton design pattern.
 */
public class Game implements Runnable {
    private static Game instance = null;

    private final GameWindow window;
    private boolean runState;
    private Thread gameThread;
    private BufferStrategy bs;
    public Graphics g;

    public static final int tileSize = 32;
    public static final int wndRows = 18;
    public static final int wndCols = 36;
    public static final int WIDTH = wndCols * tileSize;
    public static final int HEIGHT = wndRows * tileSize;

    public final static int MAPS_NUMBER = 3;
    public static int currentMapNumber;
    public static boolean SoundOn = true;

    public GameState state;
    public static MenuOption option;

    //! \brief Private constructor that initializes a new window and sets the menu state.
    private Game() {
        window = new GameWindow("Not Alone", WIDTH, HEIGHT);
        state = MenuState.getInstance();
        runState = false;
    }

    //! Gets the class instance.
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    //! \brief Calls the functions \ref update() and \ref draw().
    public void run() {
        long oldTime = System.nanoTime();
        long currentTime;

        final int framesPerSecond = 60;
        final double timeFrame = 1_000_000_000.0 / framesPerSecond;

        while (runState) {
            currentTime = System.nanoTime();
            if ((currentTime - oldTime) > timeFrame) {
                update();
                draw();
                oldTime = currentTime;
            }
        }
    }

    //! \brief Creates and starts a new thread of execution.
    public synchronized void startGame() {
        if (!runState) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start(); // it will call run function
        }
    }

    //! \brief Closes the game, used sounds, and the database (if it was opened).
    public synchronized void stopGame() {
        if (runState) {
            runState = false;

            SoundLibrary.closeSounds();

            if (DBHandler.checkIfInstanceIsNull())
                DBHandler.getInstance().close();

            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    //! \brief Calls the update function for the current state.
    private void update() {
        state.update();
    }

    //! \brief Calls the draw function for the current state, which renders the elements.
    private void draw() {
        bs = window.getCanvas().getBufferStrategy();
        if (bs == null) {
            try {
                window.getCanvas().createBufferStrategy(3);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        g = bs.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        window.getFrame().requestFocusInWindow();
        state.draw(g);

        bs.show();
        g.dispose();
    }
}
