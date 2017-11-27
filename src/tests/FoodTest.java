package tests;

import logic.Config;
import logic.Food;
import logic.Level;
import org.junit.Assert;
import org.junit.Test;

public class FoodTest extends Assert {

  private String levelName = "test";
  @Test
  public void foodIntoTheField() {
    Config config = new Config(25, 25, 25, 250);
    Level level = new Level(config, levelName);
    Food food = level.getFood();
    assertTrue(food.getLocation().x >= 0);
    assertTrue(food.getLocation().y >= 0);
    assertTrue(food.getLocation().x < config.getFieldWidth());
    assertTrue(food.getLocation().y < config.getFieldHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void foodWithIllegalBounds() {
    Config config = new Config(-1, -1, 25, 250);
    Level level = new Level(config, levelName);
    Food food = level.getFood();
  }

}