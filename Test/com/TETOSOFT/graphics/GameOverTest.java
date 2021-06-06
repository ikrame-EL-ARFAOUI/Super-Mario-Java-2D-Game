package com.TETOSOFT.graphics;

import static org.junit.Assert.*;

import org.junit.Test;


import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Window;

import org.junit.jupiter.api.BeforeAll;

import com.TETOSOFT.graphics.ScreenManager;
import com.TETOSOFT.graphics.GameOver;

public class GameOverTest {

    private static GameOver endmenu;
    private static ScreenManager screen;



    @BeforeAll
    static void beforeAll() {
        screen = new ScreenManager();
        DisplayMode POSSIBLE_MODES[] = { new DisplayMode(800, 600, 32, 0),
                    new DisplayMode(800, 600, 16, 0), new DisplayMode(800, 600, 24, 0), new DisplayMode(640, 480, 16, 0),
                    new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 24, 0), new DisplayMode(1024, 768, 16, 0),
                    new DisplayMode(1024, 768, 32, 0), new DisplayMode(1024, 768, 24, 0), };

        DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);
        screen.setFullScreen(displayMode);

        Window window = screen.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN, 18));
        window.setBackground(Color.BLACK);
        window.setForeground(Color.WHITE);

        endmenu = new GameOver (5,screen);
    }

    @Test
    void testExit() {

        new Thread(
                ()->{try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    endmenu.getExit().press();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    assertNull(screen);

                     }
        ).start();

        endmenu.run();

    }


}






















/*public class GameOverTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}*/
