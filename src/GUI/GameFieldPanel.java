package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import logic.Config;
import logic.Food;
import logic.Level;
import logic.Snake;
import logic.Wall;
import lombok.Getter;
import lombok.Setter;

public class GameFieldPanel extends JPanel implements Serializable {

  private int width;
  private int height;
  private int pixel;
  private Image foodIm;
  private Image snakeIm;
  private Image gameOver;
  private Image wallIm;
  private Image headIm;
  private Food food;
  @Getter
  @Setter
  private boolean isPause = false;
  private Point[] snakeLocations;
  @Getter
  private Snake snake;
  private Level level;
  private Random rnd = new Random();


  public GameFieldPanel(Config config, Level currentLevel) {
    initGameSettings(config, currentLevel);
    initGameField();
  }

  public GameFieldPanel(Config baseConfiguration, Level level, GameFieldPanel previousState) {
    initGameSettings(baseConfiguration, level);
    //TODO в этом initGameField размещать змейку так, чтобы она вылазила из входа.
    //Наверное, для этого ещё movesnake надо как то модифицировать.
    initGameField(previousState);
  }

  private void initGameSettings(Config config, Level currentLevel) {
    width = config.getFieldWidth();
    height = config.getFieldHeight();
    pixel = config.getPixelSize();

    setBackground(config.getBackgroundColor());
    level = currentLevel;

    loadImages();

    setFocusable(true);
  }


  public Point[] getSnakeLocations() {
    return snakeLocations;
  }

  public void setSnakeLocations(Point[] locations) {
    snakeLocations = locations;
    while (snake.getLength() < snakeLocations.length) {
      snake.addLength();
    }
  }

  private void initGameField() {
    snake = new Snake();
    food = new Food(level);
    placeSnake();
  }

  private void initGameField(GameFieldPanel previousState) {
    snake = previousState.getSnake();
    food = new Food(level);
    //TODO сделать перегрузку для этого метода
    placeSnake();
  }

  private void placeSnake() {
    // потом сделаю, чтобы две клетки змейки размещались на карте, честное слово :)
    snakeLocations = new Point[height * width];
    snakeLocations[0] = level.findFreeSpot();
  }


  public void moveSnake() {
    for (int i = snake.getLength() - 1; i > 0; i--) {
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
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (!isSnakeDead()) {
      Point location = food.getLocation();
      g.drawImage(foodIm, location.x * pixel, location.y * pixel, this);
      g.drawImage(headIm, snakeLocations[0].x * pixel, snakeLocations[0].y * pixel, this);
      for (int i = 1; i < snake.getLength(); i++) {
        g.drawImage(snakeIm, snakeLocations[i].x * pixel, snakeLocations[i].y * pixel, this);
      }
      for (Wall wall : level.getMazeLocations()) {
        g.drawImage(wallIm, wall.getLocation().x * pixel, wall.getLocation().y * pixel, this);
      }
    } else {
      g.drawImage(gameOver, width / 2 * pixel - 420, height / 2 * pixel - 240, this);
    }
    g.setColor(Color.green);
    g.drawLine(0, 0, width * pixel, 0);
    g.drawLine(0, 0, 0, height * pixel);
    g.drawLine(width * pixel, 0, width * pixel, height * pixel);
    g.drawLine(0, height * pixel, width * pixel, height * pixel);
    g.setColor(Color.cyan);
    g.drawString("Score:", width * pixel + 100, 100);
    g.drawString(Integer.toString((snake.getLength() - 1) * 10), width * pixel + 100, 150);

  }

  public void tryEatFood() {
    if (snakeLocations[0].x == food.getLocation().x && snakeLocations[0].y == food
        .getLocation().y) {
      snake.eatFood();
      food = new Food(level);
      int i = snake.getLength() - 1;
      snakeLocations[i] = new Point(snakeLocations[i - 1].x, snakeLocations[i - 1].y);
    }
  }

  public boolean isSnakeDead() {
    for (int j = 2; j < snake.getLength(); j++) {
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


}
