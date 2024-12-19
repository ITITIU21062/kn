import java.util.*;
import java.awt.event.*;

import javax.swing.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.Timer;

public class Gameplay  extends JPanel implements ActionListener 
{
	private brick br;
	
	private ImageIcon player1;	
	private int player1X = 200;    //tọa độ của p1
	private int player1Y = 550;	
	private boolean player1right = false;     // status của p1 (quay trái, quay phải, ...)
	private boolean player1left = false;
	private boolean player1down = false;
	private boolean player1up = true;	
	private int player1score = 0;          // điểm của p1
	private int player1lives = 5;          // hp của p1
	private boolean player1Shoot = false;  // có lệnh bắn hay ko
	private String bulletShootDir1 = "";   
	
	private ImageIcon player2;	
	private int player2X = 400;
	private int player2Y = 550;	
	private boolean player2right = false;
	private boolean player2left = false;
	private boolean player2down = false;
	private boolean player2up = true;
	private int player2score = 0;
	private int player2lives = 5;
	private boolean player2Shoot = false;
	private String bulletShootDir2 = "";
	
	private Timer timer;
	private int delay=8;
	
	private Player1Listener player1Listener;
	private Player2Listener player2Listener;
	
	private Player1Bullet player1Bullet = null;
	private Player2Bullet player2Bullet = null;
	
	private boolean play = true;
	
	public Gameplay()
	{				
		br = new brick();
		player1Listener = new Player1Listener();
		player2Listener = new Player2Listener();
		setFocusable(true);
		//addKeyListener(this);
		addKeyListener(player1Listener);
		addKeyListener(player2Listener);
		setFocusTraversalKeysEnabled(false);
        timer=new Timer(delay,this);
		timer.start();
	}
	
	public void paint(Graphics g)
	{    		
		// play background
		g.setColor(Color.black);        // Bản đồ trò chơi (nền đen)
		g.fillRect(0, 0, 650, 600);
		
		// right side background
		g.setColor(Color.DARK_GRAY);   // Khu vực bên phải hiển thị điểm và kills
		g.fillRect(660, 0, 140, 600);
		
		// draw solid bricks
		br.drawSolids(this, g);        // Vẽ các viên gạch không phá được
		
		// draw Breakable bricks	
		br.draw(this, g);              // Vẽ các viên gạch có thể phá
		
		if(play)
		{
			// draw player 1
			if(player1up)
				player1=new ImageIcon("player1_tank_up.png");	   // Nếu p1 bấm up thì vẽ ảnh "up"
			else if(player1down)
				player1=new ImageIcon("player1_tank_down.png");
			else if(player1right)
				player1=new ImageIcon("player1_tank_right.png");
			else if(player1left)
				player1=new ImageIcon("player1_tank_left.png");
				
			player1.paintIcon(this, g, player1X, player1Y);
			
			// draw player 2
			if(player2up)
				player2=new ImageIcon("player2_tank_up.png");	
			else if(player2down)
				player2=new ImageIcon("player2_tank_down.png");
			else if(player2right)
				player2=new ImageIcon("player2_tank_right.png");
			else if(player2left)
				player2=new ImageIcon("player2_tank_left.png");
						
			player2.paintIcon(this, g, player2X, player2Y);
			
			if(player1Bullet != null && player1Shoot)           // Nếu không có viên đạn nào của p1 trên map và p1 có lệnh bắn thì:
			{
				if(bulletShootDir1.equals(""))              // xét trạng thái p1, nếu p1 "up" thì đạn bắn "up"
				{
					if(player1up)
					{					
						bulletShootDir1 = "up";
					}
					else if(player1down)
					{					
						bulletShootDir1 = "down";
					}
					else if(player1right)
					{				
						bulletShootDir1 = "right";
					}
					else if(player1left)
					{			
						bulletShootDir1 = "left";
					}
				}
				else
				{
					player1Bullet.move(bulletShootDir1);
					player1Bullet.draw(g);
				}
				
				
				if(new Rectangle(player1Bullet.getX(), player1Bullet.getY(), 10, 10)   // nếu đạn của p1 đấm trúng p2 thì:
				.intersects(new Rectangle(player2X, player2Y, 50, 50)))
				{
					player1score += 10;        // điểm p1 +10
					player2lives -= 1;         // hp p2 -1
					player1Bullet = null;      // đặt số đạn về null
					player1Shoot = false;      // đặt lệnh bắn về false
					bulletShootDir1 = ""; 
				}
				
				if(br.checkCollision(player1Bullet.getX(), player1Bullet.getY())
						|| br.checkSolidCollision(player1Bullet.getX(), player1Bullet.getY()))     // nếu như đạn đâm cục gạch ko bể đc
				{
					player1Bullet = null;      // đặt số đạn về null  
					player1Shoot = false;      // đặt lệnh bắn về false
					bulletShootDir1 = "";				
				}
	
				if(player1Bullet.getY() < 1                                 // nếu đạn ra ngoài màn hình:
						|| player1Bullet.getY() > 580
						|| player1Bullet.getX() < 1
						|| player1Bullet.getX() > 630)
				{
					player1Bullet = null;      // đặt số đạn về null 
					player1Shoot = false;      // đặt lệnh bắn về false
					bulletShootDir1 = "";
				}
			}
			
			if(player2Bullet != null && player2Shoot)    //lệnh bắn của p2. i chang p1
			{
				if(bulletShootDir2.equals(""))
				{
					if(player2up)
					{					
						bulletShootDir2 = "up";
					}
					else if(player2down)
					{					
						bulletShootDir2 = "down";
					}
					else if(player2right)
					{				
						bulletShootDir2 = "right";
					}
					else if(player2left)
					{			
						bulletShootDir2 = "left";
					}
				}
				else
				{
					player2Bullet.move(bulletShootDir2);
					player2Bullet.draw(g);
				}
				
				
				if(new Rectangle(player2Bullet.getX(), player2Bullet.getY(), 10, 10)
				.intersects(new Rectangle(player1X, player1Y, 50, 50)))
				{
					player2score += 10;
					player1lives -= 1;
					player2Bullet = null;
					player2Shoot = false;
					bulletShootDir2 = "";
				}
				
				if(br.checkCollision(player2Bullet.getX(), player2Bullet.getY())
						|| br.checkSolidCollision(player2Bullet.getX(), player2Bullet.getY()))
				{
					player2Bullet = null;
					player2Shoot = false;
					bulletShootDir2 = "";				
				}
				
				if(player2Bullet.getY() < 1 
						|| player2Bullet.getY() > 580
						|| player2Bullet.getX() < 1
						|| player2Bullet.getX() > 630)
				{
					player2Bullet = null;
					player2Shoot = false;
					bulletShootDir2 = "";
				}
			}
		}
	
		
		// the scores 		
		g.setColor(Color.white);                                // bảng điểm 
		g.setFont(new Font("serif",Font.BOLD, 15));             // dùng phông serif, định dạng in đậm, cỡ 15
		g.drawString("Scores", 700,30);
		g.drawString("Player 1:  "+player1score, 670,60);
		g.drawString("Player 2:  "+player2score, 670,90);
		
		g.drawString("Lives", 700,150);                         // bảng hp
		g.drawString("Player 1:  "+player1lives, 670,180);
		g.drawString("Player 2:  "+player2lives, 670,210);
		
		if(player1lives == 0)                                   // nếu như hp p1 =0
		{
			g.setColor(Color.white);
			g.setFont(new Font("serif",Font.BOLD, 60));
			g.drawString("Game Over", 200,300);             // in game over
			g.drawString("Player 2 Won", 180,380);          // in p2 thắng
			play = false;
			g.setColor(Color.white);
			g.setFont(new Font("serif",Font.BOLD, 30));
			g.drawString("(Space to Restart)", 230,430);    // in dòng "bấm space để chơi lại"
		}
		else if(player2lives == 0)
		{
			g.setColor(Color.white);
			g.setFont(new Font("serif",Font.BOLD, 60));
			g.drawString("Game Over", 200,300);
			g.drawString("Player 1 Won", 180,380);
			g.setColor(Color.white);
			g.setFont(new Font("serif",Font.BOLD, 30));
			g.drawString("(Space to Restart)", 230,430);
			play = false;
		}
		
		g.dispose();  // để làm gì thì không biết
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
	
		repaint();
	}

	private class Player1Listener implements KeyListener
	{
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}		
		public void keyPressed(KeyEvent e) {	
			if(e.getKeyCode()== KeyEvent.VK_SPACE && (player1lives == 0 || player2lives == 0))    // nếu bấm space khi hp của p1 hoặc p2 =0
			{
				br = new brick();        //làm gạch mới
				player1X = 200;      //đặt lại tọa độ p1
				player1Y = 550;	
				player1right = false;   // đặt lại trạng thái p1
				player1left = false;
				player1down = false;
				player1up = true;	
				
				player2X = 400;         //đặt lại tọa độ p2
				player2Y = 550;	
				player2right = false;    // đặt lại trạng thái p2
				player2left = false;
				player2down = false;
				player2up = true;	
				
				player1score = 0;    // điểm 2 đứa =0, mỗi thằng 5 máu
				player1lives = 5;
				player2score = 0;
				player2lives = 5;
				play = true;         // đặt trạng thái thành "đang chơi"
				repaint();
			}
			if(e.getKeyCode()== KeyEvent.VK_U)  // nếu như nhận chữ U từ bàn phím
			{
				if(!player1Shoot)      // nếu như p1 đang không bắn gì
				{
					if(player1up)   // nếu p1 đang "up"
					{					
						player1Bullet = new Player1Bullet(player1X + 20, player1Y);     //
					}
					else if(player1down)
					{					
						player1Bullet = new Player1Bullet(player1X + 20, player1Y + 40);
					}
					else if(player1right)
					{				
						player1Bullet = new Player1Bullet(player1X + 40, player1Y + 20);
					}
					else if(player1left)
					{			
						player1Bullet = new Player1Bullet(player1X, player1Y + 20);
					}
					
					player1Shoot = true;
				}
			}
			if(e.getKeyCode()== KeyEvent.VK_W)     // lệnh di chuyển của p1, nếu nhận W:
			{
				player1right = false;
				player1left = false;
				player1down = false; 
				player1up = true;	       // "player1up" thành true, nhích lên
				
				if(!(player1Y < 10))      // nếu như tọa độ vẫn còn trong màn hình thì vẫn di chuyển
					player1Y-=10;

			}
			if(e.getKeyCode()== KeyEvent.VK_A)
			{
				player1right = false;
				player1left = true;
				player1down = false;
				player1up = false;
				
				if(!(player1X < 10))
					player1X-=10;
			}
			if(e.getKeyCode()== KeyEvent.VK_S)
			{
				player1right = false;
				player1left = false;
				player1down = true;
				player1up = false;
				
				if(!(player1Y > 540))
					player1Y+=10;
			}
			if(e.getKeyCode()== KeyEvent.VK_D)
			{
				player1right = true;
				player1left = false;
				player1down = false;
				player1up = false;
				
				if(!(player1X > 590))
					player1X+=10;
			}
		}
	}
	
	private class Player2Listener implements KeyListener
	{
		public void keyTyped(KeyEvent e) {}              // như trên, nhưng của p2 
		public void keyReleased(KeyEvent e) {}		
		public void keyPressed(KeyEvent e) {	
			if(e.getKeyCode()== KeyEvent.VK_M)
			{
				if(!player2Shoot)
				{
					if(player2up)
					{					
						player2Bullet = new Player2Bullet(player2X + 20, player2Y);
					}
					else if(player2down)
					{					
						player2Bullet = new Player2Bullet(player2X + 20, player2Y + 40);
					}
					else if(player2right)
					{				
						player2Bullet = new Player2Bullet(player2X + 40, player2Y + 20);
					}
					else if(player2left)
					{			
						player2Bullet = new Player2Bullet(player2X, player2Y + 20);
					}
					
					player2Shoot = true;
				}
			}
			if(e.getKeyCode()== KeyEvent.VK_UP)
			{
				player2right = false;
				player2left = false;
				player2down = false;
				player2up = true;		
				
				if(!(player2Y < 10))
					player2Y-=10;

			}
			if(e.getKeyCode()== KeyEvent.VK_LEFT)
			{
				player2right = false;
				player2left = true;
				player2down = false;
				player2up = false;
				
				if(!(player2X < 10))
					player2X-=10;
			}
			if(e.getKeyCode()== KeyEvent.VK_DOWN)
			{
				player2right = false;
				player2left = false;
				player2down = true;
				player2up = false;
				
				if(!(player2Y > 540))
					player2Y+=10;
			}
			if(e.getKeyCode()== KeyEvent.VK_RIGHT)
			{
				player2right = true;
				player2left = false;
				player2down = false;
				player2up = false;
				
				if(!(player2X > 590))
					player2X+=10;
			}
			
		}
	}
}
