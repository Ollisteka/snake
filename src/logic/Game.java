package logic;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class Game {

  @Setter
  @Getter
  private List<Snake> snakes = new ArrayList<>();
  @Setter
  @Getter
  private int score;
  @Getter
  private Config config;
  @Getter
  @Setter
  private Map<Integer, Entrance> closedEntrances = new HashMap<>();
  @Getter
  private List<Level> levels = new ArrayList<>();
  @Getter
  @Setter
  private boolean paused = false;
  @Getter
  @Setter
  private Level currentLevel;

  public Game(Config config, Level level, boolean twoPlayers) {
    this.config = config;
    levels.add(level);
    currentLevel = level;
    placeSnakes(twoPlayers);
  }


  public Game(Config config, List<Level> levels, boolean twoPlayers) {
    this.config = config;
    closedEntrances = getClosedEntrances(levels);
    this.levels = levels;
    currentLevel = levels.get(0);
    placeSnakes(twoPlayers);
  }

  private void placeSnakes(boolean twoPlayers) {
    snakes.add(new Snake());
    if (twoPlayers) {
      snakes.add(new Snake());
    }

    for (Level level : levels) {
      level.putSnakes(getSnakes());
    }

    for (Snake snake : getSnakes()) {
      currentLevel.placeSnake(snake);
      if (currentLevel.getFood() == null) {
        currentLevel.generateFood();
      }
    }

  }

  private void addScore() {
    setScore(getScore() + 10);
    tryToOpenEntrance();
  }

  private void tryToOpenEntrance() {
    Integer score = getScore();
    if (getClosedEntrances().containsKey(score)) {
      Entrance openingEntrance = getClosedEntrances().get(score);
      opened:
      for (Level level : levels) {
        for (Entrance entrance : level.getEntrances()) {
          if (entrance == openingEntrance) {
            entrance.setOpen(true);
            break opened;
          }
        }
      }
      getClosedEntrances().remove(score);
    }
  }

  /**
   * Найти все закрытые входы в игре (на всех уровнях) и поставить им в соответствие очки,
   * котовые нужно набрать для открытия двери
   *
   * @param levels уровни, которые существуют в этой игре
   * @return словарик, где ключь - это очки, а значение - "вход"/дверь уровня
   */
  private Map<Integer, Entrance> getClosedEntrances(List<Level> levels) {
    Map<Integer, Entrance> closedEntrances = new HashMap<>();
    Integer count = 50;
    for (Level level : levels) {
      for (Entrance entrance : level.getEntrances()) {
        if (!entrance.isOpen()) {
          closedEntrances.put(count, entrance);
          count += 50;
        }
      }
    }

    return closedEntrances;
  }

  public void tryEatFood(Game game, Snake snake) {
    Point[] snakeLocations = currentLevel.getSnakesBodies().get(snake);
    if (snakeLocations[0].x == currentLevel.food.getLocation().x
        && snakeLocations[0].y == currentLevel.food.getLocation().y) {
      snake.eatFood();
      game.addScore();
      currentLevel.generateFood();
      int i = snake.getLength() - 1;
      snakeLocations[i] = new Point(snakeLocations[i - 1].x, snakeLocations[i - 1].y);
    }
  }

  public void moveSnake(Level level, Snake snake) {
    Point[] snakeLocations = level.getSnakesBodies().get(snake);
    if (snakeLocations == null) {
      return;
    }
    int snakeOnBoard = level.findSnakePartsOnBoard(snake);
    Point tail = null;
    if (snakeOnBoard == 0) {
      return;
    }
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

  public void tryToDie(Snake snake) {
    Point[] snakeLocations = currentLevel.getSnakesBodies().get(snake);
    int snakeOnBoard = currentLevel.findSnakePartsOnBoard(snake);
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
        snakeLocations[0].x >= currentLevel.getWidth() ||
        snakeLocations[0].y >= currentLevel.getHeight()) {
      snake.die();
    }
    for (Wall wall : currentLevel.getMazeLocations()) {
      if (snakeLocations[0].x == wall.getLocation().x &&
          snakeLocations[0].y == wall.getLocation().y) {
        snake.die();
      }
    }
  }

}
