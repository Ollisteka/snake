package snake;

import java.awt.*;
import java.io.Serializable;

public class Wall implements Serializable {

    private Point location;

    public Point getLocation() {
        return location;
    }

    public Wall(int width, int height) {
        location = new Point(width, height);
    }
}
