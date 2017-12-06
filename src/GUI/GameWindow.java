package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
  private Game game;
  private Timer timer;

  public GameWindow(Config config, Level level, boolean twoPlayers) {
    initWindow(config);
    game = new Game(config, level, twoPlayers);
    currentGameField = new GameFieldPanel(game, level);
    add(currentGameField);
    setVisible(true);
  }


  public GameWindow(List<Level> levels, Config config, boolean twoPlayers) {
    initWindow(config);
    game = new Game(config, levels, twoPlayers);
    currentGameField = new GameFieldPanel(game, game.getCurrentLevel());
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
    Level lastLevel = game.getCurrentLevel();
    Level newLevel = game.getLevels().get(levelNumber);
    currentGameField = new GameFieldPanel(game, newLevel);
    currentGameField.handlePreviousSnake(lastLevel, game.getSnakes().get(0));
    add(currentGameField);
    game.setCurrentLevel(newLevel);
  }

  /**
   * Найти номер уровня, в котором лежит entrance, соответствующий тому, в который мы вошли
   *
   * @param inputEntrance вход, в который мы зашли
   * @return номер уровня, в который мы попадём
   */
  private int findNextLevel(Entrance inputEntrance) {
    for (int i = 0; i < game.getLevels().size(); i++) {
      Level level = game.getLevels().get(i);
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
    if (game.isPaused()) {
        timer.stop();
    }
    Entrance possibleEntrance = game.getCurrentLevel().canMoveToNextLevel(game.getSnakes().get(0));
    if (possibleEntrance != null) {
      currentGameField.setVisible(false);
      int nextLevel = findNextLevel(possibleEntrance);
      changeLevel(nextLevel);
      currentGameField.setVisible(true);
    }
    for (Snake snake : game.getSnakes()) {
      for (Level level : game.getLevels()) {
        game.moveSnake(level, snake);
      }
      game.tryToDie(snake);
      game.tryEatFood(game, snake);
    }
    repaint();
  }

  public class FieldKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      super.keyPressed(e);
      int key = e.getKeyCode();
      Snake snakeOne;
      Snake snakeTwo = null;
      List<Snake> snakesList = game.getSnakes();
      snakeOne = snakesList.get(0);
      if (snakesList.size() == 2) {
        snakeTwo = snakesList.get(1);
      }

      if (snakeOne.isAlive() && key == KeyEvent.VK_LEFT) {
        snakeOne.moveLeft();
      }
      if (snakeOne.isAlive() && key == KeyEvent.VK_UP) {
        snakeOne.moveUp();
      }
      if (snakeOne.isAlive() && key == KeyEvent.VK_RIGHT) {
        snakeOne.moveRight();
      }
      if (snakeOne.isAlive() && key == KeyEvent.VK_DOWN) {
        snakeOne.moveDown();
      }

      if (key == KeyEvent.VK_SPACE) {
        if (game.isPaused()) {
          game.setPaused(false);
          timer.start();
        } else {
          game.setPaused(true);
          timer.stop();
        }
      }
      if (key == KeyEvent.VK_M) {
        new MenuWindow().setVisible(true);
        dispose();
        return;
      }
      if (snakeTwo != null) {
        if (snakeTwo.isAlive() && key == KeyEvent.VK_A) {
          snakeTwo.moveLeft();
        }
        if (snakeTwo.isAlive() && key == KeyEvent.VK_W) {
          snakeTwo.moveUp();
        }
        if (snakeTwo.isAlive() && key == KeyEvent.VK_D) {
          snakeTwo.moveRight();
        }
        if (snakeTwo.isAlive() && key == KeyEvent.VK_S) {
          snakeTwo.moveDown();
        }
      }
    }
  }
}

