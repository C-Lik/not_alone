package Items;

import Control.Camera;

import java.awt.*;

public abstract class ItemBlock {
    public Rectangle hitBox;
    protected final int x, y;
    protected int benefit;
    private final static Camera camera = Camera.getInstance();

    public ItemBlock(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    public void update() {
        hitBox.x = camera.getCameraXAxis() + this.x;
    }

    public abstract void draw(Graphics g);

    public int getBenefit() {
        return benefit;
    }
}
