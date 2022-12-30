import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel {
	private static int blockSize = 40;
	private int boardWidth = 18;
   private int boardHeight = 14;
   private int width = boardWidth*blockSize + blockSize;
   private int height = boardHeight*blockSize+2*blockSize + blockSize/2;
   private Color background1 = new Color(119, 179, 209);
	private Color background2 = new Color(119, 170, 209);
	private Color revealed1 = new Color(229, 194, 159);
	private Color revealed2 = new Color(215, 184, 153);
	private Color bar = new Color(91, 141, 160);
	private Color temp = new Color(255, 81, 72);
	private Color highlight = new Color(176, 213, 238);
	
	private int mouseMoveX;
	private int mouseMoveY;
	
	private ImageIcon one;
	private ImageIcon two;
	private ImageIcon three;
	private ImageIcon four;
	private ImageIcon five;
	private ImageIcon six;
	private ImageIcon seven;
	private ImageIcon eight;
	private ImageIcon flag;
	private ImageIcon iconFlag;
	private ImageIcon clock;
	private ImageIcon mine;
	private ImageIcon trophy;
	
	Game minesweeper;
	
	private int sec = 0;
	private int bestTime;
	
	private Cell[][] finalCells;
	
	public Board(Game m) {
		minesweeper = m;
		Move motion = new Move();
		this.addMouseMotionListener(motion);
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
					flag = new ImageIcon("flag.png");
					flag.paintIcon(this, g, xPos, yPos);
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
					if(currentCell.getNumber() == 1) {
						one = new ImageIcon("one.png");
						one.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getNumber() == 2) {
						two = new ImageIcon("two.png");
						two.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getNumber() == 3) {
						three = new ImageIcon("three.png");
						three.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getNumber() == 4) {
						four = new ImageIcon("four.png");
						four.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getNumber() == 5) {
						five = new ImageIcon("five.png");
						five.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getNumber() == 6) {
						six = new ImageIcon("six.png");
						six.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getNumber() == 7) {
						seven = new ImageIcon("seven.png");
						seven.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getNumber() == 8) {
						eight = new ImageIcon("two.png");
						eight.paintIcon(this, g, xPos, yPos);
					}
					else if(currentCell.getMine()) {
						mine = new ImageIcon("mine.png");
						if(minesweeper.getWin()) {
							mine = new ImageIcon("star.png");
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
		iconFlag = new ImageIcon("flag.png");
		iconFlag.paintIcon(this, g, blockSize, blockSize/2);
		g.setColor(Color.white);
		g.setFont(new Font("Helvetica", Font.BOLD, 36));
		g.drawString(Integer.toString(minesweeper.getFlagCount()), 2*blockSize + blockSize/8, 3*blockSize/8 + blockSize);
		
		//time count
		clock = new ImageIcon("clock.png");
		clock.paintIcon(this, g, blockSize*8 + blockSize/2, blockSize/2);
		if(!minesweeper.getFinished()) {
			sec = minesweeper.getTime();
		}
		g.drawString(Integer.toString(sec), 9*blockSize + 3*blockSize/4, 3*blockSize/8 + blockSize);
		
		//best time
		trophy = new ImageIcon("trophy.png");
		trophy.paintIcon(this, g, blockSize*15, blockSize/2 + 1);
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