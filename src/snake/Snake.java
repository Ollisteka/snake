package snake;

public class Snake {
    private int length;
    private boolean right;
    private boolean left;
    private boolean up;
    private boolean down;
    private boolean isAlive;

    public Snake()
    {
        length = 2;
        right = true;
        isAlive = true;
    }

    public int getLength() {
        return length;
    }
    public void setLength(int newLength){
        length = newLength;
    }

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

    public boolean isAlive(){
        return isAlive;
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
        if (!down) {
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

    public void eatFood() {
        addLength();
    }

    public void die() {
        isAlive = false;
    }

}
