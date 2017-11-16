package logic;

import java.awt.Point;
import java.util.Random;

public class Food {

  private Point location;

  public Food(int width, int height) {
    location = new Point();
    createFood(width, height);
  }

  public Point getLocation() {
    return location;
  }

  public void createFood(int width, int height) {
    int X = new Random().nextInt(width);
    int Y = new Random().nextInt(height);
    location = new Point(X, Y);
  }
}