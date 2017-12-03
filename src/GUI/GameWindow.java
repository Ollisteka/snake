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

public class GameWindow extends JFrame implements ActionListener {

  private GameFieldPanel gamefield;
  private List<Level> levels;
  private List<GameFieldPanel> gamefields = new ArrayList<GameFieldPanel>();
  private Config baseConfiguration;
  private Timer timer;
  private Game game;

  public GameWindow(Config config, Level level) {
    initWindow(config);
    game = new Game(config);
    gamefield = new GameFieldPanel(game, level);
    add(gamefield);
    setVisible(true);
  }

  public GameWindow(List<Level> levels, Config config) {
    this.levels = levels;
    this.baseConfiguration = config;
    game = new Game(config, levels);
    initWindow(baseConfiguration);

    for (Level level : this.levels) {
      gamefields.add(new GameFieldPanel(game, level));
    }
    gamefield = gamefields.get(0);
    add(gamefield);
    //gamefield = new GameFieldPanel(game, this.levels.get(0));
    //add(gamefield);
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

  public void changeLevel(int levelNumber) {
    //gamefield = new GameFieldPanel(game, levels.get(levelNumber), gamefield);
    int lastGameField = gamefields.indexOf(gamefield);
    gamefield = gamefields.get(levelNumber);
    gamefield.handlePreviousSnake(gamefields.get(lastGameField));
    add(gamefield);
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
      if (level == gamefield.getLevel()) {
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
    Entrance possibleEntrance = gamefield.canMoveToNextLevel();
    if (possibleEntrance != null) {
      gamefield.setVisible(false);
      int nextLevel = findNextLevel(possibleEntrance);
      changeLevel(nextLevel);
      gamefield.setVisible(true);
    }
    for (GameFieldPanel fieldPanel : this.gamefields) {
      fieldPanel.moveSnake();
    }
    gamefield.tryEatFood();
    repaint();
  }

  public class FieldKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      super.keyPressed(e);
      int key = e.getKeyCode();
      if (key == KeyEvent.VK_LEFT) {
        gamefield.getSnake().moveLeft();
      }
      if (key == KeyEvent.VK_UP) {
        gamefield.getSnake().moveUp();
      }
      if (key == KeyEvent.VK_RIGHT) {
        gamefield.getSnake().moveRight();
      }
      if (key == KeyEvent.VK_DOWN) {
        gamefield.getSnake().moveDown();
      }
      if (key == KeyEvent.VK_SPACE) {
        if (gamefield.isPause()) {
          gamefield.setPause(false);
          timer.start();
        } else {
          gamefield.setPause(true);
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
