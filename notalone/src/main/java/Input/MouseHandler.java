package Input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseHandler extends MouseAdapter {

    private static MouseHandler instance = null;
    public static int mouseX, mouseY;

    private MouseHandler() {}

    public static MouseHandler getInstance() {
        if (instance == null) {
            instance = new MouseHandler();
        }
        return instance;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public static void resetMouse() {
        MouseHandler.mouseX = -1;
        MouseHandler.mouseY = -1;
    }
}
