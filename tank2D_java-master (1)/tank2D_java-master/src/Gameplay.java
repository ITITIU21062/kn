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
	private String bulletShootDir1 = "";   // bắn đâu
	
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
	
	private Timer timer;    // bộ đếm tg
	private int delay=8;    // delay
	
	private Player1Listener player1Listener;    //lát xuống kia gán phím bấm
	private Player2Listener player2Listener;
	
	private Player1Bullet player1Bullet = null;
	private Player2Bullet player2Bullet = null;
	
	private boolean play = true;
	
	private Item items;
	private boolean[][] explosionMap = new boolean[650][600];
	int countExpl1=0;    //đếm số đạn nổ của p1
	int countExpl2=0;    // như trên nhưng là p2
	int deleteExpl=0;    // bộ đếm để xóa vụ nổ
	
	public Gameplay()
	{				
		br = new brick();                                // gọi class gạch
		player1Listener = new Player1Listener();
		player2Listener = new Player2Listener();
		setFocusable(true);
		//addKeyListener(this);
		addKeyListener(player1Listener);
		addKeyListener(player2Listener);
		setFocusTraversalKeysEnabled(false);
        	timer=new Timer(delay,this);
		timer.start();
		
		
		int[][] occupiedPositions = new int[br.bricksXPos.length + br.solidBricksXPos.length][2];
	    for (int i = 0; i < br.bricksXPos.length; i++) {
	        occupiedPositions[i][0] = br.bricksXPos[i];
	        occupiedPositions[i][1] = br.bricksYPos[i];
	    }
	    for (int i = 0; i < br.solidBricksXPos.length; i++) {
	        occupiedPositions[br.bricksXPos.length + i][0] = br.solidBricksXPos[i];
	        occupiedPositions[br.bricksXPos.length + i][1] = br.solidBricksYPos[i];
	    }
	    items = new Item(650, 600, 50, occupiedPositions);
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
		
		items.draw(this, g);           // vẽ items
		drawExplosion(g);
		
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
			if(player2up)                                             // vẽ p2, i chang p1
				player2=new ImageIcon("player2_tank_up.png");	
			else if(player2down)
				player2=new ImageIcon("player2_tank_down.png");
			else if(player2right)
				player2=new ImageIcon("player2_tank_right.png");
			else if(player2left)
				player2=new ImageIcon("player2_tank_left.png");
						
			player2.paintIcon(this, g, player2X, player2Y);
			
			
			
			if (items.checkCollision(player1X, player1Y)==0) {            // nhặt tnt
				countExpl1+=1;
	        }
			
			if (items.checkCollision(player2X, player2Y)==0) {            // nhặt tnt p2
				countExpl2+=1;
	        }
			
			/*if (items.checkCollision(player1X, player1Y)==0) {            // nhặt hp
				player1lives+=2;
				player1score+=1;
	        }
			
			if (items.checkCollision(player2X, player2Y)==0) {            // nhặt hp p2
				player2lives+=2;
				player2score+=1;
	        }*/
			
			if (deleteExpl>=3) {                 // làm 3 hành động thì xóa vụ nổ
				deleteExplosion(g);
				deleteExpl=0;
				resetExplosionMap();
				}
			
			
			
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
					if (countExpl1 >=1) {
						floodFill(player2X + 25, player2Y + 25, 70);           // nếu có đạn tnt thì sẽ nỗ chỗ p2
						countExpl1-=1;                                         // số đạn tnt -1
						player2lives -= 1;                                     // p2 mất thêm 1 máu
					}
					player1score += 10;        // điểm p1 +10
					player2lives -= 1;         // hp p2 -1
					player1Bullet = null;      // đặt số đạn về null
					player1Shoot = false;      // đặt lệnh bắn về false
					bulletShootDir1 = "";
				}
				
				if(br.checkCollision(player1Bullet.getX(), player1Bullet.getY())
						|| br.checkSolidCollision(player1Bullet.getX(), player1Bullet.getY()))     // nếu như đạn đâm cục gạch ko bể đc
				{
					if (countExpl2 >=1) {
						//floodFill(player2X + 25, player2Y + 25, 70);
						countExpl2-=1;
					}
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
					if (countExpl2 >=1) {
						floodFill(player1X + 25, player1Y + 25, 70);
						countExpl2-=1;
						player1lives -= 1;
					}
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
		
		if(player1lives <= 0)                                   // nếu như hp p1 <=0
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
		else if(player2lives <= 0)
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
	
	
	private void floodFill(int x, int y, int radius) {
	    if (x < 0 || y < 0 || x >= explosionMap.length || y >= explosionMap[0].length) return;  // nếu nằm ngoài màn hình thì ko nổ nữa
	    if (explosionMap[x][y] || radius <= 0) return;

	    explosionMap[x][y] = true;
	    
	    if (br.checkCollision(x, y)) {
	        br.breakBrick(x, y); // Phương thức này cần được định nghĩa trong lớp brick
	    }

	    floodFill(x + 1, y, radius - 1);       // vụ nổ lan về bên phải, nhỏ dần
	    floodFill(x - 1, y, radius - 1);
	    floodFill(x, y + 1, radius - 1);       // tóm lại này là dùng đệ quy để vẽ vụ nổ, lan dần về 4 hướng và nhỏ dần
	    floodFill(x, y - 1, radius - 1);
	}

	// Vẽ vụ nổ trên màn hình
	private void drawExplosion(Graphics g) {
	    g.setColor(Color.ORANGE);                                                // vụ nổ sẽ màu cam
	    for (int i = 0; i < explosionMap.length; i++) {                          // duyệt qua mảng expM coi chỗ nào nổ thì tô
	        for (int j = 0; j < explosionMap[0].length; j++) {
	            if (explosionMap[i][j]) {
	                g.fillRect(i, j, 5, 5); // Vẽ ô vuông nhỏ cho hiệu ứng
	            }
	        }
	    }
	}
	
	private void deleteExplosion(Graphics g) {
	    g.setColor(Color.BLACK);
	    for (int i = 0; i < explosionMap.length; i++) {
	        for (int j = 0; j < explosionMap[0].length; j++) {                   // duyệt như trên nhưng mà coi chỗ nào nổ rồi thì:
	            if (explosionMap[i][j]) {
	                g.fillRect(i, j, 5, 5);                                      // Xóa ô vuông nhỏ của vụ nổ
	                explosionMap[i][j] = false;                                  // Đặt lại phần tử trong mảng về false hết
	            }
	        }
	    }
	}
	
	private void resetExplosionMap() {                                           // để đặt các phần tử trong mảng về false hết như cái trên, thêm 1 cái cho chắc
	    for (int i = 0; i < explosionMap.length; i++) {
	        Arrays.fill(explosionMap[i], false);
	    }
	}
	

	private class Player1Listener implements KeyListener
	{
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}		
		public void keyPressed(KeyEvent e) {	
			if(e.getKeyCode()== KeyEvent.VK_SPACE && (player1lives <= 0 || player2lives <= 0))    // nếu bấm space khi hp của p1 hoặc p2 =0
			{
				brick br = new brick();        // gọi class gạch để reset cả gạch nhưng khó quá bug mãi nên kệ.
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
			if(e.getKeyCode()== KeyEvent.VK_U)  // nếu như nhận chữ "U" từ bàn phím
			{
				deleteExpl+=1;
				if(!player1Shoot)      // nếu như p1 đang không bắn gì
				{
					if(player1up)   // nếu p1 đang "up"
					{					
						player1Bullet = new Player1Bullet(player1X + 20, player1Y);     // vị trí đạn bắn ra so với playerX và playerY
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
				deleteExpl+=1;
				player1right = false;
				player1left = false;
				player1down = false; 
				player1up = true;	       // "player1up" thành true, nhích lên
				
				if(!br.checkTankCollision(player1X, player1Y - 10) && player1Y > 0)               //nếu như còn trong màn hình và ko húc vào gạch thì vẫn chạy 
					player1Y -= 10;

			}
			if(e.getKeyCode()== KeyEvent.VK_A)
			{
				deleteExpl+=1;
				player1right = false;
				player1left = true;
				player1down = false;
				player1up = false;
				
				if(!br.checkTankCollision(player1X - 10, player1Y) && player1X > 0) 
					player1X -= 10;
			}
			if(e.getKeyCode()== KeyEvent.VK_S)
			{
				deleteExpl+=1;
				player1right = false;
				player1left = false;
				player1down = true;
				player1up = false;
				
				if(!br.checkTankCollision(player1X, player1Y + 10) && player1Y < 550) 
					player1Y += 10;
			}
			if(e.getKeyCode()== KeyEvent.VK_D)
			{
				deleteExpl+=1;
				player1right = true;
				player1left = false;
				player1down = false;
				player1up = false;
				
				if(!br.checkTankCollision(player1X + 10, player1Y) && player1X < 600)
					player1X += 10;
			}
		}
	}
	
	private class Player2Listener implements KeyListener               // như trên nhưng mà là p2
	{
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}		
		public void keyPressed(KeyEvent e) {	
			if(e.getKeyCode()== KeyEvent.VK_M)
			{
				deleteExpl+=1;
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
				deleteExpl+=1;
				player2right = false;
				player2left = false;
				player2down = false;
				player2up = true;		
				
				if(!br.checkTankCollision(player2X, player2Y - 10) && player2Y > 0) 
					player2Y -= 10;

			}
			if(e.getKeyCode()== KeyEvent.VK_LEFT)
			{
				deleteExpl+=1;
				player2right = false;
				player2left = true;
				player2down = false;
				player2up = false;
				
				if(!br.checkTankCollision(player2X - 10, player2Y) && player2X > 0) 
					player2X -= 10;
			}
			if(e.getKeyCode()== KeyEvent.VK_DOWN)
			{
				deleteExpl+=1;
				player2right = false;
				player2left = false;
				player2down = true;
				player2up = false;
				
				if(!br.checkTankCollision(player2X, player2Y + 10) && player2Y < 550) 
					player2Y += 10;
			}
			if(e.getKeyCode()== KeyEvent.VK_RIGHT)
			{
				deleteExpl+=1;
				player2right = true;
				player2left = false;
				player2down = false;
				player2up = false;
				
				if(!br.checkTankCollision(player2X + 10, player2Y) && player2X < 600) 
					player2X += 10;
			}
			
		}
	}
}
