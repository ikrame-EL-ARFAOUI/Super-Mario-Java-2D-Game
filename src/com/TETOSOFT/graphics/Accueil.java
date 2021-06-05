package com.TETOSOFT.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Accueil extends JFrame implements ActionListener {

	  private JPanel pan = new JPanel();
	  private JButton bouton = new JButton("START !");
	  private JLabel label = new JLabel("Super Mario");
	  public  Accueil()throws IOException {
		    this.setTitle("Welcome");
		    this.setSize(400, 300);
		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    this.setLocationRelativeTo(null);
		    
		    bouton.addActionListener(this);  

		  	pan.setBackground(Color.red);
		    pan.setLayout(new BorderLayout());
		    pan.add(bouton, BorderLayout.SOUTH);
		   
		 
		   Font police = new Font("Tahoma", Font.BOLD, 16);
		   label.setFont(police);
		   label.setForeground(Color.black);
		   label.setHorizontalAlignment(JLabel.CENTER);
		   pan.add(label, BorderLayout.NORTH);
		   this.setContentPane(pan);
		  }
	  
	public void actionPerformed(ActionEvent arg0) {
		this.setVisible(false);
	}       
	
	}

	

