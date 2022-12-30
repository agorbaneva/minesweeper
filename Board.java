import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel {
	private static int blockSize = 40;
	private int boardWidth = 18;
	private int boardHeight = 14;
	private int width = boardWidth*blockSize + blockSize;
	private Color background1 = new Color(119, 179, 209);
	private Color background2 = new Color(119, 170, 209);
	private Color revealed1 = new Color(229, 194, 159);
	private Color revealed2 = new Color(215, 184, 153);
	private Color bar = new Color(91, 141, 160);
	private Color highlight = new Color(176, 213, 238);
	
	private int mouseMoveX;
	private int mouseMoveY;
	
	private HashMap<String, ImageIcon> imageIcons;
	private HashMap<Integer, String> iconKey;
	
	Game minesweeper;
	
	private int sec = 0;
	private int bestTime;
	
	private Cell[][] finalCells;
	
	public Board(Game m) {
		minesweeper = m;
		Move motion = new Move();
		this.addMouseMotionListener(motion);
		String path = "customImages/";
		String[] paths = new String[] {"one", "two", "three", "four", "five", "six", "seven", "eight", "mine", "flag", "clock", "star", "trophy"};
		imageIcons = new HashMap<>();

		for(int index=0; index<paths.length; index++){
			imageIcons.put(paths[index], new ImageIcon(path+paths[index] + ".png"));
		}

		iconKey = new HashMap<>();
		for(int index=0; index<9; index++){
			iconKey.put(index+1, paths[index]);
		}
	}
	
	public void paintComponent(Graphics g) {
		finalCells = minesweeper.getCells(); //obtains cells
		//makes the playing board
		for(int x = 0; x < boardWidth; x ++) {
			for(int y = 0; y < boardHeight; y++) {
				Cell currentCell = finalCells[y][x];
				Color backgroundColor = getBackground(x, y, currentCell.isRevealed());
				int xPos = x*blockSize + blockSize/2;
				int yPos = y*blockSize + 2*blockSize;
				if(currentCell.getFlag()) {
					g.setColor(backgroundColor);
					if(mouseMoveX == x && mouseMoveY == y) {
						g.setColor(highlight);
					}
					g.fillRect(xPos, yPos, blockSize, blockSize);
					imageIcons.get("flag").paintIcon(this, g, xPos, yPos);
				}
				else if(!currentCell.isRevealed()) {
					g.setColor(backgroundColor);
					if(mouseMoveX == x && mouseMoveY == y) {
						g.setColor(highlight);
					}
					g.fillRect(xPos, yPos, blockSize, blockSize);
				}
				if(currentCell.isRevealed()) {
					g.setColor(backgroundColor);
					g.fillRect(xPos, yPos, blockSize, blockSize);
					if(currentCell.getNumber() != 0) {
						int number = currentCell.getNumber();
						String key = iconKey.get(number);
						ImageIcon icon = imageIcons.get(key);
						icon.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getMine()) {
						ImageIcon mine = imageIcons.get("mine");
						if(minesweeper.getWin()) {
							mine = imageIcons.get("star");
						}
						mine.paintIcon(this,  g,  xPos, yPos);
					}
				}
			}
		}
		//makes the banner
		g.setColor(bar);
		g.fillRect(0, 0, width, 2*blockSize);
		
		//flag count
		imageIcons.get("flag").paintIcon(this, g, blockSize, blockSize/2);
		g.setColor(Color.white);
		g.setFont(new Font("Helvetica", Font.BOLD, 36));
		g.drawString(Integer.toString(minesweeper.getFlagCount()), 2*blockSize + blockSize/8, 3*blockSize/8 + blockSize);
		
		//time count
		imageIcons.get("clock").paintIcon(this, g, blockSize*8 + blockSize/2, blockSize/2);
		if(!minesweeper.getFinished()) {
			sec = minesweeper.getTime();
		}
		g.drawString(Integer.toString(sec), 9*blockSize + 3*blockSize/4, 3*blockSize/8 + blockSize);
		
		//best time
		imageIcons.get("trophy").paintIcon(this, g, blockSize*15, blockSize/2 + 1);
		try {
			bestTime = minesweeper.getBestTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		g.drawString(Integer.toString(bestTime), 16*blockSize + blockSize/8, 3*blockSize/8 + blockSize);
	}
	
	public Color getBackground(int x, int y, boolean isRevealed) {
		Color c;
		if(isRevealed) {
			if((x+y)%2==0)
				c = revealed1;
			else
				c = revealed2;
		}
		else {
			if((x+y)%2==0)
				c = background1;
			else
				c = background2;
		}
		return c;
	}
	
	public class Move implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent arg0) {
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			mouseMoveX = (arg0.getX() - blockSize/2)/blockSize;
			mouseMoveY = (arg0.getY() - 2*blockSize)/blockSize;
			repaint();
		}
    }
}