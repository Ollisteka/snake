package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import logic.Config;
import logic.Entrance;
import logic.Game;
import logic.Level;
import logic.Snake;
import logic.Sublevels;
import logic.Wall;
import lombok.Getter;


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
  private Image secondHeadIm;
  private Image closedImage;

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
    ImageIcon q2 = new ImageIcon("head2.png");
    secondHeadIm = q2.getImage();
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

  private void paintSnake(Graphics g, Snake snake, boolean isSecond) {
    Point[] snakeLocations = level.getSnakesBodies().get(snake);
    int snakeOnBoard = level.findSnakePartsOnBoard(snake).size();
    if (isSecond){
      g.drawImage(secondHeadIm, snakeLocations[0].x * pixel, snakeLocations[0].y * pixel, this);
    } else {
      g.drawImage(headIm, snakeLocations[0].x * pixel, snakeLocations[0].y * pixel, this);
    }
    for (int i = 1; i < snakeOnBoard; i++) {
      if (snakeLocations[i].x >= width || snakeLocations[i].y >= height) {
        continue;
      }
      g.drawImage(snakeIm, snakeLocations[i].x * pixel, snakeLocations[i].y * pixel, this);
    }
  }

  private void paintScores(Graphics g) {
    for (int i=0; i < game.getSnakes().size(); i++) {
      Snake snake = game.getSnakes().get(i);
      int offset;
      if (i == 0) {
        g.setColor(Color.yellow);
        offset = 100;
      } else {
        g.setColor(Color.cyan);
        offset = 150;
      }
      g.drawString("Score:", width * pixel + 100, offset);
      g.drawString(Integer.toString(snake.getLength() * 10), width * pixel + 100, offset + 25);
    }
  }

  private void paintFrame(Graphics g) {
    g.setColor(Color.green);
    g.drawRect(0, 0, width * pixel, height * pixel);
  }

  private void paintFood(Graphics g) {
    Point location = level.getFood().getLocation();
    g.drawImage(foodIm, location.x * pixel, location.y * pixel, this);
  }


  private void handleSublevels(Graphics g) {
    int xAxis = game.getCurrentLevel().getXAxis();
    int yAxis = game.getCurrentLevel().getYAxis();
    HashMap<Sublevels, List<Point>> sublevels = game.getCurrentLevel().getSubLevels();
    if (sublevels.get(Sublevels.UpperLeft).size() == 0) {
      paintHidingPicture(g, 0, xAxis, 0, yAxis);
    }
    if (sublevels.get(Sublevels.UpperRight).size() == 0) {
      paintHidingPicture(g, xAxis, width, 0, yAxis);
    }
    if (sublevels.get(Sublevels.LowerLeft).size() == 0) {
      paintHidingPicture(g, 0, xAxis, yAxis, height);
    }
    if (sublevels.get(Sublevels.LowerRight).size() == 0) {
      paintHidingPicture(g, xAxis, width, yAxis, height);
    }
  }
  private void paintHidingPicture(Graphics g, int xFrom, int xTo, int yFrom, int yTo) {
    //тут надо нарисовать картинку с такими координатами:
    //(xFrom,yFrom)
    //+---------------+
    //+               +
    //+---------------+ (xTo, yTo)
    //хм, возможно не совсем с такими, надо учитывать, что дырки
    //в стенах должны быть видны
    //throw new NotImplementedException();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (game.getCurrentLevel().getSubLevels().size() != 0) {
      handleSublevels(g);
    }
    for (int i=0; i < game.getSnakes().size(); i++) {
      Snake snake = game.getSnakes().get(i);
      if (!snake.isAlive() || game.isPaused()) {
        game.setPaused(true);
        paintGameOver(g);
        paintScores(g);
        return;
      } else {
        paintSnake(g, snake, i != 0);
        paintFrame(g);
        paintWalls(g);
        paintEntrances(g);
        paintFood(g);
      }
    }
    paintScores(g);
  }


}


