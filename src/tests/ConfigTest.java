package tests;

import logic.Config;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class ConfigTest {

    @Test
    public void configTest() {
        Config config = new Config(25, 20, 30, 250);
        Assert.assertEquals(config.getFieldWidth(), 25);
        Assert.assertEquals(config.getFieldHeight(), 20);
        Assert.assertEquals(config.getPixelSize(), 30);
        Assert.assertEquals(config.getWindowWidth(), 25 * 30 + 285);
        Assert.assertEquals(config.getWindowHeight(), 20 * 30 + 85);
        Assert.assertEquals(config.getTimerTick(), 250);
        Assert.assertEquals(config.getTextColor(), Color.GREEN);
        Assert.assertEquals(config.getBackgroundColor(), Color.BLACK);
        Assert.assertEquals(config.getButtonBordColor(), Color.GREEN);
    }
}
