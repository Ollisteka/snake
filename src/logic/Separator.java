package logic;

import lombok.Getter;

public class Separator {

  @Getter
  private int xAxis;
  @Getter
  private int yAxis;

  public Separator(int x, int y) {
    xAxis = x;
    yAxis = y;
  }
}
