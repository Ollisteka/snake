package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import logic.Config;
import logic.Level;

public class GameWindow extends JFrame implements ActionListener {

  private GameFieldPanel gamefield;
  private ArrayList<Level> levels;
  private Config baseConfiguration;
  private Timer timer;

  public GameWindow(Config config, Level level) {
    setTitle("Snake");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(config.getWindowWidth(), config.getWindowHeight());
    setLocation(100, 100);
    gamefield = new GameFieldPanel(config, level, this);
    add(gamefield);
    setVisible(true);
    addKeyListener(new FieldKeyListener());
    timer = new Timer(config.getTimerTick(), this);
    timer.start();
    setFocusable(true);
  }

  public GameWindow(ArrayList<Level> levels, Config baseConfiguration) {
    this.levels = levels;
    this.baseConfiguration = baseConfiguration;
    setTitle("Snake");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(baseConfiguration.getWindowWidth(), baseConfiguration.getWindowHeight());
    setLocation(100, 100);
    gamefield = new GameFieldPanel(baseConfiguration, this.levels.get(0), this);
    add(gamefield);
    setVisible(true);
    timer = new Timer(baseConfiguration.getTimerTick(), this);
    addKeyListener(new FieldKeyListener());
    timer.start();
    setFocusable(true);
  }

  public void changeLevel(int levelNumber) {
    gamefield = new GameFieldPanel(baseConfiguration, levels.get(levelNumber), this);
    add(gamefield);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    gamefield.moveSnake();
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
      if (key == KeyEvent.VK_1) {
        gamefield.setVisible(false);
        changeLevel(1);
      }
      if (key == KeyEvent.VK_2) {
        gamefield.setVisible(false);
        changeLevel(2);
      }
      if (key == KeyEvent.VK_3) {
        gamefield.setVisible(false);
        changeLevel(3);
      }
      if (key == KeyEvent.VK_0) {
        gamefield.setVisible(false);
        changeLevel(0);
      }
      if (key == KeyEvent.VK_M) {
        new MenuWindow().setVisible(true);
        dispose();
      }
    }
  }
}
