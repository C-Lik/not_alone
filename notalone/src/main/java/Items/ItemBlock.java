package Items;

import Control.Camera;

import java.awt.*;

public abstract class ItemBlock {
    private final static Camera camera = Camera.getInstance();
    public Rectangle hitBox;
    protected int x, y, benefit;

    public ItemBlock(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    public void update() {
        hitBox.x = camera.getCamera() + this.x;
    }

    public abstract void draw(Graphics g);

    public int getBenefit() {
        return benefit;
    }
}
