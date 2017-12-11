package logic;

import lombok.Getter;

public class Separator {

  @Getter
  private int coordinate;
  @Getter
  private boolean isHorizontal;

  public Separator(int coordinate, boolean isHorizontal) {
    this.coordinate = coordinate;
    this.isHorizontal = isHorizontal;
  }
}
