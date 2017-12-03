package logic;

import java.awt.Point;
import lombok.Getter;
import lombok.Setter;

public class Entrance {

  @Getter
  private Point location;
  @Getter
  private char name;
  @Getter
  @Setter
  private boolean open;
  @Getter
  private String level;

  public Entrance(int x, int y, char name, String levelName, boolean opened) {
    location = new Point(x, y);
    open = opened;
    this.name = name;
    level = levelName;
  }

  public boolean isOpen() {
    return open;
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
