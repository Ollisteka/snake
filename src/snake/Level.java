package snake;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;

public class Level implements Serializable {
    private int WIDTH;
    private int HEIGHT;
    private int PIXEL;
    private String levelName;
    private HashSet<Wall> mazeLocations;

    public String getLevelName() {
        return levelName;
    }

    public HashSet<Wall> getMazeLocations() {
        return mazeLocations;
    }

    public void setMazeLocations(HashSet<Wall> maze){
        mazeLocations = maze;
    }

    public Level(Config config, String level){
        levelName = level;
        mazeLocations = new HashSet();
        WIDTH = config.getFieldWidth();
        HEIGHT = config.getFieldHeight();
        PIXEL = config.getPixelSize();
    }

    public HashSet createRandomField(){
        HashSet maze = new HashSet();
        for (int x = 0; x < WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                if (new Random().nextInt(50) == 0)
                    maze.add(new Wall(x, y));
            }
        }
        return maze;
    }

}
