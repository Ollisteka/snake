package logic;

import java.awt.Point;
import java.io.Serializable;
import lombok.Getter;

public class Wall implements Serializable {

  @Getter
  private Point location;

  public Wall(int x, int y) {
    location = new Point(x, y);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Wall wall = (Wall) o;

    return location != null ? location.equals(wall.location) : wall.location == null;
  }

  @Override
  public int hashCode() {
    return location != null ? location.hashCode() : 0;
  }
}
