package main;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // dat dau "x"
		window.setResizable(false);// ko the resize
		window.setTitle("2D Adventure"); // dat ten
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		window.pack();
		
		window.setLocationRelativeTo(null); // se hien thi o dau, null la center
		window.setVisible(true); // co the xem
		
		
		gamePanel.startGameThread();

	}

}
