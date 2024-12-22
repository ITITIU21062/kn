import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.ImageIcon;

public class Item {
    private int itemsXPos[];          // tọa độ của item
    private int itemsYPos[];
    private boolean itemsActive[];    // đã bị húc chưa
    private ImageIcon itemImages[];   // khai báo mảng để lưu item và hình của nó
    private int itemTypes[];          // iteam gì?
    private Random random;

    public Item(int mapWidth, int mapHeight, int brickSize, int[][] occupiedPositions) {
        random = new Random();
        itemImages = new ImageIcon[1];     // chỉ dùng mỗi tnt nên khai 1 thôi
        itemImages[0] = new ImageIcon(new ImageIcon("tnt.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));               // tnt
        //itemImages[1] = new ImageIcon(new ImageIcon("heal.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
        //itemImages[2] = new ImageIcon(new ImageIcon("snowflake.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
        
        int maxItems = 10; // Số lượng item tối đa
        itemsXPos = new int[maxItems];
        itemsYPos = new int[maxItems];
        itemsActive = new boolean[maxItems];
        itemTypes = new int[maxItems];
        
        // Sinh vị trí cho các item
        for (int i = 0; i < maxItems; i++) {
            int x, y;
            do {
                x = random.nextInt(mapWidth / brickSize) * brickSize;
                y = random.nextInt(mapHeight / brickSize) * brickSize;
            } while (isOccupied(x, y, occupiedPositions));
            
            itemsXPos[i] = x;
            itemsYPos[i] = y;
            itemsActive[i] = true;
            itemTypes[i] = random.nextInt(1); // Randomly assign one of three types
        }
    }

    private boolean isOccupied(int x, int y, int[][] occupiedPositions) {    // coi chỗ đó có gạch không
        for (int[] pos : occupiedPositions) {
            if (pos[0] == x && pos[1] == y) {
                return true; // Vị trí bị chiếm bởi gạch hoặc vật cản khác
            }
        }
        return false;
    }

    public void draw(Component c, Graphics g) {                // vẽ item
        for (int i = 0; i < itemsXPos.length; i++) {
            if (itemsActive[i]) {
                itemImages[itemTypes[i]].paintIcon(c, g, itemsXPos[i], itemsYPos[i]);
            }
        }
    }

    public int checkCollision(int x, int y) {              // xem xe tăng có đâm item ko
        for (int i = 0; i < itemsXPos.length; i++) {
            if (itemsActive[i] &&
                new Rectangle(x, y, 30, 30).intersects(new Rectangle(itemsXPos[i], itemsYPos[i], 30, 30))) {
                itemsActive[i] = false; // Item bị nhặt
                return itemTypes[i]; // trả về loại item bị nhặt, nhưng có vẻ bug
            }
        }
        return -1; // No collision
    }
}
