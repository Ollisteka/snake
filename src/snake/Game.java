package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class Game extends JFrame{

    private JButton buttonRandom;
    private JButton buttonRedactor;
    private JButton buttonOpen;

    public Game() {
        setTitle("Snake");
    }

    public static void main(String[] args) {
        new Menu().setVisible(true);
    }


    public static void serialize(Level maze, String levelName) throws IOException {
        FileOutputStream fos = new FileOutputStream(levelName + ".out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(maze);  // serializatin
        oos.flush();
        oos.close();
    }

    public static Level deserialize(String levelName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(levelName + ".out");
        ObjectInputStream oin = new ObjectInputStream(fis);
        Level maze = (Level) oin.readObject();
        return maze;
    }
}
