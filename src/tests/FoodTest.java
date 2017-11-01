package tests;

import org.junit.Test;
import org.junit.Assert;
import snake.Config;
import snake.Food;

public class FoodTest extends Assert{

    @Test
    public void foodIntoTheField(){
        Config config = new Config(25, 25, 25, 250);
        Food food = new Food(config.getFieldWidth(), config.getFieldHeight());
        assertTrue(food.getLocation().x >= 0);
        assertTrue(food.getLocation().y >= 0);
        assertTrue(food.getLocation().x < config.getFieldWidth());
        assertTrue(food.getLocation().y < config.getFieldHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void foodWithIllegalBounds(){
        Config config = new Config(-1, -1, 25, 250);
        Food food = new Food(config.getFieldWidth(), config.getFieldHeight());
    }

    @Test
    public void foodOutOfTheField(){
        Config config = new Config(5, 5, 25, 10000);
        Food food = new Food(100, 100);
        while (food.getLocation().x < config.getFieldWidth()
                && food.getLocation().y < config.getFieldHeight()){
            food = new Food(100, 100);
        }
        assertTrue(food.getLocation().x > config.getFieldWidth()
                || food.getLocation().y > config.getFieldHeight());
    }

}