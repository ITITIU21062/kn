
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;


public class brick {
	int bricksXPos[] = {50,350,450,550,50,300,350,450,550,150,150,450,550,                                  // tọa độ x của cục gạch
						250,50,100,150,550,250,350,450,550,50,250,350,550,
						50,150,250,300,350,550,50,150,250,350,450,550,50,
						250,350,550};
	
	int bricksYPos[] = {50,50,50,50,100,100,100,100,100,150,200,200,200,250,                                // tọa ộ y của cục gạch
						300,300,300,300,350,350,350,350,400,400,400,400,450,
						450,450,450,450,450,500,500,500,500,500,500,550,550,
						550,550};
	
	int solidBricksXPos[] = {150,350,150,500,450,300,600,400,350,200,0,200,500};                            // tọa độ x của cục gạch không bể đc
	
	int solidBricksYPos[] = {0,0,50,100,150,200,200,250,300,350,400,400,450};                              // tọa độ x của cục gạch không bể đc
	
	int brickON[] = new int[42];                                                                           // trạng thái của cục gạch (1 là còn 0 là bể)
	
	private ImageIcon breakBrickImage;
	private ImageIcon solidBrickImage;
	
	public brick()
	{
		breakBrickImage=new ImageIcon("break_brick.jpg");           // hình cục gạch bể dc
		solidBrickImage=new ImageIcon("solid_brick.jpg");           // hình cục gạch không bể dc
		
		for(int i=0; i< brickON.length;i++)
		{
			brickON[i] = 1;
		}
	}
	
	public void draw(Component c, Graphics g)
	{
		for(int i=0; i< brickON.length;i++)                                                      // nếu cục gạch có status 1 thì vẽ nó
		{
			if(brickON[i]==1)
			{
				breakBrickImage.paintIcon(c, g, bricksXPos[i],bricksYPos[i]);
			}
		}
	}
	
	public void drawSolids(Component c, Graphics g)
	{
		for(int i=0; i< solidBricksXPos.length;i++)
		{			
			solidBrickImage.paintIcon(c, g, solidBricksXPos[i],solidBricksYPos[i]);        // vẽ cục gạch không thể bể
		}
	}
	
	public boolean checkCollision(int x, int y)               // này là kiểm tra coi đạn có đâm gạch không
	{
		boolean collided = false;
		for(int i=0; i< brickON.length;i++)                            // kiểm tra tất cả viên gạch
		{
			if(brickON[i]==1)                    // nếu viên gạch đó trạng thái là 1(chưa bể)
			{
				if(new Rectangle(x, y, 10, 10).intersects(new Rectangle(bricksXPos[i], bricksYPos[i], 50, 50)))   // cái intersect là để coi 2 hình cn có giao không
				{                                                                                                 // tức là kiểm tra cục gạch có va với đạn không
					brickON[i] = 0;         // nếu có thì status cục gạch về 0 (bể)
					collided = true;        // chuyển trạng thái collided (va chạm) thành có
					break;
				}
			}
		}
		
		return collided;
	}

	public boolean checkTankCollision(int x, int y)     // coi gạch với tank có húc nhau không
	{
		boolean collided = false;
		for(int i=0; i< brickON.length;i++)       // duyệt qua từng viên
		{
			if(brickON[i]==1)                  // nếu cục gạch chưa bể
			{
				
				Rectangle brickRect = new Rectangle(bricksXPos[i], bricksYPos[i], 50, 50);
				Rectangle tankRect = new Rectangle(x, y, 50, 50);
				//Rectangle solidBrickRect = new Rectangle(solidBricksXPos[i], solidBricksYPos[i], 50, 50);
				if(tankRect.intersects(brickRect))     // và nếu 2 th var nhau
				{
					collided = true;    // trả về là có va chạm
					break;
				}
				
				/*if(tankRect.intersects(solidBrickRect))
				{
					collided = true;
					break;
				}*/
			}
		}
		
		for (int i = 0; i < solidBricksXPos.length; i++) {
		    Rectangle solidBrickRect = new Rectangle(solidBricksXPos[i], solidBricksYPos[i], 50, 50);  // cái này là cho gạch không thể bể
		    Rectangle tankRect = new Rectangle(x, y, 50, 50);
		    if (tankRect.intersects(solidBrickRect)) {
		        collided = true;
		        break;
		    }
		}
		
		return collided;
	}
	
	public boolean checkSolidCollision(int x, int y)     // gạch không bể vs đạn
	{
		boolean collided = false;
		for(int i=0; i< solidBricksXPos.length;i++)
		{		
			if(new Rectangle(x, y, 10, 10).intersects(new Rectangle(solidBricksXPos[i], solidBricksYPos[i], 50, 50)))
			{		
				collided = true;               // gạch khoong bể, đạn bắn kệ
				break;
			}			
		}
		
		return collided;
	}
	
	public void breakBrick(int x, int y) {                                    // method phá gạch, xài chung với cái tnt
	    for (int i = 0; i < bricksXPos.length; i++) {
	        if (bricksXPos[i] == x && bricksYPos[i] == y) {
	            // Đánh dấu gạch bị phá vỡ (có thể loại bỏ khỏi danh sách)
	            bricksXPos[i] = -1; // Xóa hoặc gắn giá trị không hợp lệ
	            bricksYPos[i] = -1;
	            break;
	        }
	    }
	}

}
