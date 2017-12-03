package logic;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class Game {

  @Setter
  @Getter
  private int score;
  @Getter
  private Config config;
  @Getter
  @Setter
  private Map<Integer, Entrance> closedEntrances;
  @Getter
  private List<Level> levels;
  @Getter
  @Setter
  private boolean paused = false;

  public Game(Config config) {
    score = 0;
    this.config = config;
    closedEntrances = new HashMap<>();
    levels = new ArrayList<>();
  }

  public Game(Config config, List<Level> levels) {
    score = 0;
    this.config = config;
    closedEntrances = getClosedEntrances(levels);
    this.levels = levels;
  }

  public void addScore() {
    setScore(getScore() + 10);
    tryToOpenEntrance();
  }

  public void tryToOpenEntrance() {
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
}
