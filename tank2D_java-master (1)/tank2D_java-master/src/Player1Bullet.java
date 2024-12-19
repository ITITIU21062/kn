import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;


public class Player1Bullet {                         // tọa độ viên đạn
	private double x, y;
	
	public Player1Bullet(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void move(String face)               // đạn bay
	{
		if(face.equals("right"))            // nếu xe đang quay phải
			x += 5;                     // mỗi lần đạn bay 5 pixel
		else if(face.equals("left"))
			x -= 5;
		else if(face.equals("up"))
			y -= 5;
		else
			y +=5;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.yellow);                 // viên đạn màu vàng
		g.fillOval((int) x, (int) y, 10, 10);     // hình tròn
	}
	
	public int getX()
	{
		return (int)x;
	}
	public int getY()
	{
		return (int)y;
	}

}
