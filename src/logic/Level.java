package logic;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class Level implements Serializable {

  @Getter
  @Setter
  private Food food;

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
  private Set<Separator> subLevelSeparators;
  @Getter
  @Setter
  private Set<Entrance> entrances;
  @Getter
  @Setter
  private Map<Snake, Point[]> snakesBodies;
  @Getter
  private int xAxis;
  @Getter
  private int yAxis;
  @Getter
  private HashMap<Sublevels, List<Point>> subLevels = new HashMap<>();

  public Level(Config config, String level) {
    levelName = level;
    snakesBodies = new HashMap<>();
    mazeLocations = new HashSet<>();
    entrances = new HashSet<>();
    width = config.getFieldWidth();
    height = config.getFieldHeight();
    //generateFood();
  }

  public void initializeSnakes(List<Snake> snakes) {
    for (Snake snake : snakes) {
      snakesBodies.put(snake, new Point[height * width]);
      placeSnake(snake);
    }
  }

  public void generateFood() {
    food = new Food(findFreeSpot());
  }

  public void createRandomField() {
    mazeLocations = new HashSet();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (new Random().nextInt(50) == 0) {
          mazeLocations.add(new Wall(x, y));
        }
      }
    }
    if (mazeLocations.size() == 0) {
      mazeLocations.add(new Wall(0, 0));
    }
  }

  public void initAxis() {
    for (Separator separator : getSubLevelSeparators()) {
      if (separator.isHorizontal()) {
        xAxis = separator.getCoordinate();
      } else {
        yAxis = separator.getCoordinate();
      }
    }
  }


  public Point findFreeSpot() {
    while (true) {
      boolean repeat = false;
      int x = rnd.nextInt(width - 1);
      int y = rnd.nextInt(height - 1);
      for (Wall wall : mazeLocations) {
        if (wall.getLocation().x == x && wall.getLocation().y == y) {
          repeat = true;
          break;
        }
      }
      outer:
      for (Snake snake : snakesBodies.keySet()) {
        for (Point point : findSnakePartsOnBoard(snake)) {
          if (point.x == x && point.y == y) {
            repeat = true;
            break outer;
          }
        }
      }
      if (!repeat) {
        return new Point(x, y);
      }
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
  private void placeSnake(Snake snake) {
    snakesBodies.get(snake)[0] = findFreeSpot();
  }

  /**
   * Размещает змейку с учётом того, что она вылезла из какого то входа
   *
   * @param previousLevel прерыдущий уровень
   */
  public void placeSnake(Level previousLevel, Snake snake) {
    Point prevSnakeHead = previousLevel.snakesBodies.get(snake)[0];
    char inputEntry = previousLevel.getOpenedEntranceName(prevSnakeHead);
    snakesBodies.get(snake)[0] = findOpenedEntry(inputEntry);
  }


  /**
   * Ищет в данном уровне открытый вход с названием inputEntry
   *
   * @return местоположение входа
   */
  public Point findOpenedEntry(char inputEntry) {
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
   * Клетки поля, занятых змейкой
   */
  public Set<Point> findSnakePartsOnBoard(Snake snake) {
    Set<Point> result = new HashSet<Point>();
    for (Point point : snakesBodies.get(snake)) {
      if (point != null) {
        result.add(point);
      }
    }
    return result;
  }


  /**
   * Проверяем, находимся ли мы в ячейке с открытым входом.
   *
   * @return Вход, в который мы попали или null
   */
  public Entrance canMoveToNextLevel(Snake snake) {
    Point[] snakeLocations = snakesBodies.get(snake);
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


  /**
   * Ищет в данном уровне вход с местоположением location
   *
   * @return имя входа
   */
  public char getOpenedEntranceName(Point location) {
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
