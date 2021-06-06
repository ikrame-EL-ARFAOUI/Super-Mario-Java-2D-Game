package com.TETOSOFT.graphics;


import java.awt.DisplayMode;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.TETOSOFT.input.GameAction;
import com.TETOSOFT.input.InputManager;
import com.TETOSOFT.input.inputTest;


import junit.framework.TestCase;

//This tests if the game over page's actions are working, pressingE to quit and R to replay

public class GameOverTest extends TestCase {
	public void testGameOverKeys() throws Exception {
	GameAction exitE = new GameAction("exitE");
	GameAction replay = new GameAction("replay");
	ScreenManager screenManager = new ScreenManager();
	screenManager.setFullScreen(new DisplayMode(800, 600, 32, 0));

	InputManager inputManager = new InputManager(screenManager.
	    getFullScreenWindow());

	inputManager.mapToKey(exitE, KeyEvent.VK_E);
	inputManager.mapToKey(replay, KeyEvent.VK_R);

	Robot robot = new Robot();

	assertTrue("Failed to detect action '" + exitE.getName() + "'",
	    keyPress(robot, KeyEvent.VK_E, exitE) == true);
	Thread.sleep(100);
	assertTrue("Failed to detect action '" + replay.getName() + "'",
		    keyPress(robot, KeyEvent.VK_R, replay) == true);


}
	private boolean keyPress(Robot robot, int keycode, GameAction action) throws InterruptedException {
        new Thread(() -> {
            robot.keyPress(keycode);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(inputTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            robot.keyRelease(keycode);
        }).start();
        Thread.sleep(100);
        return action.isPressed();
    }
}

