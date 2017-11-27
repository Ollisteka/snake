package logic;

import java.awt.Point;
import lombok.Getter;

public class Food {

  @Getter
  private Point location;

  public Food(Point location) {
    this.location = location;
  }
}