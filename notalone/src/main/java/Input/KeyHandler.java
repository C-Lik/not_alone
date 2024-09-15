package Input;

import MainGame.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static boolean upPressed, leftPressed, rightPressed, attackPressed, exitPressed, killEnemiesPressed;

    public KeyHandler() {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_C) {
            attackPressed = true;
        }
        if (key == KeyEvent.VK_F9) {
            killEnemiesPressed = true;
        }
        if (key == KeyEvent.VK_M) {
            Game.SoundOn = !Game.SoundOn;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            exitPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_C) {
            attackPressed = false;
        }
        if (key == KeyEvent.VK_F9) {
            killEnemiesPressed = false;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            exitPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
