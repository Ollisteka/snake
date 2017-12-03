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
  private Level level;

  public GameFieldPanel(Game game, Level currentLevel) {
    initGameSettings(game, currentLevel);
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


  public void handlePreviousSnake(Level previousLevel, Snake snake) {
    //level.setSnake(previousLevel.getSnake());
    level.placeSnake(previousLevel, snake);
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

  private void paintGameOver(Graphics g) {
    g.drawImage(gameOver, width / 2 * pixel - 420, height / 2 * pixel - 240, this);
  }

  private void paintEntrances(Graphics g) {
    for (Entrance e : level.getEntrances()) {
      if (!e.isOpen()) {
        g.drawImage(closedImage, e.getLocation().x * pixel, e.getLocation().y * pixel, this);
      }
    }
  }

  private void paintWalls(Graphics g) {
    for (Wall wall : level.getMazeLocations()) {
      g.drawImage(wallIm, wall.getLocation().x * pixel, wall.getLocation().y * pixel, this);
    }
  }

  private void paintSnake(Graphics g, Snake snake) {
    Point[] snakeLocations = level.getSnakesBodies().get(snake);
    int snakeOnBoard = level.findSnakePartsOnBoard(snake);
    Point location = level.getFood().getLocation();
    g.drawImage(foodIm, location.x * pixel, location.y * pixel, this);
    g.drawImage(headIm, snakeLocations[0].x * pixel, snakeLocations[0].y * pixel, this);
    for (int i = 1; i < snakeOnBoard; i++) {
      if (snakeLocations[i].x >= width || snakeLocations[i].y >= height) {
        continue;
      }
      g.drawImage(snakeIm, snakeLocations[i].x * pixel, snakeLocations[i].y * pixel, this);
    }

  }

  private void paintScores(Graphics g) {
    g.setColor(Color.green);
    g.drawRect(0, 0, width * pixel, height * pixel);
    g.setColor(Color.cyan);
    g.drawString("Score:", width * pixel + 100, 100);
    g.drawString(Integer.toString(game.getScore()), width * pixel + 100, 150);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (Snake snake : game.getSnakes()) {
      if (!snake.isAlive()) {
        paintGameOver(g);
        paintScores(g);
        return;
      }
    }
    for (Snake snake : game.getSnakes()) {
      paintSnake(g, snake);
      paintWalls(g);
      paintEntrances(g);
    }
    //сюда можно запихнуть метод, который будет проверять все координаты, и рисовать поверх чёрное
    // если там нет змей.
    paintScores(g);
  }
}


