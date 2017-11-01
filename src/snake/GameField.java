package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

public class GameField extends JPanel implements ActionListener, Serializable{
    private int width;
    private int height;
    private int pixel;
    private Image foodIm;
    private Image snakeIm;
    private Image gameOver;
    private Image wallIm;
    private Image headIm;
    private Timer timer;
    private Food food;
    private boolean isPause = false;
    private Point[] snakeLocations;
    private Snake snake;
    private Level level;

    public void setSnakeLocations(Point[] locations){
        snakeLocations = locations;
        while (snake.getLength() < snakeLocations.length)
            snake.addLength();
    }
    public Point[] getSnakeLocations(){
        return snakeLocations;
    }

    public GameField(Config config, Level currentLevel){
        width = config.getFieldWidth();
        height = config.getFieldHeight();
        pixel = config.getPixelSize();
        timer = new Timer(config.getTimerTick(),this);
        setBackground(config.getBackgroundColor());
        initGame();
        loadImages();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        level = currentLevel;
    }


    private void initGame(){
        snake = new Snake();
        food = new Food(width, height);
        timer.start();
        snakeLocations = new Point[height * width];
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
        ImageIcon p = new ImageIcon("bush.jpg");
        wallIm = p.getImage();
        ImageIcon q = new ImageIcon("head.png");
        headIm = q.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!isSnakeDead()){
            Point location = food.getLocation();
            g.drawImage(foodIm,location.x * pixel, location.y * pixel, this);
            g.drawImage(headIm, snakeLocations[0].x * pixel, snakeLocations[0].y * pixel, this);
            for (int i = 1; i < snake.getLength(); i++) {
                g.drawImage(snakeIm, snakeLocations[i].x * pixel, snakeLocations[i].y * pixel, this);
            }
            for(Wall wall : level.getMazeLocations()) {
                g.drawImage(wallIm,wall.getLocation().x * pixel, wall.getLocation().y * pixel,this);
            }
        } else{
            g.drawImage(gameOver, width /2* pixel -420, height /2* pixel -240, this);
            timer.stop();
        }
        g.setColor(Color.green);
        g.drawLine(0, 0, width * pixel, 0);
        g.drawLine(0, 0, 0, height * pixel);
        g.drawLine(width * pixel, 0, width * pixel, height * pixel);
        g.drawLine(0, height * pixel, width * pixel, height * pixel);
        g.setColor(Color.cyan);
        g.drawString("Score:", width * pixel + 100, 100);
        g.drawString(Integer.toString((snake.getLength() - 1) * 10), width * pixel + 100, 150);

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
            food.createFood(width, height);
            int count = 0;
            while (count < level.getMazeLocations().size()) {
                for (Wall wall : level.getMazeLocations()) {
                    if (wall.getLocation().x == food.getLocation().x &&
                            wall.getLocation().y == food.getLocation().y) {
                        food.createFood(width, height);
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
                snakeLocations[0].x >= width ||
                snakeLocations[0].y >= height) {
            snake.die();
            return true;
        }
        for (Wall wall : level.getMazeLocations()) {
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
