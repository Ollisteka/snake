package snake;

import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.HashSet;


public class LevelRedactor extends JPanel implements Serializable{
    private int WIDTH;
    private int HEIGHT;
    private int PIXEL;
    private Point location;
    private Image bush;
    private HashSet maze;

    public LevelRedactor(Config config){
        WIDTH = config.getFieldWidth();
        HEIGHT = config.getFieldHeight();
        PIXEL = config.getPixelSize();
        maze = new HashSet();
        location = new Point(0, 0);
        setBackground(Color.black);
        loadImages();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public void setLocation(int x, int y){
        location.x = x;
        location.y = y;
    }

    private void loadImages(){
        ImageIcon f = new ImageIcon("bush.jpg");
        bush = f.getImage();
    }

    private void addNewWall(){
        Wall newWall = new Wall(location.x, location.y);
        if (!maze.contains(newWall)) {
            maze.add(newWall);
        }
        else{
            maze.remove(newWall);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*
        Iterator iterator = maze.iterator();
        while (iterator.hasNext()){
            g.drawImage(bush,((Wall) iterator.next()).getLocation().x * PIXEL,
                    ((Wall) iterator.next()).getLocation().y * PIXEL,this);
        }*/
        Wall[] arr = (Wall[])maze.toArray(new Wall[maze.size()]);
        for (Wall wall : arr){
            g.drawImage(bush, wall.getLocation().x * PIXEL,
                    wall.getLocation().y * PIXEL,this);
        }
        g.setColor(Color.cyan);
        g.drawRect(location.x*PIXEL,location.y*PIXEL,PIXEL,PIXEL);
        g.setColor(Color.green);
        g.drawRect(0,0,WIDTH*PIXEL,HEIGHT*PIXEL);
    }

    public class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && getLocation().x > 0) {
                setLocation(--location.x, location.y);
            }
            if (key == KeyEvent.VK_RIGHT && getLocation().x < WIDTH) {
                setLocation(++location.x, location.y);
            }
            if (key == KeyEvent.VK_UP && getLocation().y > 0) {
                setLocation(location.x, --location.y);
            }
            if (key == KeyEvent.VK_DOWN && getLocation().y < HEIGHT) {
                setLocation(location.x, ++location.y);
            }
            if (key == KeyEvent.VK_SPACE){
                addNewWall();
            }
            if (key == KeyEvent.VK_ENTER){
                //Level lvl = new Level(config, "new Level");
                //Game.serialize(lvl, "new Level");
            }
            repaint();
        }
    }
}
