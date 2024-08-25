package Input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*!
    \brief Handles mouse input.

    Extends the MouseAdapter class to override the mouseClicked method as needed.

    \note Implements the Singleton design pattern.
 */
public class MouseHandler extends MouseAdapter {

    private static MouseHandler instance = null;
    public static int mouseX, mouseY;

    //! \brief Private constructor
    private MouseHandler() {}

    //! \brief Gets the class instance
    public static MouseHandler getInstance() {
        if (instance == null) {
            instance = new MouseHandler();
        }
        return instance;
    }

    //! \brief Sets the coordinates when the mouse button is pressed.
    @Override
    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    //! Invalidates the saved mouse coordinates.
    public static void resetMouse() {
        MouseHandler.mouseX = -1;
        MouseHandler.mouseY = -1;
    }
}
