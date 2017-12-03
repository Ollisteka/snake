package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import logic.Config;
import logic.Entrance;
import logic.Game;
import logic.Level;
import logic.Snake;

public class GameWindow extends JFrame implements ActionListener {

  private GameFieldPanel currentGameField;
  private Level currentLevel;
  private Game game;
  private List<Level> levels;
  private Timer timer;

  public GameWindow(Config config, Level level) {
    initWindow(config);
    game = new Game(config);
    this.levels = new ArrayList<>();
    levels.add(level);
    currentLevel = level;
    currentGameField = new GameFieldPanel(game, level);
    add(currentGameField);
    setVisible(true);
  }

  public GameWindow(List<Level> levels, Config config) {
    initWindow(config);
    game = new Game(config, levels);
    this.levels = levels;
    currentLevel = levels.get(0);
    currentGameField = new GameFieldPanel(game, currentLevel);
    add(currentGameField);
    setVisible(true);
  }

  private void initWindow(Config config) {
    setTitle("Snake");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(config.getWindowWidth(), config.getWindowHeight());
    setLocation(100, 100);

    timer = new Timer(config.getTimerTick(), this);
    timer.start();

    addKeyListener(new FieldKeyListener());
    setFocusable(true);
  }

  /**
   * Сменить текущую панель на новую, правильно разместив змейку
   */
  private void changeLevel(int levelNumber) {
    Level lastLevel = currentGameField.getLevel();
    Level newLevel = levels.get(levelNumber);
    currentGameField = new GameFieldPanel(game, newLevel);
    currentGameField.handlePreviousSnake(lastLevel);
    add(currentGameField);
    currentLevel = newLevel;
  }

  /**
   * Найти номер уровня, в котором лежит entrance, соответствующий тому, в который мы вошли
   *
   * @param inputEntrance вход, в который мы зашли
   * @return номер уровня, в который мы попадём
   */
  private int findNextLevel(Entrance inputEntrance) {
    for (int i = 0; i < levels.size(); i++) {
      Level level = levels.get(i);
      if (level == currentGameField.getLevel()) {
        continue;
      }
      Set<Entrance> openedEntrances = level.findOpenEntrances();
      for (Entrance openedEntry : openedEntrances) {
        if (openedEntry.getName() == inputEntrance.getName()) {
          return i;
        }
      }

    }
    return -1;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Entrance possibleEntrance = currentLevel.canMoveToNextLevel();
    if (possibleEntrance != null) {
      currentGameField.setVisible(false);
      int nextLevel = findNextLevel(possibleEntrance);
      changeLevel(nextLevel);
      currentGameField.setVisible(true);
    }
    for (Level level : this.levels) {
      level.moveSnake();
    }
    currentLevel.tryToDie();
    currentLevel.tryEatFood(game);
    repaint();
  }

  public class FieldKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      super.keyPressed(e);
      int key = e.getKeyCode();
      Snake snake = currentLevel.getSnake();

      if (snake.isAlive() && key == KeyEvent.VK_LEFT) {
        snake.moveLeft();
      }
      if (snake.isAlive() && key == KeyEvent.VK_UP) {
        snake.moveUp();
      }
      if (snake.isAlive() && key == KeyEvent.VK_RIGHT) {
        snake.moveRight();
      }
      if (snake.isAlive() && key == KeyEvent.VK_DOWN) {
        snake.moveDown();
      }

      if (key == KeyEvent.VK_SPACE) {
        if (currentGameField.isPause()) {
          currentGameField.setPause(false);
          timer.start();
        } else {
          currentGameField.setPause(true);
          timer.stop();
        }
      }
      if (key == KeyEvent.VK_M) {
        new MenuWindow().setVisible(true);
        dispose();
      }
    }
  }
}
