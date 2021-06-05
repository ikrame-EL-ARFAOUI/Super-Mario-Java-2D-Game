package com.TETOSOFT.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameOver {

    private static final int BUTTON_WIDTH = 10;
    private static final int BUTTON_HEIGHT = 10;
    public ScreenManager sc;
    public Rectangle exitButton,rejouerButton;
     
     public void draw(Graphics2D g,int screenWidth, int screnHeight) {
     	
     	Font titleFont = new Font("arial",Font.ITALIC,50);
     	Font buttonFont = new Font("arial",Font.ITALIC,20);
     	
     	exitButton = new Rectangle(screenWidth / 2 - 90 ,200,200,50);
     	rejouerButton = new Rectangle(screenWidth / 2 - 90 ,300,200,50);

     	g.setFont(titleFont);
     	g.setColor(Color.white);
     	g.drawString("GameOver",screenWidth / 2 - 120,120);
     	
     	g.setFont(buttonFont);
     	g.setColor(Color.GREEN);
     	
     	g.drawString("Exit", exitButton.x+70,exitButton.y+35);
     	
     	g.drawString("Rejouer", rejouerButton.x+60,rejouerButton.y+35);
     	
     	g.draw(rejouerButton);
     	g.draw(exitButton);

     }


}
