package logic;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import lombok.Getter;

public class Level implements Serializable {

  Random rnd = new Random();
  @Getter
  private int width;
  @Getter
  private int height;
  private String levelName;
  private Set<Wall> mazeLocations;

  public Level(Config config, String level) {
    levelName = level;
    mazeLocations = new HashSet<>();
    width = config.getFieldWidth();
    height = config.getFieldHeight();
  }

  public String getLevelName() {
    return levelName;
  }

  public Set<Wall> getMazeLocations() {
    return mazeLocations;
  }

  public void setMazeLocations(Set<Wall> maze) {
    mazeLocations = maze;
  }

  public Set<Wall> createRandomField() {
    Set<Wall> maze = new HashSet();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (new Random().nextInt(50) == 0) {
          maze.add(new Wall(x, y));
        }
      }
    }
    return maze;
  }

  public Point findFreeSpot() {
    while (true) {
      int x = rnd.nextInt(width - 1);
      int y = rnd.nextInt(height - 1);
      boolean repeat = false;
      for (Wall wall : mazeLocations) {
        if (wall.getLocation().x == x && wall.getLocation().y == y) {
          repeat = true;
          break;
        }
      }
      if (repeat) {
        continue;
      }
      return new Point(x, y);
    }
  }

}
