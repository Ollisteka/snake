package logic;

import java.awt.Point;

public class Food {

  private Point location;

  public Food(Level level) {
    createFood(level);
  }

  public Point getLocation() {
    return location;
  }

  private void createFood(Level level) {
    location = level.findFreeSpot();
  }
}