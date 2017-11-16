package GUI;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import logic.Config;
import logic.Level;

public class GameWindow extends JFrame {

  public GameWindow(Config config, Level level) {
    setTitle("Snake");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(config.getWindowWidth(), config.getWindowHeight());
    setLocation(100, 100);
    GameFieldPanel field = new GameFieldPanel(config, level, this);
    add(field);
    setVisible(true);
  }
}
