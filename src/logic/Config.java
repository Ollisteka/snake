package logic;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import lombok.Getter;

public class Config implements Serializable {

  @Getter
  private int fieldWidth;
  @Getter
  private int fieldHeight;
  @Getter
  private int pixelSize;
  @Getter
  private int windowHeight;
  @Getter
  private int windowWidth;
  @Getter
  private int timerTick;
  @Getter
  private Color textColor;
  @Getter
  private Color buttonBordColor;
  @Getter
  private Color backgroundColor;
  @Getter
  private Font font;

  public Config(int width, int height, int pixel, int tick) {
    fieldWidth = width;
    fieldHeight = height;
    pixelSize = pixel;
    windowHeight = fieldHeight * pixelSize + 85;
    windowWidth = fieldWidth * pixelSize + 285;
    timerTick = tick;
    textColor = Color.GREEN;
    buttonBordColor = Color.GREEN;
    backgroundColor = Color.BLACK;
    font = new Font("Comic Sans MS", Font.BOLD, 20);
  }
}
