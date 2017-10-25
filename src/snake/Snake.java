package snake;

import java.awt.*;

public class Snake
{
    public Snake()
    {
        length = 2;
        right = true;
    }

    private int length;
    public int getLength() {
        return length;
    }
    public void setLength(int newLength){
        length = newLength;
    }

    private boolean right;
    private boolean left;
    private boolean up;
    private boolean down;

    public boolean looksRight(){
        return right;
    }

    public boolean looksLeft(){
        return left;
    }

    public boolean looksUp(){
        return up;
    }

    public boolean looksDown(){
        return down;
    }

    public void moveRight(){
        if (!left) {
            right = true;
            left = false;
            up = false;
            down = false;
        }
    }

    public void moveUp() {
        if (!down) {
            up = true;
            left = false;
            right = false;
            down = false;
        }
    }

    public void moveDown() {
        if (!up) {
            down = true;
            left = false;
            up = false;
            right = false;
        }
    }
    public void moveLeft() {
        if (!right) {
            left = true;
            right = false;
            up = false;
            down = false;
        }
    }

    public void addLength() {
        length++;
    }

    public void eatFood(){
        addLength();
    }

}
