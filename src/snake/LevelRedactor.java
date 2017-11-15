package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.HashSet;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class LevelRedactor extends JPanel implements Serializable {

  private int width;
  private int height;
  private int pixel;
  private Point location;
  private Image bush;
  private HashSet maze;
  private Level level;

  public LevelRedactor(Config config) {
    width = config.getFieldWidth();
    height = config.getFieldHeight();
    pixel = config.getPixelSize();
    maze = new HashSet();
    location = new Point(0, 0);
    level = new Level(config, "level");
    setBackground(config.getBackgroundColor());
    loadImages();
    addKeyListener(new FieldKeyListener());
    setFocusable(true);
    repaint();
  }

  public Level getLevel() {
    level.setMazeLocations(maze);
    return level;
  }

  public HashSet getMaze() {
    return maze;
  }

  @Override
  public Point getLocation() {
    return location;
  }

  @Override
  public void setLocation(int x, int y) {
    location.x = x;
    location.y = y;
  }

  private void loadImages() {
    ImageIcon f = new ImageIcon("bush.jpg");
    bush = f.getImage();
  }

  private void addNewWall() {
    Wall newWall = new Wall(location.x, location.y);
    if (!maze.contains(newWall)) {
      maze.add(newWall);
    } else {
      maze.remove(newWall);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
        /*
        Iterator iterator = maze.iterator();
        while (iterator.hasNext()){
            g.drawImage(bush,((Wall) iterator.next()).getLocation().x * pixel,
                    ((Wall) iterator.next()).getLocation().y * pixel,this);
        }*/
    Wall[] arr = (Wall[]) maze.toArray(new Wall[maze.size()]);
    for (Wall wall : arr) {
      g.drawImage(bush, wall.getLocation().x * pixel,
          wall.getLocation().y * pixel, this);
    }
    g.setColor(Color.cyan);
    g.drawRect(location.x * pixel, location.y * pixel, pixel, pixel);
    g.setColor(Color.green);
    g.drawRect(0, 0, width * pixel, height * pixel);
  }

  public class FieldKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      super.keyPressed(e);
      int key = e.getKeyCode();
      if (key == KeyEvent.VK_LEFT && getLocation().x > 0) {
        setLocation(--location.x, location.y);
      }
      if (key == KeyEvent.VK_RIGHT && getLocation().x < width - 1) {
        setLocation(++location.x, location.y);
      }
      if (key == KeyEvent.VK_UP && getLocation().y > 0) {
        setLocation(location.x, --location.y);
      }
      if (key == KeyEvent.VK_DOWN && getLocation().y < height - 1) {
        setLocation(location.x, ++location.y);
      }
      if (key == KeyEvent.VK_SPACE) {
        addNewWall();
      }
      repaint();
    }
  }
}
