package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import GUI.GameFieldPanel;
import java.awt.Point;
import logic.Config;
import logic.Level;
import org.junit.Test;

public class GameFieldPanelTest {

  @Test
  public void isDead() {
    Config config = new Config(25, 25, 25, 1000);
    GameFieldPanel gf = new GameFieldPanel(config, new Level(config, "test"));
    //gf.setTimerStop();
    Point[] arr = new Point[9];
    arr[0] = new Point(0, 0);
    arr[1] = new Point(1, 0);
    arr[2] = new Point(2, 0);
    arr[3] = new Point(2, 1);
    arr[4] = new Point(2, 2);
    arr[5] = new Point(1, 2);
    arr[6] = new Point(0, 2);
    arr[7] = new Point(0, 1);
    arr[8] = new Point(0, 0);
    gf.setSnakeLocations(arr);
    assertTrue(gf.isSnakeDead());
  }

  @Test
  public void isNotDead() {
    Config config = new Config(25, 25, 25, 1000);
    GameFieldPanel field = new GameFieldPanel(config, new Level(config, "test"));
    Point[] arr = new Point[9];
    arr[0] = new Point(0, 0);
    arr[1] = new Point(1, 0);
    arr[2] = new Point(2, 0);
    arr[3] = new Point(2, 1);
    arr[4] = new Point(2, 2);
    arr[5] = new Point(1, 2);
    arr[6] = new Point(0, 2);
    arr[7] = new Point(0, 1);
    arr[8] = new Point(1, 1);
    field.setSnakeLocations(arr);
    assertFalse(field.isSnakeDead());
  }

  @Test
  public void outOfTheBounds() {
    Config config = new Config(25, 25, 25, 1000);
    GameFieldPanel field = new GameFieldPanel(config, new Level(config, "test"));
    Point[] arr = new Point[3];
    arr[0] = new Point(-1, 0);
    arr[1] = new Point(0, 0);
    arr[2] = new Point(1, 0);
    field.setSnakeLocations(arr);
    assertTrue(field.isSnakeDead());
  }
}
