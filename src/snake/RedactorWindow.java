package snake;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static javax.swing.GroupLayout.Alignment.BASELINE;

public class RedactorWindow extends JFrame {

    private GroupLayout layout;
    private JButton buttonGame;
    private JButton buttonSave;
    private LevelRedactor redactor;

    public RedactorWindow(){
        setTitle("Snake: redactor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Config config = new Config(25, 25, 25, 250);
        setSize(config.getWindowWidth(),config.getWindowHeight());
        setLocation(100,100);
        redactor = new LevelRedactor(config);
        redactor.setVisible(true);

        layout = new GroupLayout(getContentPane());

        setBackground(config.getBackgroundColor());
        getContentPane().setBackground(config.getBackgroundColor());

        setUpButtons(config.getBackgroundColor(),
                config.getButtonBordColor(),
                config.getTextColor(),
                config.getFont());
        setRedactorLayout();
        //pack();
    }

    private void setUpButtons(Color background, Color buttonBorderColor,
                              Color TextColor, Font font){
        buttonGame = new JButton(" Start with this maze ");
        buttonSave = new JButton(" Save this level ");

        buttonGame.setFont(font);
        buttonSave.setFont(font);

        buttonGame.setBackground(background);
        buttonGame.setForeground(TextColor);
        buttonSave.setBackground(background);
        buttonSave.setForeground(TextColor);

        LineBorder lb = new LineBorder(buttonBorderColor);

        buttonGame.setBorder(lb);
        buttonSave.setBorder(lb);

        buttonGame.addActionListener(evt -> startActionPerformed(evt));
        buttonSave.addActionListener(evt -> {
            try {
                saveActionPerformed(evt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setRedactorLayout(){
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(redactor)
                        .addGroup(layout.createParallelGroup()
                            .addComponent(buttonGame)
                            .addComponent(buttonSave)))
                );

        layout.linkSize(SwingConstants.HORIZONTAL, buttonSave, buttonGame);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(redactor)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonGame)
                                .addComponent(buttonSave))
                )
        );
    }

    private void startActionPerformed(ActionEvent evt) {
        this.dispose();
        Config config = new Config(25, 25, 25, 250);
        Level level = redactor.getLevel();

        new NewGame(config, level).setVisible(true);
        // Здесь надо вытащить уровень с redactor (поле этого же класса)
        // а потом запихнуть его в GameField
        //new GameField(config, ).setVisible(true);
    }

    private void saveActionPerformed(ActionEvent evt) throws IOException {
        Config config = new Config(25, 25, 25, 250);
        Game game = new Game();
        Level level = redactor.getLevel();
        game.serialize(config.getFieldWidth(), config.getFieldHeight(), level);
        // Здесь надо вытащить уровень с redactor (поле этого же класса)
        // и сохранить
        // можно потом снова вызвать окно меню
        this.dispose();
        new Menu().setVisible(true);

    }
}
