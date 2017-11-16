package logic;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Food {

  private Point location;
  private Random rnd = new Random();

  public Food(int width, int height, Set<Wall> mazeLocations) {
    location = new Point();
    createFood(width, height, mazeLocations);
  }
  public Food(int width, int height) {
    location = new Point();
    createFood(width, height, new HashSet<Wall>() {
    });
  }

  public Point getLocation() {
    return location;
  }

  public void createFood(int width, int height, Set<Wall> mazeLocations) {
    while (true) {
      int x = rnd.nextInt(width - 1);
      int y = rnd.nextInt(height - 1);
      boolean repeat = false;
      for (Wall wall : mazeLocations) {
        if (wall.getLocation() == new Point(x, y)) {
          repeat = true;
          break;
        }
      }
      if (repeat) {
        continue;
      }
      location = new Point(x, y);
      break;
    }
  }
}