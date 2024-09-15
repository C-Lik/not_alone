package MainGame;

import Audio.SoundLibrary;
import Database.DBHandler;
import GameState.GameState;
import GameState.MenuOption;
import GameState.MenuState;

import java.awt.*;
import java.awt.image.BufferStrategy;

/*!
    \brief The game engine.
    \note Implements the Singleton design pattern.
 */
public class Game implements Runnable {
    public static final int tileSize = 32;
    public static final int wndRows = 18;
    public static final int wndCols = 36;
    public static final int WIDTH = wndCols * tileSize;
    public static final int HEIGHT = wndRows * tileSize;
    public final static int MAPS_NUMBER = 3;
    public static int currentMapNumber;
    public static boolean SoundOn = true;
    public static MenuOption option;
    private static Game instance = null;
    private final GameWindow window;
    public Graphics g;
    public GameState state;
    private boolean runState;
    private Thread gameThread;
    private BufferStrategy bs;

    private Game() {
        window = new GameWindow("Not Alone", WIDTH, HEIGHT);
        state = MenuState.getInstance();
        runState = false;
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

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

    public synchronized void startGame() {
        if (!runState) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start(); // it will call run function
        }
    }

    public synchronized void stopGame() {
        if (runState) {
            runState = false;

            SoundLibrary.closeSounds();

            if (DBHandler.isInstanceIsNull())
                DBHandler.getInstance().close();

            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void update() {
        state.update();
    }

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
