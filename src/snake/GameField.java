package snake;

import javafx.util.Pair;

import java.util.HashSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

public class GameField extends JPanel implements ActionListener, Serializable{
    private int WIDTH;
    private int HEIGHT;
    private int PIXEL;
    private Image foodIm;
    private Image snakeIm;
    private Image gameOver;
    private Image wallIm; //
    private Timer timer;
    private Food food;
    private boolean isPause = false;
    private Point[] snakeLocations;
    private Snake snake;
    private HashSet<Wall> walls;

    public void setSnakeLocations(Point[] locations){
        snakeLocations = locations;
        while (snake.getLength() < snakeLocations.length)
            snake.addLength();
    }
    public Point[] getSnakeLocations(){
        return snakeLocations;
    }

    public GameField(Config config){
        WIDTH = config.getFieldWidth();
        HEIGHT = config.getFieldHeight();
        PIXEL = config.getPixelSize();
        timer = new Timer(config.getTimerTick(),this);
        setBackground(Color.black);
        initGame();
        loadImages();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        Level level = new Level(config, "1"); //
        walls = level.createRandomField(); //
    }


    private void initGame(){
        snake = new Snake();
        food = new Food(WIDTH, HEIGHT);
        timer.start();
        snakeLocations = new Point[HEIGHT * WIDTH];
        snakeLocations[0] = new Point(1, 0);
        snakeLocations[1] = new Point(0, 0);
    }


    public void moveSnake() {
        for (int i = snake.getLength() - 1; i > 0; i--) {
            snakeLocations[i].x = snakeLocations[i - 1].x;
            snakeLocations[i].y = snakeLocations[i - 1].y;
        }
        if (snake.looksRight())
            snakeLocations[0].x++;
        if (snake.looksLeft())
            snakeLocations[0].x--;
        if (snake.looksUp())
            snakeLocations[0].y--;
        if (snake.looksDown())
            snakeLocations[0].y++;
    }


    private void loadImages(){
        ImageIcon f = new ImageIcon("food.png");
        foodIm = f.getImage();
        ImageIcon s = new ImageIcon("snake.png");
        snakeIm = s.getImage();
        ImageIcon g = new ImageIcon("gameOver.jpg");
        gameOver = g.getImage();
        ImageIcon p = new ImageIcon("bush.jpg"); //
        wallIm = p.getImage(); //
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!isSnakeDead()){
            Point location = food.getLocation();
            g.drawImage(foodIm,location.x * PIXEL, location.y * PIXEL, this);
            for (int i = 0; i < snake.getLength(); i++) {
                g.drawImage(snakeIm, snakeLocations[i].x * PIXEL, snakeLocations[i].y * PIXEL, this);
            }
            for(Wall wall : walls) {
                g.drawImage(wallIm,wall.getLocation().x * PIXEL, wall.getLocation().y * PIXEL,this);
            }
        } else{
            g.drawImage(gameOver, WIDTH/2*PIXEL-420,HEIGHT/2*PIXEL-240, this);
            timer.stop();
        }
        g.setColor(Color.green);
        g.drawLine(0, 0, WIDTH*PIXEL, 0);
        g.drawLine(0, 0, 0, HEIGHT*PIXEL);
        g.drawLine(WIDTH*PIXEL, 0, WIDTH*PIXEL, HEIGHT*PIXEL);
        g.drawLine(0, HEIGHT*PIXEL, WIDTH*PIXEL, HEIGHT*PIXEL);
        g.setColor(Color.cyan);
        g.drawString("Score:", WIDTH*PIXEL + 100, 100);
        g.drawString(Integer.toString((snake.getLength() - 1) * 10), WIDTH*PIXEL + 100, 150);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        moveSnake();
        tryEatFood();
        repaint();
    }

    private void tryEatFood() {
        if (snakeLocations[0].x == food.getLocation().x && snakeLocations[0].y == food.getLocation().y)
        {
            snake.eatFood();
            int i = snake.getLength()-1;
            snakeLocations[i] = new Point(snakeLocations[i-1].x, snakeLocations[i-1].y);
            food.createFood(WIDTH, HEIGHT);
            int count = 0;
            while (count < walls.size()) {
                for (Wall wall : walls) {
                    if (wall.getLocation().x == food.getLocation().x &&
                            wall.getLocation().y == food.getLocation().y) {
                        food.createFood(WIDTH, HEIGHT);
                        count = 0;
                    }
                    count++;
                }
            }
        }
    }

    public boolean isSnakeDead() {
        for (int j = 2; j < snake.getLength(); j++) {
            if (snakeLocations[0].x == snakeLocations[j].x &&
                    snakeLocations[0].y == snakeLocations[j].y) {
                snake.die();
                return true;
            }
        }
        if (snakeLocations[0].x < 0 ||
                snakeLocations[0].y < 0 ||
                snakeLocations[0].x >= WIDTH ||
                snakeLocations[0].y >= HEIGHT) {
            snake.die();
            return true;
        }
        for (Wall wall : walls) {
            if (snakeLocations[0].x == wall.getLocation().x &&
                    snakeLocations[0].y == wall.getLocation().y) {
                snake.die();
                return true;
            }
        }
        return false;
    }

    public class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT){
                snake.moveLeft();
            }
            if(key == KeyEvent.VK_UP) {
                snake.moveUp();
            }
            if(key == KeyEvent.VK_RIGHT){
                snake.moveRight();
            }
            if(key == KeyEvent.VK_DOWN){
                snake.moveDown();
            }
            if(key == KeyEvent.VK_SPACE){
                if (isPause){
                    isPause = false;
                    timer.start();
                }else{
                    isPause = true;
                    timer.stop();
                }
            }
        }
    }
}
