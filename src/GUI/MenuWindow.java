package GUI;

import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import logic.Config;
import logic.Game;
import logic.Level;

public class MenuWindow extends JFrame {

  private JButton buttonRandom;
  //TODO к этой кнопке - метод startNewStory
  private JButton buttonStory;
  private JButton buttonRedactor;
  private JButton buttonOpen;
  private GroupLayout layout;

  public MenuWindow() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocation(600, 400);
    setTitle("Snake: menu");

    buttonRandom = new JButton(" Create random level and play ");
    buttonRedactor = new JButton(" Create new level ");
    buttonOpen = new JButton(" Upload saved level ");
    layout = new GroupLayout(getContentPane());

    setUpButtons();
    setMenuLayout();

    setBackground(Color.BLACK);
    getContentPane().setBackground(Color.BLACK);

    buttonRandom.addActionListener(evt -> {
      try {
        startNewStory(evt);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    });
    buttonOpen.addActionListener(evt -> uploadActionPerformed(evt));
    buttonRedactor.addActionListener(evt -> createActionPerformed(evt));

    pack();
  }

  private void setUpButtons() {
    Font font = new Font("Comic Sans MS", Font.BOLD, 40);

    buttonRandom.setFont(font);
    buttonRedactor.setFont(font);
    buttonOpen.setFont(font);

    buttonRandom.setBackground(Color.BLACK);
    buttonRandom.setForeground(Color.GREEN);
    buttonRedactor.setBackground(Color.BLACK);
    buttonRedactor.setForeground(Color.GREEN);
    buttonOpen.setBackground(Color.BLACK);
    buttonOpen.setForeground(Color.GREEN);

    LineBorder lb = new LineBorder(Color.GREEN);

    buttonRandom.setBorder(lb);
    buttonRedactor.setBorder(lb);
    buttonOpen.setBorder(lb);
  }

  private void setMenuLayout() {

    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);

    layout.setHorizontalGroup(layout.createSequentialGroup()
        .addGroup(layout.createSequentialGroup()
            .addGap(5, 100, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(LEADING)
                .addComponent(buttonRandom)
                .addComponent(buttonRedactor)
                .addComponent(buttonOpen))
            .addGap(5, 100, Short.MAX_VALUE)
        ));

    layout.linkSize(SwingConstants.HORIZONTAL, buttonOpen, buttonRandom, buttonRedactor);

    layout.setVerticalGroup(layout.createSequentialGroup()
        .addGap(5, 50, Short.MAX_VALUE)
        .addComponent(buttonRandom)
        .addComponent(buttonRedactor)
        .addComponent(buttonOpen)
        .addGap(5, 50, Short.MAX_VALUE)
    );
  }

  private void createActionPerformed(ActionEvent evt) {
    this.dispose();
    new LevelEditorWindow().setVisible(true);
  }

  private void uploadActionPerformed(ActionEvent evt) {
    Config config = new Config(25, 25, 25, 250);
    try {
      Level level = Game.deserialize();
      new GameWindow(config, level).setVisible(true);
    } catch (IOException | ClassNotFoundException e) {
      new MenuWindow().setVisible(true);
    }
    this.dispose();
  }

  private void randomActionPerformed(ActionEvent evt) {
    Config config = new Config(25, 25, 25, 250);
    Level level = new Level(config, "Random");
    level.setMazeLocations(level.createRandomField());
    this.dispose();
    new GameWindow(config, level).setVisible(true);
  }

  private void startNewStory(ActionEvent evt) throws IOException, ClassNotFoundException {
    Config config = new Config(25, 25, 25, 250);
    ArrayList<Level> levels = new ArrayList<>();
    ArrayList<String> levelsNames = new ArrayList<>();
    levelsNames.add("Level_0.txt");
    levelsNames.add("Level_1.txt");
    levelsNames.add("Level_2.txt");
    levelsNames.add("Level_3.txt");

    for (String levelName : levelsNames) {
      levels.add(Game.deserialize(levelName));
    }
    this.dispose();
    new GameWindow(levels, config).setVisible(true);
  }
}
