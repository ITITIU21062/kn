import java.awt.Color;

import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
		JFrame obj = new JFrame();
		Gameplay gamePlay = new Gameplay();
		
		obj.setBounds(10, 10, 800, 630); // Đặt kích thước cửa sổ game
		obj.setTitle("Tank 2D"); // Tiêu đề của cửa sổ game
		obj.setBackground(Color.gray); // Màu nền của cửa sổ
		obj.setResizable(false); // Không cho phép thay đổi kích thước cửa sổ
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đóng ứng dụng khi tắt cửa sổ
		obj.add(gamePlay); // Thêm đối tượng Gameplay (nơi xử lý logic game) vào JFrame
		obj.setVisible(true); // Hiển thị cửa sổ game

	}

}
