package logic;

import lombok.Getter;

public class Snake {

  @Getter
  private int length;
  private boolean right;
  private boolean left;
  private boolean up;
  private boolean down;
  private boolean isAlive;

  public Snake() {
    length = 1;
    isAlive = true;
  }


  public boolean looksRight() {
    return right;
  }

  public boolean looksLeft() {
    return left;
  }

  public boolean looksUp() {
    return up;
  }

  public boolean looksDown() {
    return down;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public void moveRight() {
    if (!left) {
      right = true;
      up = down = left = false;
    }
  }

  public void moveUp() {
    if (!down) {
      up = true;
      left = right = down = false;
    }
  }

  public void stopSnake() {
    left = right = down = up = false;
  }

  public void moveDown() {
    if (!down) {
      down = true;
      left = up = right = false;
    }
  }

  public void moveLeft() {
    if (!right) {
      left = true;
      right = up = down = false;
    }
  }

  public void addLength() {
    length++;
  }

  public void eatFood() {
    addLength();
  }

  public void die() {
    stopSnake();
    isAlive = false;
  }

}
