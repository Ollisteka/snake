package logic;

import java.awt.Point;
import lombok.Getter;
import lombok.Setter;

public class Entrance {

  @Getter
  private Point location;
  @Getter
  @Setter
  private boolean open;

  public Entrance(int width, int height, boolean opened) {
    location = new Point(width, height);
    open = opened;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Entrance entrance = (Entrance) obj;

    return location != null ? location.equals(entrance.location) : entrance.location == null;
  }

  @Override
  public int hashCode() {
    return location != null ? location.hashCode() : 0;
  }

}
