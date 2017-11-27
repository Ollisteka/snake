package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import GUI.GameFieldPanel;
import logic.Config;
import logic.Level;
import logic.Snake;
import org.junit.Test;

public class GameFieldPanelTest {

  @Test
  public void isDead() {
    Config config = new Config(25, 25, 25, 1000);
    GameFieldPanel field = new GameFieldPanel(config, new Level(config, "test"));
    Snake snake = field.getSnake();
    snake.moveRight();
    for (int i = 0; i < config.getFieldWidth(); i++) {
      field.moveSnake();
    }
    assertTrue(field.isSnakeDead());
  }

  @Test
  public void isNotDead() {
    Config config = new Config(25, 25, 25, 1000);
    GameFieldPanel field = new GameFieldPanel(config, new Level(config, "test"));
    assertFalse(field.isSnakeDead());
  }
}
