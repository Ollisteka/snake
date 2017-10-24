package snake;

import java.awt.*;

public class Wall {

    private Point location;

    public Point getLocation() {
        return location;
    }

    public Wall(int width, int height) {
        location = new Point(width, height);
    }
}
