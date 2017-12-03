package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import logic.Config;
import logic.Entrance;
import logic.Game;
import logic.Level;
import logic.Snake;
import logic.Wall;
import lombok.Getter;
import lombok.Setter;

public class GameFieldPanel extends JPanel implements Serializable {

  private Game game;
  private int width;
  private int height;
  private int pixel;
  private Image foodIm;
  private Image snakeIm;
  private Image gameOver;
  private Image wallIm;
  private Image headIm;
  private Image closedImage;
  @Getter
  @Setter
  private boolean isPause = false;
  @Getter
  private Point[] snakeLocations = new Point[height * width];
  @Getter
  private Snake snake;
  @Getter
  private Level level;

  public GameFieldPanel(Game game, Level currentLevel) {
    initGameSettings(game, currentLevel);
    initGameField();
  }

  private void initGameSettings(Game game, Level currentLevel) {
    this.game = game;
    Config config = game.getConfig();
    width = config.getFieldWidth();
    height = config.getFieldHeight();
    pixel = config.getPixelSize();

    setBackground(config.getBackgroundColor());
    level = currentLevel;

    loadImages();

    setFocusable(true);
  }

  private void initGameField() {
    snake = new Snake();
    placeSnake();
    level.generateFood();
  }

  public void handlePreviousSnake(GameFieldPanel previousState) {
    snake = previousState.getSnake();
    placeSnake(previousState);
  }

  /**
   * Размещает змейку при первом запуске игры
   */
  private void placeSnake() {
    snakeLocations = new Point[height * width];
    snakeLocations[0] = level.findFreeSpot();
  }

  /**
   * Размещает змейку с учётом того, что она вылезла из какого то входа
   *
   * @param previousState прерыдущее состояние игры
   */
  private void placeSnake(GameFieldPanel previousState) {
    Point prevSnakeHead = previousState.getSnakeLocations()[0];
    char inputEntry = previousState.level.getEntranceName(prevSnakeHead);
    snakeLocations[0] = level.findEntry(inputEntry);
  }

  /**
   * Количество клеток поля, занятых змейкой
   * @return
   */
  private int findSnakePartsOnBoard() {
    int count = 0;
    for (Point point : snakeLocations) {
      if (point != null) {
        count++;
      }
    }
    return count;
  }

  public void moveSnake() {
    int snakeOnBoard = findSnakePartsOnBoard();
    Point tail = null;
    if (snakeOnBoard != snake.getLength()) {
      tail =
          new Point(snakeLocations[snakeOnBoard - 1].x, snakeLocations[snakeOnBoard - 1].y);
    }
    for (int i = snakeOnBoard - 1; i > 0; i--) {
      snakeLocations[i].x = snakeLocations[i - 1].x;
      snakeLocations[i].y = snakeLocations[i - 1].y;
    }

    if (snake.looksRight()) {
      snakeLocations[0].x++;

    }
    if (snake.looksLeft()) {
      snakeLocations[0].x--;

    }
    if (snake.looksUp()) {
      snakeLocations[0].y--;

    }
    if (snake.looksDown()) {
      snakeLocations[0].y++;
    }
    if (snakeOnBoard != snake.getLength()) {
      snakeLocations[snakeOnBoard] = tail;
    }
  }

  private void loadImages() {
    ImageIcon f = new ImageIcon("food.png");
    foodIm = f.getImage();
    ImageIcon s = new ImageIcon("snake.png");
    snakeIm = s.getImage();
    ImageIcon g = new ImageIcon("gameOver.jpg");
    gameOver = g.getImage();
    ImageIcon p = new ImageIcon("bush.jpg");
    wallIm = p.getImage();
    ImageIcon q = new ImageIcon("head.png");
    headIm = q.getImage();
    ImageIcon c = new ImageIcon("closed_lock.png");
    closedImage = c.getImage();
  }

  public void tryEatFood() {
    if (snakeLocations[0].x == level.getFood().getLocation().x
        && snakeLocations[0].y == level.getFood().getLocation().y) {
      snake.eatFood();
      game.addScore();
      level.generateFood();
      int i = snake.getLength() - 1;
      snakeLocations[i] = new Point(snakeLocations[i - 1].x, snakeLocations[i - 1].y);
    }
  }

  /**
   * Проверяем, находимся ли мы в ячейке с открытым входом.
   * @return Вход, в который мы попали или null
   */
  public Entrance canMoveToNextLevel() {
    for (Entrance entrance : level.getEntrances()) {
      if (snakeLocations[0].x == entrance.getLocation().x &&
          snakeLocations[0].y == entrance.getLocation().y) {

        if (entrance.isOpen()) {
          return entrance;
        } else {
          return null;
        }
      }
    }
    return null;
  }

  public boolean isSnakeDead() {
    int snakeOnBoard = findSnakePartsOnBoard();
    for (int j = 2; j < snakeOnBoard; j++) {
      if (snakeLocations[0].x == snakeLocations[j].x &&
          snakeLocations[0].y == snakeLocations[j].y) {
        snake.die();
        return true;
      }
    }
    if (snakeLocations[0].x < 0 ||
        snakeLocations[0].y < 0 ||
        snakeLocations[0].x >= width ||
        snakeLocations[0].y >= height) {
      snake.die();
      return true;
    }
    for (Wall wall : level.getMazeLocations()) {
      if (snakeLocations[0].x == wall.getLocation().x &&
          snakeLocations[0].y == wall.getLocation().y) {
        snake.die();
        return true;
      }
    }
    return false;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int snakeOnBoard = findSnakePartsOnBoard();
    if (!isSnakeDead()) {
      Point location = level.getFood().getLocation();
      g.drawImage(foodIm, location.x * pixel, location.y * pixel, this);
      g.drawImage(headIm, snakeLocations[0].x * pixel, snakeLocations[0].y * pixel, this);
      for (int i = 1; i < snakeOnBoard; i++) {
        if (snakeLocations[i].x >= width || snakeLocations[i].y >= height) {
          continue;
        }
        g.drawImage(snakeIm, snakeLocations[i].x * pixel, snakeLocations[i].y * pixel, this);
      }
      for (Wall wall : level.getMazeLocations()) {
        g.drawImage(wallIm, wall.getLocation().x * pixel, wall.getLocation().y * pixel, this);
      }
      for (Entrance e : level.getEntrances()) {
        if (!e.isOpen()) {
          g.drawImage(closedImage, e.getLocation().x * pixel, e.getLocation().y * pixel, this);
        }
      }
    } else {
      g.drawImage(gameOver, width / 2 * pixel - 420, height / 2 * pixel - 240, this);
    }
    g.setColor(Color.green);
    g.drawRect(0, 0, width*pixel, height*pixel);
    g.setColor(Color.cyan);
    g.drawString("Score:", width * pixel + 100, 100);
    g.drawString(Integer.toString(game.getScore()), width * pixel + 100, 150);

  }

}
