import java.awt.Color;

import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
		JFrame obj = new JFrame();
		Gameplay gamePlay = new Gameplay();
		
		obj.setBounds(10, 10, 800, 630); // set kích thước cửa sổ game
		obj.setTitle("Tank 2D"); // đặt tiêu đề cửa sổ game
		obj.setBackground(Color.gray); // set Màu nền
		obj.setResizable(false); // Không cho phép thay đổi kích thước cửa sổ
		
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng ứng dụng bằng cái nút đỏ
		obj.add(gamePlay); // bỏ gamePlay JFrame
		obj.setVisible(true); // Hiển thị cửa sổ game

	}

}
