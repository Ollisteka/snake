package snake;

import java.io.Serializable;

public class Config implements Serializable{
    private int fieldWidth;
    private int fieldHeight;
    private int pixelSize;
    private int windowHeight;
    private int windowWidth;
    private int timerTick;

    public Config(int width, int height, int pixel, int tick){
        fieldWidth = width;
        fieldHeight = height;
        pixelSize = pixel;
        windowHeight = fieldHeight * pixelSize + 57;
        windowWidth = fieldWidth * pixelSize + 265;
        timerTick = tick;
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
}
