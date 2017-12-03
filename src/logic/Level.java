package logic;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class Level implements Serializable {

  @Getter
  @Setter
  Food food;
  @Getter
  @Setter
  private Snake snake = new Snake();
  @Getter
  private int width;
  @Getter
  private int height;
  private Random rnd = new Random();
  @Getter
  private String levelName;
  @Getter
  @Setter
  private Set<Wall> mazeLocations;
  @Getter
  @Setter
  private Set<Entrance> entrances;
  @Getter
  private Point[] snakeLocations;

  public Level(Config config, String level) {
    levelName = level;
    mazeLocations = new HashSet<>();
    entrances = new HashSet<>();
    width = config.getFieldWidth();
    height = config.getFieldHeight();
    snakeLocations = new Point[height * width];
    generateFood();
  }

  public void generateFood() {
    food = new Food(findFreeSpot());
  }

  public Set<Wall> createRandomField() {
    Set<Wall> maze = new HashSet();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (new Random().nextInt(50) == 0) {
          maze.add(new Wall(x, y));
        }
      }
    }
    return maze;
  }

  public Point findFreeSpot() {
    while (true) {
      int x = rnd.nextInt(width - 1);
      int y = rnd.nextInt(height - 1);
      boolean repeat = false;
      for (Wall wall : mazeLocations) {
        if (wall.getLocation().x == x && wall.getLocation().y == y) {
          repeat = true;
          break;
        }
      }
      if (repeat) {
        continue;
      }
      return new Point(x, y);
    }
  }

  public Set<Entrance> findOpenEntrances() {
    Set<Entrance> openedEntrances = new HashSet<>();
    for (Entrance entry : getEntrances()) {
      if (entry.isOpen()) {
        openedEntrances.add(entry);
      }
    }
    return openedEntrances;
  }

  /**
   * Размещает змейку при первом запуске игры
   */
  public void placeSnake() {
    snakeLocations[0] = findFreeSpot();
  }

  /**
   * Размещает змейку с учётом того, что она вылезла из какого то входа
   *
   * @param previousLevel прерыдущий уровень
   */
  public void placeSnake(Level previousLevel) {
    Point prevSnakeHead = previousLevel.snakeLocations[0];
    char inputEntry = previousLevel.getEntranceName(prevSnakeHead);
    snakeLocations[0] = findEntry(inputEntry);
  }

  public void tryEatFood(Game game) {
    Point[] snakeLocations = getSnakeLocations();
    if (snakeLocations[0].x == food.getLocation().x
        && snakeLocations[0].y == food.getLocation().y) {
      snake.eatFood();
      game.addScore();
      generateFood();
      int i = snake.getLength() - 1;
      snakeLocations[i] = new Point(snakeLocations[i - 1].x, snakeLocations[i - 1].y);
    }
  }

  /**
   * Ищет в данном уровне вход с названием inputEntry
   *
   * @return местоположение входа
   */
  public Point findEntry(char inputEntry) {
    Set<Entrance> openedEntrances = findOpenEntrances();
    for (Entrance openedEntry : openedEntrances) {
      if (openedEntry.getName() == inputEntry) {
        Point location = openedEntry.getLocation();
        return new Point(location.x, location.y);
      }
    }
    return null;
  }

  /**
   * Количество клеток поля, занятых змейкой
   */
  public int findSnakePartsOnBoard() {
    int count = 0;
    for (Point point : snakeLocations) {
      if (point != null) {
        count++;
      }
    }
    return count;
  }

  public void tryToDie() {
    int snakeOnBoard = findSnakePartsOnBoard();
    if (snakeOnBoard == 0) {
      return;
    }
    for (int j = 2; j < snakeOnBoard; j++) {
      if (snakeLocations[0].x == snakeLocations[j].x &&
          snakeLocations[0].y == snakeLocations[j].y) {
        snake.die();
      }
    }
    if (snakeLocations[0].x < 0 ||
        snakeLocations[0].y < 0 ||
        snakeLocations[0].x >= width ||
        snakeLocations[0].y >= height) {
      snake.die();
    }
    for (Wall wall : getMazeLocations()) {
      if (snakeLocations[0].x == wall.getLocation().x &&
          snakeLocations[0].y == wall.getLocation().y) {
        snake.die();
      }
    }
  }

  /**
   * Проверяем, находимся ли мы в ячейке с открытым входом.
   *
   * @return Вход, в который мы попали или null
   */
  public Entrance canMoveToNextLevel() {
    for (Entrance entrance : entrances) {
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

  public void moveSnake() {
    int snakeOnBoard = findSnakePartsOnBoard();
    Point tail = null;
    if (snakeOnBoard != 0 && snakeOnBoard != snake.getLength()) {
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

  /**
   * Ищет в данном уровне вход с местоположением location
   *
   * @return имя входа
   */
  public char getEntranceName(Point location) {
    Set<Entrance> openedEntrances = findOpenEntrances();
    for (Entrance openedEntry : openedEntrances) {
      Point entryLocation = openedEntry.getLocation();
      if (entryLocation.x == location.x && entryLocation.y == location.y) {
        return openedEntry.getName();
      }
    }
    //дефолтное значение для char == ошибка
    return '\u0000';
  }
}
