package tests;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import logic.Config;
import logic.Entrance;
import logic.Level;
import logic.Snake;
import logic.Wall;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LevelTests {

  private Level level;
  private Config configuration = new Config(5, 5, 5, 5);
  private String name = "Level";
  private Snake snake = new Snake();
  private Point openedEntry = new Point(0, 0);
  private Point closedEntry = new Point(0, 1);
  private Entrance openedEntrance = new Entrance(openedEntry.x, openedEntry.y, 'a', true);
  private Entrance closedEntrance = new Entrance(closedEntry.x, closedEntry.y, 'A', false);

  @Before
  public void setUp() {
    level = new Level(configuration, name);

    Set<Entrance> entrances = new HashSet<>();
    entrances.add(openedEntrance);
    entrances.add(closedEntrance);
    level.setEntrances(entrances);

    List<Snake> snakes = new ArrayList<>();
    snakes.add(snake);
    level.initializeSnakes(snakes);
  }

  @Test
  public void levelShould_HaveParameters_AsInConfig() {
    Assert.assertEquals(name, level.getLevelName());
    Assert.assertEquals(5, level.getHeight());
    Assert.assertEquals(5, level.getWidth());
  }

  @Test
  public void levelShould_HaveFood_AtCreation() {
    Assert.assertTrue(level.getFood() != null);
  }

  @Test
  public void levelShould_MakeMaze_IfAsked() {
    level.createRandomField();
    Assert.assertTrue(level.getMazeLocations().size() != 0);
  }

  @Test
  public void levelShould_HandleEntrancesCorrectly() {
    Assert.assertEquals(2, level.getEntrances().size());
    Assert.assertEquals(1, level.findOpenEntrances().size());
  }

  @Test
  public void levelShould_FindOpenEntry_ByPoint() {
    Assert.assertEquals('\u0000', level.getOpenedEntranceName(new Point(0, 1)));
    Assert.assertEquals('\u0000', level.getOpenedEntranceName(new Point(999, 999)));
    Assert.assertEquals('a', level.getOpenedEntranceName(new Point(0, 0)));
  }

  @Test
  public void levelShould_FindOpenEntry_ByName() {
    Assert.assertEquals(new Point(0, 0), level.findOpenedEntry('a'));
    Assert.assertEquals(null, level.findOpenedEntry('z'));
  }

  @Test
  public void levelShould_PlaceSnakeCorrectly() {
    Point snakeHead = level.getSnakesBodies().get(snake)[0];
    for (Wall wall : level.getMazeLocations()) {
      if (snakeHead.y == wall.getLocation().y && snakeHead.x == wall.getLocation().x) {
        Assert.assertTrue(false);
      }
    }
    for (Entrance entry : level.getEntrances()) {
      if (snakeHead.y == entry.getLocation().y && snakeHead.x == entry.getLocation().x) {
        Assert.assertTrue(false);
      }
    }
    Assert.assertTrue(snakeHead.x != level.getFood().getLocation().x
        || snakeHead.y != level.getFood().getLocation().y);

  }

  @Test
  public void levelShould_DetectWhen_SnakeCanMoveToNextLevel() {
    Point[] newSnakeLocations = new Point[level.getWidth() * level.getHeight()];
    newSnakeLocations[0] = openedEntry;
    level.getSnakesBodies().put(snake, newSnakeLocations);

    Assert.assertEquals(openedEntrance, level.canMoveToNextLevel(snake));

    newSnakeLocations[0] = new Point(3, 3);
    level.getSnakesBodies().put(snake, newSnakeLocations);

    Assert.assertEquals(null, level.canMoveToNextLevel(snake));

    newSnakeLocations[0] = closedEntry;
    level.getSnakesBodies().put(snake, newSnakeLocations);

    Assert.assertEquals(null, level.canMoveToNextLevel(snake));
  }
}
