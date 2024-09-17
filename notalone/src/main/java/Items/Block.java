package Items;

import MainGame.Game;

import java.awt.*;

public class Block extends ItemBlock {

    public Block(int xPos, int yPos) {
        super(xPos, yPos);
        hitBox = new Rectangle(x, y, Game.tileSize, Game.tileSize);
    }

    @Override
    public void draw(Graphics g) {}
}
