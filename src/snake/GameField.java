package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.HashSet;

public class GameField extends JPanel implements ActionListener, Serializable{
    private int WIDTH;
    private int HEIGHT;
    private int PIXEL;
    private Image foodIm;
    private Image snakeIm;
    private Image gameOver;
    private Image bush;
    private Timer timer;
    private int direction;
    private Food food;
    private Snake snake;
    private HashSet maze;
    private boolean inGame = true;
    private boolean isPause = false;


    public GameField(Config config){
        WIDTH = config.getFieldWidth();
        HEIGHT = config.getFieldHeight();
        PIXEL = config.getPixelSize();
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    private void initGame(){
        snake = new Snake();
        direction = KeyEvent.VK_RIGHT;
        food = new Food(WIDTH, HEIGHT);
        timer = new Timer(250,this);
        timer.start();
    }

    private void loadImages(){
        ImageIcon f = new ImageIcon("food.png");
        foodIm = f.getImage();
        ImageIcon s = new ImageIcon("snake.png");
        snakeIm = s.getImage();
        ImageIcon g = new ImageIcon("gameOver.jpg");
        gameOver = g.getImage();
        ImageIcon b = new ImageIcon("bush.jpg");
        bush = b.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!isDead()){
            Point location = food.getLocation();
            g.drawImage(foodIm,location.x * PIXEL, location.y * PIXEL, this);
            for (int i = 0; i < snake.getLength(); i++) {
                Point[] locations = snake.getLocations();
                g.drawImage(snakeIm,locations[i].x * PIXEL, locations[i].y * PIXEL,this);
            }
        } else{
            g.drawImage(gameOver, WIDTH/2*PIXEL-420,HEIGHT/2*PIXEL-240, this);
            timer.stop();
        }
        g.setColor(Color.green);
        g.drawRect(0,0,WIDTH*PIXEL,HEIGHT*PIXEL);
        g.setColor(Color.cyan);
        g.drawString("Score:", WIDTH*PIXEL + 100, 100);
        g.drawString(Integer.toString((snake.getLength() - 1) * 10), WIDTH*PIXEL + 100, 150);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (direction == KeyEvent.VK_DOWN)
            snake.turnDown();
        if (direction == KeyEvent.VK_UP)
            snake.turnUp();
        if (direction == KeyEvent.VK_RIGHT)
            snake.turnRight();
        if (direction == KeyEvent.VK_LEFT)
            snake.turnLeft();
        eatFood();
        repaint();
    }

    private void eatFood() {
        if (snake.getLocations()[0].x == food.getLocation().x && snake.getLocations()[0].y == food.getLocation().y)
        {
            snake.addLength();
            food.createFood(WIDTH, HEIGHT);
        }
    }

    private boolean isDead() {
        for (int j = 2; j < snake.getLength(); j++) {
            if (snake.getLocations()[0].x == snake.getLocations()[j].x &&
                    snake.getLocations()[0].y == snake.getLocations()[j].y)
                return true;
            if (snake.getLocations()[0].x < 0 ||
                    snake.getLocations()[0].y < 0 ||
                    snake.getLocations()[0].x >= WIDTH ||
                    snake.getLocations()[0].y >= HEIGHT)
                return true;
        }
        return false;
    }

    public class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT){
                direction = KeyEvent.VK_LEFT;
            }
            if(key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT){
                direction = KeyEvent.VK_RIGHT;
            }
            if(key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN){
                direction = KeyEvent.VK_UP;
            }
            if(key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP){
                direction = KeyEvent.VK_DOWN;
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