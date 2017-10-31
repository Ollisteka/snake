package snake;

import java.awt.*;
import java.io.Serializable;

public class Config implements Serializable{
    private int fieldWidth;
    private int fieldHeight;
    private int pixelSize;
    private int windowHeight;
    private int windowWidth;
    private int timerTick;
    private Color textColor;
    private Color buttonBord;
    private Color background;
    private Font font;

    public Config(int width, int height, int pixel, int tick){
        fieldWidth = width;
        fieldHeight = height;
        pixelSize = pixel;
        windowHeight = fieldHeight * pixelSize + 85;
        windowWidth = fieldWidth * pixelSize + 285;
        timerTick = tick;
        textColor = Color.GREEN;
        buttonBord = Color.GREEN;
        background = Color.BLACK;
        font = new Font("Comic Sans MS", Font.BOLD, 20);
    }

    public int getFieldWidth(){
        return fieldWidth;
    }

    public int getFieldHeight(){
        return fieldHeight;
    }

    public int getPixelSize(){
        return pixelSize;
    }

    public int getWindowHeight(){
        return windowHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getTimerTick() {
        return timerTick;
    }

    public Color getBackground() {
        return background;
    }

    public Color getButtonBord() {
        return buttonBord;
    }

    public Font getFont() {
        return font;
    }

    public Color getTextColor() {
        return textColor;
    }
}
