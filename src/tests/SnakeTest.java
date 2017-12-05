package tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import logic.Snake;
import org.junit.Test;

public class SnakeTest {

  @Test
  public void createSnake() {
    Snake snake = new Snake();
    assertEquals(1, snake.getLength());
    assertEquals(false, snake.looksRight());
    assertEquals(false, snake.looksLeft());
    assertEquals(false, snake.looksUp());
    assertEquals(false, snake.looksDown());
  }

  @Test
  public void moveUpSnake() {
    Snake snake = new Snake();
    snake.moveUp();
    assertTrue(snake.looksUp());
    assertFalse(snake.looksDown());
    assertFalse(snake.looksRight());
    assertFalse(snake.looksLeft());
  }

  @Test
  public void moveDownSnake() {
    Snake snake = new Snake();
    snake.moveDown();
    assertTrue(snake.looksDown());
    assertFalse(snake.looksUp());
    assertFalse(snake.looksRight());
    assertFalse(snake.looksLeft());
  }

  @Test
  public void doNotMoveLeftSnake() {
    Snake snake = new Snake();
    snake.moveRight();
    snake.moveLeft();
    assertTrue(snake.looksRight());
    assertFalse(snake.looksDown());
    assertFalse(snake.looksLeft());
    assertFalse(snake.looksUp());
  }

  @Test
  public void moveRightSnake() {
    Snake snake = new Snake();
    snake.moveRight();
    assertTrue(snake.looksRight());
    assertFalse(snake.looksDown());
    assertFalse(snake.looksUp());
    assertFalse(snake.looksLeft());
  }

  @Test
  public void moveLeftSnake() {
    Snake snake = new Snake();
    snake.moveUp();
    snake.moveLeft();
    assertTrue(snake.looksLeft());
    assertFalse(snake.looksDown());
    assertFalse(snake.looksUp());
    assertFalse(snake.looksRight());
  }

  @Test
  public void checkAddLength() {
    Snake snake = new Snake();
    snake.addLength();
    assertEquals(2, snake.getLength());
  }

  @Test
  public void checkEatFood() {
    Snake snake = new Snake();
    snake.eatFood();
    assertEquals(2, snake.getLength());
  }

  @Test
    public void dieSnakeTest() {
      Snake snake = new Snake();
      snake.die();
      assertFalse(snake.isAlive());
      assertFalse(snake.looksDown());
      assertFalse(snake.looksLeft());
      assertFalse(snake.looksRight());
      assertFalse(snake.looksUp());
  }
}
