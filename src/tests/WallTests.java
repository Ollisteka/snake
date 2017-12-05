package tests;

import logic.Wall;
import org.junit.Assert;
import org.junit.Test;

public class WallTests {

    @Test
    public void wallsAreEqual() {
      Wall firstWall = new Wall(5, 4);
      Wall secondWall = firstWall;
      Assert.assertEquals(firstWall, secondWall);
    }

    @Test
    public void wallsAreEqual2() {
      Wall firstWall = new Wall(5, 4);
      Wall secondWall = new Wall(5, 4);
      Assert.assertEquals(firstWall, secondWall);
    }

    @Test
    public void wallsAreNotEqual() {
        Wall firstWall = new Wall(5, 4);
        Wall secondWall = new Wall(4, 5);
        Assert.assertNotSame(firstWall, secondWall);
    }

    @Test
    public void wallsGetter() {
      Wall wall = new Wall(5, 4);
      int x = wall.getLocation().x;
      int y = wall.getLocation().y;
      Assert.assertEquals(x, 5);
      Assert.assertEquals(y, 4);
    }
}
